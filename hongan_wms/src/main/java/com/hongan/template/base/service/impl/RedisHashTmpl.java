package com.hongan.template.base.service.impl;

import com.hongan.template.base.service.IRedisHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: CaptchaCache
 */

@Service
public class RedisHashTmpl<K, V> extends AbstractRedis implements IRedisHash<K, V> {


    @Autowired
    public RedisHashTmpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public V getKV(String h, K k) {
        HashOperations<String, K, V> ops = redisTemplate.opsForHash();
        return ops.get(h, k);
    }

    @Override
    public void setKV(String h, K k, V v) {
        HashOperations<String, K, V> ops = redisTemplate.opsForHash();
        ops.put(h, k, v);
    }

    @Override
    public Map<K, V> getKVAll(String h) {
        HashOperations<String, K, V> ops = redisTemplate.opsForHash();
        Set<K> keys = ops.keys(h);
        return keys.stream().collect(Collectors.toMap(k -> k, k -> ops.get(h, k)));
    }

    @Override
    public void setKVAll(String h, Map<K, V> map) {
        HashOperations<String, K, V> ops = redisTemplate.opsForHash();
        ops.putAll(h, map);
    }

    @Override
    public Set<K> keys(String h) {
        HashOperations<String, K, V> ops = redisTemplate.opsForHash();
        return ops.keys(h);
    }

    @Override
    public List<V> values(String h, Collection<K> keys) {
        HashOperations<String, K, V> ops = redisTemplate.opsForHash();
        return ops.multiGet(h,keys);
    }

    @Override
    public Map<K, V> keys(String h, Collection<K> keys) {
        HashOperations<String, K, V> ops = redisTemplate.opsForHash();
        return keys.stream().collect(Collectors.toMap(k -> k, k -> ops.get(h, k)));
    }

}

