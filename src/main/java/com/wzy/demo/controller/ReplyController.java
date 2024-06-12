package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
 *  前端控制器
 * </p>
 *
 * @author wzy
 * @since 2024-06-11
 */
@Controller
@RequestMapping("/reply")
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
        Reply replyEmpty = new Reply().setAccount(activUser.getAccount()).setContent("锁定委托").setCommissionId(commissionId);
        return replyService.add(replyEmpty) ? ResultObj.LOCK_SUCCESS : ResultObj.LOCK_ERROR;
    }
    
    @PostMapping("unlock")
    @Operation(summary = "取消锁定", description = "取消锁定,之后对此委托失去锁定权")
    @ResponseBody
    public ResultObj unlock(Integer replyId) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        Reply entity = replyService.getById(replyId);
        if(!entity.getAccount().equals(activUser.getAccount())) {
            return ResultObj.Permission_Exceed;
        }
        entity.setState(-2);
        return replyService.updateById(entity) ? ResultObj.UNLOCK_SUCCESS : ResultObj.UNLOCK_ERROR;
    }

    @PostMapping("update")
    @Operation(summary = "修改", description = "修改")
    public ResultObj update(@RequestBody Reply entity) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        if(!entity.getAccount().equals(activUser.getAccount())) {
            return ResultObj.Permission_Exceed;
        }
        Commission commission = commissionService.getById(entity.getCommissionId());
        entity.setReplyTime(LocalDateTime.now());
        if(commission.getEndTime().isBefore(entity.getReplyTime())) {
            return ResultObj.UPDATE_ERROR.addOther("该委托已结束");
        }
        entity.setState(0);
        return replyService.updateById(entity) ? ResultObj.UPDATE_SUCCESS : ResultObj.UPDATE_ERROR;
    }
    

}
