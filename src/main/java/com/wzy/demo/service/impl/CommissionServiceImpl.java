package com.wzy.demo.service.impl;

import com.wzy.demo.common.HtmlStringHandle;
import com.wzy.demo.common.MyException;
import com.wzy.demo.entity.Commission;
import com.wzy.demo.entity.Reply;
import com.wzy.demo.job.CommissionBeginJob;
import com.wzy.demo.job.CommissionExpiredJob;
import com.wzy.demo.mapper.CommissionMapper;
import com.wzy.demo.mapper.ReplyMapper;
import com.wzy.demo.service.BellService;
import com.wzy.demo.service.BillService;
import com.wzy.demo.service.CommissionService;
import com.wzy.demo.service.MyQuartzService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wzy
 * @since 2024-06-11
 */
@Service
public class CommissionServiceImpl extends ServiceImpl<CommissionMapper, Commission> implements CommissionService {

    @Autowired
    private MyQuartzService quartzService;
    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private BellService bellService;
    @Autowired
    private BillService billService;
    @Override
    public boolean locked(Integer commissionId) {
        return this.getById(commissionId).getCurrentNum() > 0;
    }

    @Override
    public Integer getAndSetCommissionNum(Integer id) {
        QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<>();
        replyQueryWrapper.eq("commission_id", id);
        replyQueryWrapper.ne("state", 2);
        replyQueryWrapper.ne("state", -2);
        long count = replyMapper.selectCount(replyQueryWrapper);
        Commission commission = this.getById(id).setCurrentNum((int) count);
        if (commission.getState() == 0 || commission.getState() == 2) {
            if (commission.getCurrentNum() >= commission.getNum()) {
                commission.setState(2);
            } else {
                commission.setState(0);
            }
        }
        this.updateById(commission);
        return (int) count;
    }

    @Override
    public String htmlStringHandle(String htmlString) {
        return HtmlStringHandle.htmlStringHandle(htmlString);
    }

    @Override
    public String htmlStringHandle(String oldStr, String newStr) {
        return HtmlStringHandle.htmlStringHandle(oldStr, newStr);
    }

    @Override
    public void setCommissionBeginJob(Commission commission) {
        LocalDateTime now = LocalDateTime.now(); // 获取当前时间
        LocalDateTime beginTime = commission.getBeginTime(); // 获取 commission 的开始时间

        // 计算当前时间和 beginTime 之间的秒数差
        int seconds = Math.max((int) Duration.between(now, beginTime).getSeconds() - 60, 0);
        Map<String, Object> jobData = new HashMap<>();
        jobData.put("commissionId", commission.getId());
        try {
            quartzService.scheduleJob(CommissionBeginJob.class, "" + commission.getId(), "CommissionBeginJob", seconds,
                    jobData);
            log.debug("设定任务：" + commission.getId() + " setCommissionBeginJob " + seconds);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void setCommissionExpiredJob(Commission commission) {
        LocalDateTime now = LocalDateTime.now(); // 获取当前时间
        LocalDateTime endTime = commission.getEndTime(); // 获取 commission 的开始时间

        // 计算当前时间和 beginTime 之间的秒数差
        int seconds = (int) Duration.between(now, endTime).getSeconds();
        Map<String, Object> jobData = new HashMap<>();
        jobData.put("commissionId", commission.getId());
        try {
            quartzService.scheduleJob(CommissionExpiredJob.class, "" + commission.getId(), "CommissionExpiredJob",
                    seconds, jobData);
            log.debug("设定任务：" + commission.getId() + " setCommissionExpiredJob " + seconds);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public List<Reply> getApplyReplys(Integer commissionId) {
        QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<>();
        replyQueryWrapper.eq("commission_id", commissionId);
        replyQueryWrapper.eq("state", 1);
        return replyMapper.selectList(replyQueryWrapper);
    }

    @Transactional
    public Map<Integer, String> allocate(Integer commissionId) {
        // 获取委托和申请的回答
        Commission commission = this.getById(commissionId);
        List<Reply> replyListApply = this.getApplyReplys(commissionId);
        BigDecimal perGold = commission.getMoney();
        Integer numRest = commission.getNum() - commission.getCurrentNum();
        Map<Integer, Integer> userReplyCountMap = new HashMap<>();
        Map<Integer, String> messages = new HashMap<>();
    
        // 统计每个用户的回答数量
        for (Reply reply : replyListApply) {
            Integer userId = reply.getUserId();
            userReplyCountMap.put(userId, userReplyCountMap.getOrDefault(userId, 0) + 1);
        }
    
        // 定义一个辅助方法来检查操作是否成功
    
        // 分配金额并记录信息
        userReplyCountMap.forEach((userId, replyCount) -> {
            BigDecimal totalGold = perGold.multiply(BigDecimal.valueOf(replyCount));
            String msg = "您完成了委托:" + commission.getName() + "  " + replyCount + "份，获得金额：" + totalGold;
    
            MyException.throwRuntimeException(bellService.add(userId, msg), "记录bell信息失败: " + msg);
            MyException.throwRuntimeException(billService.add(userId, msg, totalGold), "记录bill信息失败: " + msg);
    
            messages.put(userId, msg);
        });
    
        // 返还剩余金额给委托人
        BigDecimal returnGold = perGold.multiply(BigDecimal.valueOf(numRest));
        String returnMsg = "您的委托:" + commission.getName() + "剩余金额份数" + numRest + "返还金额：" + returnGold;
        MyException.throwRuntimeException(bellService.add(commission.getUserId(), returnMsg), "记录bell信息失败: " + returnMsg);
        MyException.throwRuntimeException(billService.add(commission.getUserId(), returnMsg, returnGold), "记录bill信息失败: " + returnMsg);
    
        messages.put(commission.getUserId(), returnMsg);
        return messages;
    }
}
