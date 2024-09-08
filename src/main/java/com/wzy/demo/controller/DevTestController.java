package com.wzy.demo.controller;


import com.wzy.demo.job.CommissionExpiredJob;
import com.wzy.demo.job.MyJob;
import com.wzy.demo.service.MyQuartzService;

import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev")
public class DevTestController {

    @Autowired
    private MyQuartzService quartzService;

    /**
     * 动态调度简单的延时任务
     *
     * @param timeInSeconds 延迟时间，单位秒
     * @return 返回任务执行结果
     */
    @PostMapping("/schedule/{timeInSeconds}")
    public String scheduleSimpleJob(@PathVariable int timeInSeconds) {
        try {
            Map<String, Object> jobData = new java.util.HashMap<>();
            jobData.put("commissionId", 1);
            quartzService.scheduleJob(CommissionExpiredJob.class, "sytssJob", "myGroup", timeInSeconds,jobData);
            return "任务将在 " + timeInSeconds + " 秒后执行。";
        } catch (Exception e) {
            e.printStackTrace();
            return "任务调度失败";
        }
    }

    /**
     * 动态调度基于 Cron 表达式的任务
     *
     * @param cronExpression Cron 表达式
     * @return 返回任务执行结果
     */
    @PostMapping("/schedule/cron")
    public String scheduleCronJob(@RequestParam String cronExpression) {
        try {
            quartzService.scheduleCronJob(MyJob.class, "myCronJob", "myGroup", cronExpression,null);
            return "Cron 任务调度成功，表达式: " + cronExpression;
        } catch (Exception e) {
            e.printStackTrace();
            return "Cron 任务调度失败";
        }
    }
    @PostMapping("/delJob/{name}")
    public String delJob(@PathVariable String name) {
        try {
            quartzService.deleteJob(name, "default");
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "删除成功";
    }
    @PostMapping("/delTrigger/{name}")
    public String delTrigger(@PathVariable String name) {
        try {
            quartzService.deleteTrigger(name, "DEFAULT");
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "删除成功";
    }
}
