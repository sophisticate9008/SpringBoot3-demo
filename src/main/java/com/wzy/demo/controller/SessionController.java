package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wzy.demo.common.ResultObj;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/session")
public class SessionController {
    @PostMapping("stop")
    public ResultObj stop() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null && currentUser.getSession() != null) {
            currentUser.getSession().stop(); // 销毁会话
            return ResultObj.OPERATION_SUCCESS;
        } else {
            return ResultObj.OPERATION_ERROR;
        }
    }

}
