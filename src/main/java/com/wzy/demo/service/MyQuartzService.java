package com.wzy.demo.service;

import java.util.Map;

import org.quartz.Job;
import org.quartz.SchedulerException;

public interface MyQuartzService {

    /**
     * 调度简单的延迟任务（以秒为单位）
     *
     * @param jobClass  任务类
     * @param jobName   任务名称
     * @param groupName 任务组名称
     * @param triggerTime 触发时间，单位秒
     * @param jobData 任务数据,可为null
     * @throws SchedulerException 调度器异常
     */
    void scheduleJob(Class<? extends Job> jobClass, String jobName, String groupName, int triggerTime, Map<String, Object> jobData) throws SchedulerException;

    /**
     * 调度 Cron 表达式任务
     *
     * @param jobClass  任务类
     * @param jobName   任务名称
     * @param groupName 任务组名称
     * @param cronExpression Cron 表达式
     * @param jobData 任务数据,可为null
     * @throws SchedulerException 调度器异常
     */
    void scheduleCronJob(Class<? extends Job> jobClass, String jobName, String groupName, String cronExpression,Map<String, Object> jobData) throws SchedulerException;

        /**
     * 删除任务
     *
     * @param jobName   任务名称
     * @param groupName 任务组名称
     * @throws SchedulerException 调度器异常
     */
    void deleteJob(String jobName, String groupName) throws SchedulerException;

        /**
     * 删除触发器
     *
     * @param triggerName   触发器名称
     * @param triggerGroup  触发器组
     * @throws SchedulerException 调度器异常
     */
    void deleteTrigger(String triggerName, String triggerGroup) throws SchedulerException;
}
