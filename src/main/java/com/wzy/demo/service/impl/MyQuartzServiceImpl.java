package com.wzy.demo.service.impl;

import com.wzy.demo.service.MyQuartzService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class MyQuartzServiceImpl implements MyQuartzService {

    @Autowired
    private Scheduler scheduler; // 注入 Quartz 调度器

    /**
     * 调度简单的延迟任务（以秒为单位）
     */
    @Override
    public void scheduleJob(Class<? extends Job> jobClass, String jobName, String groupName, int triggerTime,
            Map<String, Object> jobData) throws SchedulerException {
        JobDetail jobDetail;
        if (jobData != null) {
            JobDataMap jobDataMap = new JobDataMap(jobData); // 创建 JobDataMap

            jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, groupName)
                    .usingJobData(jobDataMap) // 将 JobDataMap 传递给 Job
                    .storeDurably()
                    .build();
        } else {
            jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, groupName)
                    .storeDurably()
                    .build();
        }

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobName + "Trigger", groupName)
                .startAt(new Date(System.currentTimeMillis() + triggerTime * 1000))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withRepeatCount(0) // 只执行一次
                        .withMisfireHandlingInstructionFireNow())
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            System.out.println("Job scheduled: " + jobName + " with trigger: " + trigger.getKey());
        } catch (SchedulerException e) {
            System.err.println("Failed to schedule job: " + jobName);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 调度 Cron 表达式任务
     */
    @Override
    public void scheduleCronJob(Class<? extends Job> jobClass, String jobName, String groupName, String cronExpression,
            Map<String, Object> jobData) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap(jobData);

        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, groupName)
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobName + "CronTrigger", groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            System.out.println("Job scheduled: " + jobName + " with trigger: " + trigger.getKey());
        } catch (SchedulerException e) {
            System.err.println("Failed to schedule job: " + jobName);
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteJob(String jobName, String groupName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, groupName);
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
            System.out.println("Job deleted: " + jobName);
        } else {
            System.out.println("Job not found: " + jobName);
        }
    }

    @Override
    public void deleteTrigger(String triggerName, String triggerGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
        if (scheduler.checkExists(triggerKey)) {
            scheduler.unscheduleJob(triggerKey);
            System.out.println("Trigger deleted: " + triggerName);
        } else {
            System.out.println("Trigger not found: " + triggerName);
        }
    }
}
