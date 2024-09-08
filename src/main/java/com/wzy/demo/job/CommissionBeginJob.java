package com.wzy.demo.job;

import java.util.List;
import java.util.stream.Collectors;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.wzy.demo.common.RedisService;
import com.wzy.demo.entity.Subscribe;

import com.wzy.demo.service.BellService;
import com.wzy.demo.service.CommissionService;
import com.wzy.demo.service.SubscribeService;
import com.wzy.demo.service.UserService;

@Component
public class CommissionBeginJob implements Job {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CommissionService commissionService;
    @Autowired
    private SubscribeService subscribeService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BellService bellService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int commissionId = dataMap.getInt("commissionId");
        System.out.println("触发开始" + commissionId);
        sendMessage(commissionId);
    }

    public void sendMessage(Integer commissionId) {
        String commissionName = commissionService.getById(commissionId).getName();
        // 发送消息给所有订阅者
        List<Subscribe> all = subscribeService.getByCommissionId(commissionId);
        for (Subscribe subscribe : all) {
            bellService.add(subscribe.getUserId(), "您订阅的委托就要开始啦！ " + commissionName);
        }
        List<String> uuids = all.stream()
                .map(Subscribe::getUserId)
                .map(userId -> redisService.getValue("uuid" + userId).toString())
                .collect(Collectors.toList());
        for (String uuid : uuids) {
            messagingTemplate.convertAndSend("/topic/bell/" + uuid, "您订阅的委托就要开始啦！ " + commissionName);
        }
        
    }

}
