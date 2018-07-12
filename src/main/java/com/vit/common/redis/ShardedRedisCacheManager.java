package com.vit.common.redis;

import com.vit.common.cache.CacheManager;
import redis.clients.jedis.ShardedJedis;

import java.util.Map;

/**
 * Created by huangguoping on 15/4/28.
 */
public interface ShardedRedisCacheManager extends CacheManager {
    public ShardedJedis getJedis();
    public void returnResource(ShardedJedis jedis);

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
