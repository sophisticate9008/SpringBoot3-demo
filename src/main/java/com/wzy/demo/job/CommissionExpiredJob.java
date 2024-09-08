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
    private BellService bellService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private BillService billService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int commissionId = dataMap.getInt("commissionId");
        deleteSubscribe(commissionId);
        allocate(commissionId);
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

    @Transactional
    public void allocate(Integer commissionId) {
        // 获取所有订阅者
        // 获取委托
        Commission commission = commissionService.getById(commissionId);
        // 获取所有采取的回答
        List<Reply> replyListApply = commissionService.getApplyReplys(commissionId);
        BigDecimal perGold = commission.getMoney();
        Integer numRest = commission.getNum() - commission.getCurrentNum();
        Map<Integer, Integer> userReplyCountMap = new HashMap<>();
        Map<Integer, String> messages = new HashMap<>();
        for (Reply reply : replyListApply) {
            Integer userId = reply.getUserId(); // 假设 Reply 中有 getUserId 方法
            userReplyCountMap.put(userId, userReplyCountMap.getOrDefault(userId, 0) + 1);
        }
        // 分配金额给每个用户
        for (Map.Entry<Integer, Integer> entry : userReplyCountMap.entrySet()) {
            Integer userId = entry.getKey();
            Integer replyCount = entry.getValue();
            BigDecimal totalGold = perGold.multiply(BigDecimal.valueOf(replyCount)); // 计算用户总共获得的金额
            balanceService.add(userId, totalGold); // 增加用户余额
            String msg = "您完成了委托:" + commission.getName() + "  " + replyCount + "份，获得金额：" + totalGold;
            bellService.add(userId, msg); // 记录分配信息
            billService.add(userId, msg, totalGold);
            messages.put(userId, msg);
        }

        balanceService.add(commission.getUserId(), perGold);
        String msg = "您的委托:" + commission.getName() + "剩余金额份数" + numRest + "返还金额" + perGold.multiply(BigDecimal.valueOf(numRest));
        bellService.add(commission.getUserId(),msg);
        billService.add(commission.getUserId(), msg, perGold.multiply(BigDecimal.valueOf(numRest)));
        messages.put(commission.getUserId(), msg);
        sendMessage(messages);
    }
}
