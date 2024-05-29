package com.wzy.demo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;


@Configuration
@ConditionalOnClass(value= {PaginationInnerInterceptor.class})
public class MybatisPlusConfig {
    @Bean
    public PaginationInnerInterceptor  paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }
}
