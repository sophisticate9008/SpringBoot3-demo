package com.wzy.demo.job;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wzy.demo.common.RedisService;
import com.wzy.demo.entity.Balance;
import com.wzy.demo.entity.Bell;
import com.wzy.demo.entity.Bill;
import com.wzy.demo.entity.Commission;
import com.wzy.demo.entity.Reply;
import com.wzy.demo.entity.Subscribe;
import com.wzy.demo.service.BalanceService;
import com.wzy.demo.service.BellService;
import com.wzy.demo.service.BillService;
import com.wzy.demo.service.CommissionService;
import com.wzy.demo.service.SubscribeService;
import com.wzy.demo.service.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CommissionExpiredJob implements Job {

    @Autowired
    private CommissionService commissionService;
    @Autowired
    private SubscribeService subscribeService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RedisService redisService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int commissionId = dataMap.getInt("commissionId");
        deleteSubscribe(commissionId);
        Map<Integer,String> res = commissionService.allocate(commissionId);
        sendMessage(res);
    }

    public void deleteSubscribe(Integer commissionId) {
        List<Subscribe> list = subscribeService.getByCommissionId(commissionId);
        for (Subscribe subscribe : list) {
            subscribeService.remove(subscribe.getId());
        }
        String logContent = String.format("委托:%d结束,删除订阅", commissionId);
        log.debug(logContent);
    }

    // 在事务后调用以免错误通知
    public void sendMessage(Map<Integer, String> messages) {
        for (Map.Entry<Integer, String> entry : messages.entrySet()) {
            Integer userId = entry.getKey();
            String content = entry.getValue();
            
            // 获取用户的 uuid
            String uuid = redisService.getValue("uuid" + userId).toString();
            
            // 使用 WebSocket 发送消息
            messagingTemplate.convertAndSend("/topic/bell/" + uuid, content);
        }
    }

}
