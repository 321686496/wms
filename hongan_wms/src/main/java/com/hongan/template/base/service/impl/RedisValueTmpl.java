package com.hongan.template.base.service.impl;

import com.hongan.template.base.service.IRedisValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: CaptchaCache
 */

@Service
public class RedisValueTmpl<T> extends AbstractRedis implements IRedisValue<T> {

    @Autowired
    public RedisValueTmpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public T getValue(String key) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    @Override
    public void setValue(String key, T value, int t) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        ops.set(key, value, Duration.ofSeconds(t));
    }

    @Override
    public void setValue(String key, T value) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }

    @Override
    public void incr(String key) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        ops.increment(key, 1);
    }

    @Override
    public void decr(String key) {
        ValueOperations<String, T> ops = redisTemplate.opsForValue();
        ops.increment(key, -1);
    }


    @Override
    public T randomSet(String key) {
        SetOperations<String, T> ops = redisTemplate.opsForSet();
        return ops.randomMember(key);
    }

    @Override
    public Long addSet(String key, T... value) {
        SetOperations<String, T> ops = redisTemplate.opsForSet();
        return ops.add(key, value);
    }

    @Override
    public Long deleteSet(String key, T... value) {
        SetOperations<String, T> ops = redisTemplate.opsForSet();
        return ops.remove(key, value);
    }

    @Override
    public Long sizeSet(String key) {
        SetOperations<String, T> ops = redisTemplate.opsForSet();
        return ops.size(key);
    }

    @Override
    public Set<T> membersSet(String key) {
        SetOperations<String, T> ops = redisTemplate.opsForSet();
        return ops.members(key);
    }

    @Override
    public Boolean existSet(String key, String member) {
        SetOperations<String, T> ops = redisTemplate.opsForSet();
        return ops.isMember(key, member);
    }

    @Override
    public T rightPop(String key) {
        ListOperations<String, T> ops = redisTemplate.opsForList();
        return ops.rightPop(key);
    }

    @Override
    public Long leftPush(String key, T value) {
        ListOperations<String, T> ops = redisTemplate.opsForList();
        return ops.leftPush(key, value);
    }

    @Override
    public T leftPop(String key) {
        ListOperations<String, T> ops = redisTemplate.opsForList();
        return ops.leftPop(key);
    }

    @Override
    public Long rightPush(String key, T value) {
        ListOperations<String, T> ops = redisTemplate.opsForList();
        return ops.rightPush(key, value);
    }

}

