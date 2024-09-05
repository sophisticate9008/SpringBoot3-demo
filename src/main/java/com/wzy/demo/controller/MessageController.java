package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wzy.demo.annotation.InjectUser;
import com.wzy.demo.common.Constast;
import com.wzy.demo.common.DataGridView;
import com.wzy.demo.common.RedisService;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.entity.User;
import com.wzy.demo.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;




/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wzy
 * @since 2024-08-15
 */

@InjectUser 
@RestController
@RequestMapping("/message")
public class MessageController {
    private User activeUser;
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private RedisService redisService;
    @GetMapping("loadAll")
    
    @Operation(summary = "获取所有消息", description = "获取所有消息")
    public DataGridView loadAll() {
        return new DataGridView(messageService.getAllList(activeUser.getAccount()));
    }
    
    @GetMapping("initUuid")
    public ResultObj initUuid(String uuid) {

        redisService.setValue(uuid, activeUser.getAccount());
        redisService.setValue("uuid" + activeUser.getAccount(), uuid);
        return ResultObj.OPERATION_SUCCESS;
    }

    @GetMapping("changeObserve")
    public void changeObserve(String theObserved) {
        redisService.setValue(Constast.MESSAGE_FLAG + activeUser.getAccount(), theObserved);
        messageService.readMessage(theObserved, activeUser.getAccount());
    }

    @GetMapping("unObserve")
    public void unObserve() {
        redisService.deleteValue(Constast.MESSAGE_FLAG + activeUser.getAccount());
    }
    
}
