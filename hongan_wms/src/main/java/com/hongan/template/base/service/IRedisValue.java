package com.hongan.template.base.service;

import java.util.Set;

public interface IRedisValue<T> {

    void delete(String key);

    void expire(String key, int t);

    boolean exist(String key);

    int getExpire(String key);

    T getValue(String key);

    void setValue(String key, T value);

    void setValue(String key, T value, int t);

    void incr(String key);

    void decr(String key);

    T randomSet(String key);

    Long addSet(String key, T... value);

    Long deleteSet(String key, T... value);

    Long sizeSet(String key);

    Set<T> membersSet(String key);

    Boolean existSet(String key, String member);

    T rightPop(String key);

    Long leftPush(String key, T value);

    T leftPop(String key);

    Long rightPush(String key, T value);
}
