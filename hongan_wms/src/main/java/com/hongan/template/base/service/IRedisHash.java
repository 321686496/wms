package com.hongan.template.base.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRedisHash<K, V> {

    void delete(String key);

    void expire(String key, int t);

    boolean exist(String key);

    int getExpire(String key);

    V getKV(String h, K k);

    void setKV(String h, K k, V v);

    Map<K, V> getKVAll(String h);

    void setKVAll(String h, Map<K, V> map);

    Set<K> keys(String h);

    List<V> values(String h, Collection<K> keys);

    Map<K, V> keys(String h, Collection<K> keys);
}
