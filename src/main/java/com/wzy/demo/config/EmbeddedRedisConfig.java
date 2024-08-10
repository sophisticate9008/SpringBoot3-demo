package com.wzy.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

import java.io.IOException;

@Configuration
public class EmbeddedRedisConfig {

    private RedisServer redisServer;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RedisServer redisServer() throws IOException {
        redisServer = new RedisServerBuilder()
                .setting("maxheap 128000000")  // 设置最大堆内存，单位为字节
                .build();
        return redisServer;
    }
}
