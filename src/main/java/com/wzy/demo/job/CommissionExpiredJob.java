package com.wzy.demo.job;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wzy.demo.service.CommissionService;
@Component
public class CommissionExpiredJob implements Job{

    @Autowired
    private CommissionService commissionService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int commissionId = dataMap.getInt("commissionId");
        System.out.println("触发结束" + commissionId);
    }
    
}
