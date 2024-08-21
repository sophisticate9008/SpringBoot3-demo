package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wzy.demo.common.Constast;
import com.wzy.demo.common.DataGridView;
import com.wzy.demo.common.RedisService;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.common.WebUtils;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.MessageService;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wzy
 * @since 2024-08-15
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private RedisService redisService;
    @GetMapping("loadAll")
    public DataGridView loadAll() {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        return new DataGridView(messageService.getAllList(activUser.getAccount()));
    }
    
    @GetMapping("initUuid")
    public ResultObj initUuid(String uuid) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        redisService.setValue(uuid, activUser.getAccount());
        redisService.setValue("uuid" + activUser.getAccount(), uuid);
        return ResultObj.OPERATION_SUCCESS;
    }

    @GetMapping("changeObserve")
    public void changeObserve(String theObserved) {
        User activUser = (User) WebUtils.getSession().getAttribute("user");
        redisService.setValue(Constast.MESSAGE_FLAG + activUser.getAccount(), theObserved);
        messageService.readMessage(theObserved, activUser.getAccount());
    }
}
