package com.wzy.demo.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("demo")
                        .version("1.0")
                        // 作者
                        .contact(new Contact().name("wzy"))
                        // 描述
                        .description("spb3-demo Api接口文档")
                        // 服务条款网址-这里使用出处地址
                        .termsOfService("http://doc.xiaominfo.com")
                        // 许可证-这里使用出处信息
                        .license(new License().name("Apache 2.0")
                                .url("http://doc.xiaominfo.com")));
    }
    @Bean
    public GroupedOpenApi allEntities() {
        return GroupedOpenApi.builder()
                .packagesToScan("com.wzy.demo.controller", "com.wzy.demo.entity")
                .group("all")
                .pathsToMatch("/api/**")
                .build();
    }
}
