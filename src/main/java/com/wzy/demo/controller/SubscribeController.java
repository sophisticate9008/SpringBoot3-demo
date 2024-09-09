package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wzy.demo.annotation.InjectUser;
import com.wzy.demo.common.DataGridView;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.entity.Subscribe;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.SubscribeService;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wzy
 * @since 2024-09-08
 */
@InjectUser
@Controller
@RequestMapping("/subscribe")
public class SubscribeController {
    private User activerUser;
    @Autowired
    private SubscribeService subscribeService;

    @GetMapping("add")
    @Operation(summary = "添加订阅", description = "添加订阅")
    public ResultObj add(Integer commissionId) {
        if(subscribeService.add(activerUser.getId(), commissionId)){
            return ResultObj.ADD_SUCCESS;
        }else {
            return ResultObj.ADD_ERROR;
        }
    }
    @GetMapping("remove")
    @Operation(summary = "删除订阅", description = "删除订阅")
    public ResultObj remove(@RequestBody List<Integer> ids) {
        List<Integer> successList = new ArrayList<>();
        subscribeService.removeByIds(ids);
        return ResultObj.DELETE_SUCCESS.addOther("删除成功" + successList.toString());
    }

    @GetMapping("getAll")
    @Operation(summary = "获取所有订阅", description = "获取所有订阅")
    public DataGridView getAll() {
        return new DataGridView( subscribeService.getByUserId(activerUser.getId()));
    }
    
}
