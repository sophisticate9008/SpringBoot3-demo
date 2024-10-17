package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wzy.demo.annotation.InjectUser;
import com.wzy.demo.common.DataGridView;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.SubscribeService;
import com.wzy.demo.vo.MultiVo;

import io.jsonwebtoken.lang.Arrays;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wzy
 * @since 2024-09-08
 */
@InjectUser
@RestController
@RequestMapping("/subscribe")
public class SubscribeController {
    private User activeUser;
    @Autowired
    private SubscribeService subscribeService;

    @PostMapping("add")
    @Operation(summary = "添加订阅", description = "添加订阅")
    public ResultObj add(@RequestParam Integer commissionId) {

        if (subscribeService.add(activeUser.getId(), commissionId)) {
            return ResultObj.ADD_SUCCESS;
        } else {
            return ResultObj.ADD_ERROR;
        }
    }

    @GetMapping("isSubscribe")
    @Operation(summary = "判断是否订阅", description = "判断是否订阅")
    public ResultObj isSubscribe(Integer commissionId) {
        if (subscribeService.isSubscribed(activeUser.getId(), commissionId)) {
            return ResultObj.OPERATION_SUCCESS;
        } else {
            return ResultObj.OPERATION_ERROR;
        }
    }

    @PostMapping("removeByIds")
    @Operation(summary = "以id删除订阅", description = "以id删除订阅")
    public ResultObj removeByIds(@RequestBody MultiVo multiVo) {
        subscribeService.removeByIds(multiVo.getIds());
        return ResultObj.DELETE_SUCCESS;
    }

    @PostMapping("removeByCommissionId")
    @Operation(summary = "以commissionId删除订阅", description = "以commissionId删除订阅")
    public ResultObj removeByComissionIds(@RequestBody MultiVo multiVo) {
        if(subscribeService.removeByCommissionIds(activeUser.getId(),multiVo.getIds())) {
            return ResultObj.DELETE_SUCCESS;
        }else {
            return ResultObj.DELETE_ERROR;
        }
    }

    @GetMapping("getAll")
    @Operation(summary = "获取所有订阅", description = "获取所有订阅")
    public DataGridView getAll() {
        return new DataGridView(subscribeService.getByUserId(activeUser.getId()));
    }

}
