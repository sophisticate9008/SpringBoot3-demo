package com.wzy.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wzy.demo.annotation.InjectUser;
import com.wzy.demo.common.RedisService;
import com.wzy.demo.common.ResultObj;
import com.wzy.demo.common.WebUtils;
import com.wzy.demo.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.concurrent.TimeUnit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import java.time.Duration;


@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private RedisService redisService;
    private static final String STOP_REQUEST_KEY_PREFIX = "stop_request_";
    private static final Duration REQUEST_WINDOW = Duration.ofSeconds(3);
    private static final Duration DO = Duration.ofSeconds(2);
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    
    @PostMapping("stop")
    @Operation(summary = "清除会话", description = "浏览器关闭窗口时，清除会话")
    public ResultObj askStop(@RequestBody String arg ) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User activUser = (User) session.getAttribute("user");
        if(activUser == null) {
            return ResultObj.OPERATION_ERROR.addOther("未登录");
        }
        String redisKey = STOP_REQUEST_KEY_PREFIX + activUser.getAccount();
        boolean isFirstRequest = redisService.getValue(redisKey) == null;

        if (isFirstRequest && arg.equals("beforeunload")) {

            redisService.setValue(redisKey, "yes");
            redisService.setExpire(redisKey, REQUEST_WINDOW);
            relStop(redisKey, session);
            return ResultObj.OPERATION_SUCCESS;
        } else {
            redisService.deleteValue(redisKey);
            return ResultObj.OPERATION_ERROR;
        }

    }

    public void relStop(String redisKey, Session session) {

        executorService.schedule(() -> {
            try {
                if (redisService.getValue(redisKey) != null) {
                    Subject currentUser = new Subject.Builder().sessionId(session.getId()).buildSubject();
                    currentUser.getSession().stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, DO.toMillis(), TimeUnit.MILLISECONDS);
    }
}
