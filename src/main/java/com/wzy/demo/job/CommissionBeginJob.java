package com.wzy.demo.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CommissionBeginJob implements Job{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int commissionId = dataMap.getInt("commissionId");
        System.out.println("触发开始" + commissionId);
    }
    
}
