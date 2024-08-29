package com.wzy.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import java.io.IOException;


@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;
    private RedisServer redisServer;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RedisServer redisServer() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("taskkill /F /IM redis*");
        process.waitFor();
        redisServer = new RedisServerBuilder()
                .port(redisPort) // 设置 Redis 监听的端口
                .setting("bind 127.0.0.1") // 设置 Redis 绑定的地址
                .setting("maxheap 128000000") // 设置最大堆内存，单位为字节
                .build();
        return redisServer;
    }


}
