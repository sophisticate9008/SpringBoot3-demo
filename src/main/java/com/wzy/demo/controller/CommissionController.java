package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzy.demo.annotation.InjectUser;
import com.wzy.demo.common.DataGridView;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.entity.Commission;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.BellService;
import com.wzy.demo.service.BillService;
import com.wzy.demo.service.CommissionService;
import com.wzy.demo.vo.PageVo;

import io.swagger.v3.oas.annotations.Operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wzy
 * @since 2024-06-11
 */
@InjectUser
@Controller
@RequestMapping("/commission")
@ResponseBody
public class CommissionController {
    private User activeUser;
    @Autowired
    private CommissionService commissionService;
    @Autowired
    private BellService bellService;
    @Autowired
    private BillService billService;
    @PostMapping("add")
    @Transactional
    @Operation(summary = "添加委托", description = "添加委托")
    public ResultObj add(@RequestBody Commission commission) {
        if (commission.getUserId() == activeUser.getId()) {
            if (commission.getEndTime().isBefore(LocalDateTime.now())
                    || commission.getBeginTime().isAfter(commission.getEndTime())
                    || commission.getBeginTime().isBefore(LocalDateTime.now())) {
                return ResultObj.ADD_ERROR.addOther(",委托时间不符合逻辑");
            }
            commission.setState(0);
            commission.setDescription(commissionService.htmlStringHandle(commission.getDescription()));
            if(!checkAndConsumeGold(commission)) {
                return ResultObj.ADD_ERROR.addOther("余额不足");
            }
            if (commissionService.save(commission)) {
                commissionService.setCommissionBeginJob(commission);
                commissionService.setCommissionExpiredJob(commission);
                return ResultObj.ADD_SUCCESS;
            } else {
                return ResultObj.ADD_ERROR;
            }

        } else {
            return ResultObj.Permission_Exceed;
        }
    }

    @PostMapping("update")
    @Operation(summary = "修改委托", description = "修改委托(有锁定的不可修改,不可修改总金额,数量)")
    public ResultObj update(@RequestBody Commission commission) {
        Commission theCommission = commissionService.getById(commission.getId());
        //不可修改金额,数量
        commission.setMoney(theCommission.getMoney());
        commission.setNum(theCommission.getNum());
        if (theCommission.getUserId() == activeUser.getId()) {
            if (commissionService.locked(commission.getId())) {
                return ResultObj.Permission_Exceed.addOther(",委托被锁定");
            }
            theCommission.setDescription(
                    commissionService.htmlStringHandle(theCommission.getDescription(), commission.getDescription()));
            return commissionService.updateById(theCommission) ? ResultObj.UPDATE_SUCCESS : ResultObj.UPDATE_ERROR;
        } else {
            return ResultObj.Permission_Exceed;
        }
    }

    @GetMapping("stop")
    @Operation(summary = "终止委托", description = "终止委托(被锁定的不可)")
    public ResultObj stop(Integer id) {

        Commission commission = commissionService.getById(id);
        if (commission.getUserId() != activeUser.getId()) {
            return ResultObj.Permission_Exceed;
        }
        if (commissionService.locked(id)) {
            return ResultObj.Permission_Exceed.addOther(",委托被锁定");
        }
        commission.setState(-1);
        return commissionService.updateById(commission) ? ResultObj.OPERATION_SUCCESS : ResultObj.OPERATION_ERROR;
    }

    @PostMapping("list")
    @Operation(summary = "获取委托列表", description = "获取委托列表")
    public DataGridView getList(@RequestBody PageVo pageVo) {
        int page = pageVo.getPage();
        int limit = pageVo.getLimit();
        Page<Commission> pageRequest = new Page<>(page, limit);
        QueryWrapper<Commission> queryWrapper = new QueryWrapper<>();
        IPage<Commission> commissionPage = commissionService.getBaseMapper().selectPage(pageRequest, queryWrapper);
        return new DataGridView(commissionPage.getTotal(), commissionPage.getRecords());
    }

    @GetMapping("getById")
    @Operation(summary = "根据id获取委托", description = "根据id获取委托")
    public DataGridView getById(Integer id) {
        return new DataGridView(commissionService.getById(id));
    }

    public boolean checkAndConsumeGold(Commission commission) {
        BigDecimal consumeGold = commission.getMoney().multiply(BigDecimal.valueOf(commission.getNum()));
        billService.add(commission.getUserId(), "委托申请:" + commission.getName(), consumeGold);
        bellService.add(commission.getUserId(), "委托申请:" + commission.getName() + consumeGold);
        return true;
    }
}
