package com.wzy.demo.factory;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @SuppressWarnings("null")
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // Create job instance
        Object jobInstance = super.createJobInstance(bundle);
        // Autowire the job instance
        applicationContext.getAutowireCapableBeanFactory().autowireBean(jobInstance);
        return jobInstance;
    }
}

