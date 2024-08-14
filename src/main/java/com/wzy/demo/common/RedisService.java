package com.wzy.demo.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import java.time.Duration;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置值，并指定过期时间
     * 
     * @param key   键
     * @param value 值
     * @param ttl   过期时间
     */
    public void setValue(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    /**
     * 设置值，没有过期时间
     * 
     * @param key   键
     * @param value 值
     */
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取值
     * 
     * @param key 键
     * @return 值
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除值
     * 
     * @param key 键
     */
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 检查键是否存在
     * 
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置值，并指定过期时间（使用字符串）
     * 
     * @param key   键
     * @param value 值
     * @param ttl   过期时间（单位：秒）
     */
    public void setValue(String key, Object value, long ttl) {
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
    }

    public void setExpire(String key, Duration duration) {
        redisTemplate.expire(key, duration);
    }
}
