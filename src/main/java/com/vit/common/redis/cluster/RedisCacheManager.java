package com.vit.common.redis.cluster;


import com.vit.common.cache.CacheManager;

import java.util.Map;

/**
 * Created by huangguoping on 15/4/28.
 */
public interface RedisCacheManager extends CacheManager {

    public boolean lock(String key, Integer ttl);

    public boolean unlock(String key);

    /**
     * 只允许value存储对象
     */
    public boolean hset(String key, String field, String value);

    /**
     * 只允许value存储对象
     */
    public boolean hset(String key, String field, String value, int timeout);
    /**
     * 只允许value存储对象
     */
    public String hget(String key, String field);
    /**
     * 只允许value存储对象
     */
    public Map<String, String> hgetAll(String key);
}
