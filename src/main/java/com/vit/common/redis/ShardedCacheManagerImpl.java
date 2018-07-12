package com.vit.common.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vit.common.utils.ListTranscoder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangguoping on 15/4/16.
 */
public class ShardedCacheManagerImpl implements ShardedRedisCacheManager {

    private ShardedJedisPool pool;

    private static final String LOCK_TAG = "lock";

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean lock(String key, Integer ttl){
        key = key + LOCK_TAG;
        String lua = "if redis.call('get',KEYS[1]) then return 1 else redis.call('set', KEYS[1], 'lock') redis.call('expire', KEYS[1], ARGV[1] ) return 0 end";
        ShardedJedis shardedJedis = pool.getResource();
        try {
            Jedis jedis = shardedJedis.getShard(key);
            List<String> keys = new ArrayList<>();
            List<String> argv = new ArrayList<>();
            keys.add(key);
            argv.add(ttl == null ? "30" : ttl + "");
            Object result = jedis.eval(lua, keys, argv);
            if (result instanceof Long) {
                Long resultInt = (Long) result;
                if (resultInt == 0) {
                    return true;
                }
                return false;
            }
            return false;
        }finally {
            if (shardedJedis != null) {
                pool.returnResource(shardedJedis);
            }
        }
    }

    @Override
    public boolean unlock(String key){
        key = key + LOCK_TAG;
        ShardedJedis jedis = pool.getResource();
        try {
            jedis.del(key);
        }finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
        return true;
    }

    @Override
    public boolean hset(String key, String field, String value){
        ShardedJedis jedis = pool.getResource();
        try {
            jedis.hset(key, field, value);
        }finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
        return true;
    }

    @Override
    public boolean hset(String key, String field, String value, int timeout){
        hset(key, field, value);
        ShardedJedis jedis = pool.getResource();
        try {
            jedis.expire(key, timeout);
        }finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
        return true;
    }

    @Override
    public String hget(String key, String field){
        ShardedJedis jedis = pool.getResource();
        String hget = "";
        try {
            hget = jedis.hget(key, field);
        }finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
        return hget;
    }

    @Override
    public Map<String, String> hgetAll(String key){
        ShardedJedis jedis = pool.getResource();
        try {
            Map<String, String> resultMap = new HashMap<>();
            Map<String, String> map = jedis.hgetAll(key);
            if (map == null) {
                return resultMap;
            }
            return map;
        }finally {
            if (jedis != null) {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public <T> T get(String key, Class<T> classType) {
        ShardedJedis jedis = pool.getResource();
        try {
            String value = jedis.get(key);
            if (value != null && !value.equals("")) {
                T t = mapper.readValue(value, classType);
                return t;
            }
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
        return null;
    }

    @Override
    public <T> T get(String key, TypeOf<T> t) {
        ShardedJedis jedis = pool.getResource();
//        System.out.println(t.getClass().getTypeName());
        System.out.println(t.getClass().getName());
        try {
            String value = jedis.get(key);
            TypeReference<T> typeReference = new TypeReference<T>() {
                @Override
                public Type getType() {
                    return super.getType();
                }

                @Override
                public int compareTo(TypeReference<T> typeReference) {
                    return super.compareTo(typeReference);
                }
            };
            if (value != null) {
                return mapper.readValue(value, typeReference);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
        return null;
    }


    @Override
    public String get(String key) {
        ShardedJedis jedis = pool.getResource();
        try {
            String value = jedis.get(key);
            return value;
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
        return null;
    }

    @Override
    public byte[] getBytes(String key) {
        ShardedJedis jedis = pool.getResource();

        try {
            byte[] value = jedis.get(key.getBytes("utf8"));
            return value;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            if (jedis != null)
                pool.returnResource(jedis);
        }
        return new byte[0];
    }

    @Override
    public List getList(String key, Class classType) {
        if (key == null || "".equals(key)) {
            return null;
        }
        ListTranscoder<Class> listTranscoder = new ListTranscoder<Class>();
        byte[] bytes = getBytes(key);
        if (bytes == null) {
            return null;
        }
        return (List) listTranscoder.deserialize(bytes);
    }

    @Override
    public void setList(String key, List list, int seconds, Class classType) {
        ListTranscoder<Class> listTranscoder = new ListTranscoder<Class>();
        set(key, listTranscoder.serialize(list), seconds);
    }

    @Override
    public void set(String key, Object value) {
        ShardedJedis jedis = pool.getResource();
        try {
            this.set_v(key, value, jedis);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void set(String key, Object value, int seconds) {
        ShardedJedis jedis = pool.getResource();
        try {
            this.set_v(key, value, jedis);
            this.set_expire(key, seconds, jedis);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void set(String key, Object value, long unixTime) {
        ShardedJedis jedis = pool.getResource();
        try {
            this.set_v(key, value, jedis);
            this.set_expireAt(key, unixTime, jedis);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void set(String key, String value) {
        ShardedJedis jedis = pool.getResource();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void set(String key, String value, int seconds) {
        ShardedJedis jedis = pool.getResource();
        try {
            jedis.set(key, value);
            this.set_expire(key, seconds, jedis);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void set(String key, String value, long unixTime) {
        ShardedJedis jedis = pool.getResource();
        try {
            jedis.set(key, value);
            this.set_expireAt(key, unixTime, jedis);
        } catch (Exception e) {
            //  e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void set(String key, byte[] data) {
        ShardedJedis jedis = pool.getResource();
        try {
            jedis.set(key.getBytes("utf8"), data);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void set(String key, byte[] data, int seconds) {
        ShardedJedis jedis = pool.getResource();
        try {
            byte[] key_data = key.getBytes("utf8");
            jedis.set(key_data, data);
            jedis.expire(key_data, seconds);
        } catch (Exception e) {
            //  e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void set(String key, byte[] data, long unixTime) {
        ShardedJedis jedis = pool.getResource();
        try {
            byte[] key_data = key.getBytes("utf8");
            jedis.set(key_data, data);
            jedis.expireAt(key_data, unixTime);
        } catch (Exception e) {
            //  e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void delete(String key) {
        ShardedJedis jedis = pool.getResource();
        try {
            jedis.del(key);
        } catch (Exception e) {
            //  e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public void deleteBytes(String key) {
        ShardedJedis jedis = pool.getResource();
        try {
            jedis.del(key.getBytes("utf8"));
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            pool.returnResource(jedis);
        }
    }

    private void set_v(String key, Object value, ShardedJedis jedis) throws JsonProcessingException {
        String value_str = mapper.writeValueAsString(value);
        jedis.set(key, value_str);
    }

    private void set_expire(String key, int seconds, ShardedJedis jedis) {
        jedis.expire(key, seconds);
    }

    private void set_expireAt(String key, long unixTime, ShardedJedis jedis) {
        jedis.expireAt(key, unixTime);
    }

    public ShardedJedisPool getPool() {
        return pool;
    }

    public void setPool(ShardedJedisPool pool) {
        this.pool = pool;
    }

    @Override
    public ShardedJedis getJedis() {
        ShardedJedis jedis = pool.getResource();
        return jedis;
    }

    @Override
    public void returnResource(ShardedJedis jedis) {
        try {
            pool.returnResource(jedis);
        } catch (Exception e) {
            //  e.printStackTrace();
        }
    }
}
