package com.wzy.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;

import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wzy.demo.entity.Reply;
import com.wzy.demo.service.CommissionService;
import com.wzy.demo.service.ReplyService;

@Aspect
@Component
public class ReplyAspect {

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private ReplyService replyService;

    // 在指定方法执行后触发
    @Pointcut("execution(* com.wzy.demo.service.impl.ReplyServiceImpl.*(..)) && args(replyId,..) && !execution(* com.wzy.demo.service.impl.ReplyServiceImpl.setState(..))")
    public void replyServiceMethodsWithReplyId(Integer replyId) {}

    @After("replyServiceMethodsWithReplyId(replyId)")
    public void afterMethodExecution(JoinPoint joinPoint, Integer replyId) {
        Reply reply = replyService.getById(replyId);
        if (reply != null) {
            commissionService.getAndSetCommissionNum(reply.getCommissionId());
        }
    }

}
