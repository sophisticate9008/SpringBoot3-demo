package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzy.demo.common.DataGridView;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.common.WebUtils;
import com.wzy.demo.entity.Commission;
import com.wzy.demo.entity.Reply;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.CommissionService;
import com.wzy.demo.service.ReplyService;


import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@Controller
@RequestMapping("/reply")
@ResponseBody
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private CommissionService commissionService;

    @GetMapping("lock")
    @ResponseBody
    @Operation(summary = "锁定委托", description = "锁定委托")
    public ResultObj lock(Integer commissionId) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        Commission commission = commissionService.getById(commissionId);
        if (commission.getAccount().equals(activUser.getAccount())) {
            return ResultObj.LOCK_ERROR.addOther("或不能锁定自己的委托");
        }
        Reply replyEmpty = new Reply().setAccount(activUser.getAccount()).setContent("锁定委托")
                .setCommissionId(commissionId);
        return replyService.add(replyEmpty) ? ResultObj.LOCK_SUCCESS : ResultObj.LOCK_ERROR;
    }

    @GetMapping("unlock")
    @Operation(summary = "取消锁定", description = "取消锁定,之后对此委托失去锁定权")
    @ResponseBody
    public ResultObj unlock(Integer replyId) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        Reply entity = replyService.getById(replyId);
        if (!entity.getAccount().equals(activUser.getAccount())) {
            return ResultObj.Permission_Exceed;
        }

        return replyService.unlock(entity.getId()) ? ResultObj.UNLOCK_SUCCESS : ResultObj.UNLOCK_ERROR;
    }

    @PostMapping("update")
    @Operation(summary = "修改", description = "修改")
    public ResultObj update(@RequestBody Reply entity) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        if (!entity.getAccount().equals(activUser.getAccount())) {
            return ResultObj.Permission_Exceed;
        }
        Commission commission = commissionService.getById(entity.getCommissionId());
        entity.setReplyTime(LocalDateTime.now());
        if (commission.getEndTime().isBefore(entity.getReplyTime())) {
            return ResultObj.UPDATE_ERROR.addOther("该委托已结束");
        }
        entity.setState(0);
        return replyService.updateById(entity) ? ResultObj.UPDATE_SUCCESS : ResultObj.UPDATE_ERROR;
    }

    @GetMapping("getListByCommissionId")
    @Operation(summary = "根据委托id获取回复列表", description = "根据委托id获取回复列表(isOwner来确认是委托者还是回复者)")
    public DataGridView getMyListByCommissionId(@RequestParam Integer commissionId, @RequestParam Boolean isOwner) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        if (isOwner) {
            Commission commission = commissionService.getById(commissionId);
            if (!commission.getAccount().equals(activUser.getAccount())) {
                return new DataGridView(-1, "无权限");
            }
            QueryWrapper<Reply> wrapper = new QueryWrapper<Reply>().eq("commission_id", commissionId);
            return new DataGridView(replyService.getBaseMapper().selectList(wrapper));
        } else {
            QueryWrapper<Reply> wrapper = new QueryWrapper<Reply>().eq("commission_id", commissionId).eq("account",
                    activUser.getAccount());
            return new DataGridView(replyService.getBaseMapper().selectList(wrapper));
        }
    }

    @GetMapping("getListByUser")
    @Operation(summary = "获取我的回复列表", description = "获取我的回复列表")
    public DataGridView getListByUser() {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        return new DataGridView(replyService.getBaseMapper()
                .selectList(new QueryWrapper<Reply>().eq("account", activUser.getAccount())));
    }

}
