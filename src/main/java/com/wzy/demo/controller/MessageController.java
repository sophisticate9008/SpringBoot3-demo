package com.wzy.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wzy.demo.common.RedisService;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.common.WebUtils;
import com.wzy.demo.entity.User;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/message")
public class MessageController {
    
    @Autowired
    private RedisService redisService;
    @GetMapping("connect")
    @Operation(description = "初始化websocket的uuid")
    public ResultObj connect(@RequestBody String uuid) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        redisService.setValue(uuid, activUser.getAccount());
        redisService.setValue(activUser.getAccount(),uuid );
        return ResultObj.OPERATION_SUCCESS;
    }
    

}
