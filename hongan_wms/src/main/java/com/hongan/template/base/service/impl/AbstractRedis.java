package com.hongan.template.base.service.impl;


import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public abstract class AbstractRedis {

    RedisTemplate redisTemplate;

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    public void expire(String key, int t) {
        redisTemplate.expire(key, t, TimeUnit.SECONDS);
    }

    public int getExpire(String key) {
        return exist(key) ? (int)(redisTemplate.getExpire(key, TimeUnit.MINUTES) + 1) : 0;
    }

}
