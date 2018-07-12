package com.vit.common.redis.cluster;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vit.common.utils.ListTranscoder;
import redis.clients.jedis.JedisCluster;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by
 */
public class CacheManagerImpl implements RedisCacheManager {

    private JedisCluster pool;

    private static final String LOCK_TAG = "lock";

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean lock(String key, Integer ttl){
        key = key + LOCK_TAG;
        String lua = "if redis.call('get',KEYS[1]) then return 1 else redis.call('set', KEYS[1], 'lock') redis.call('expire', KEYS[1], ARGV[1] ) return 0 end";
        //JedisCluster JedisCluster = pool.getClusterNodes();
        try {
            //Jedis pool = JedisCluster.getShard(key);
            List<String> keys = new ArrayList<>();
            List<String> argv = new ArrayList<>();
            keys.add(key);
            argv.add(ttl == null ? "30" : ttl + "");
            Object result = pool.eval(lua, keys, argv);
            if (result instanceof Long) {
                Long resultInt = (Long) result;
                if (resultInt == 0) {
                    return true;
                }
                return false;
            }
            return false;
        }finally {
            
        }
    }

    @Override
    public boolean unlock(String key){
        key = key + LOCK_TAG;
        try {
            pool.del(key);
        }finally {
            
        }
        return true;
    }

    @Override
    public boolean hset(String key, String field, String value){
        try {
            pool.hset(key, field, value);
        }finally {
            
        }
        return true;
    }

    @Override
    public boolean hset(String key, String field, String value, int timeout){
        try {
            pool.hset(key, field, value);
            pool.expire(key, timeout);
        }finally {
            
        }
        return true;
    }

    @Override
    public String hget(String key, String field){
        String hget = "";
        try {
            hget = pool.hget(key, field);
        }finally {
            
        }
        return hget;
    }

    @Override
    public Map<String, String> hgetAll(String key){
        try {
            Map<String, String> resultMap = new HashMap<>();
            Map<String, String> map = pool.hgetAll(key);
            if (map == null) {
                return resultMap;
            }
            return map;
        }finally {
            
        }
    }

    @Override
    public <T> T get(String key, Class<T> classType) {
        try {
            String value = pool.get(key);
            if (value != null && !value.equals("")) {
                T t = mapper.readValue(value, classType);
                return t;
            }
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            
        }
        return null;
    }

    @Override
    public <T> T get(String key, TypeOf<T> t) {
        System.out.println(t.getClass().getName());
        try {
            String value = pool.get(key);
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
            
        }
        return null;
    }


    @Override
    public String get(String key) {
        try {
            String value = pool.get(key);
            return value;
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            
        }
        return null;
    }

    @Override
    public byte[] getBytes(String key) {

        try {
            byte[] value = pool.get(key.getBytes("utf8"));
            return value;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            
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
        try {
            this.set_v(key, value, pool);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            
        }
    }

    @Override
    public void set(String key, Object value, int seconds) {
        try {
            this.set_v(key, value, pool);
            this.set_expire(key, seconds, pool);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            
        }
    }

    @Override
    public void set(String key, Object value, long unixTime) {
        try {
            this.set_v(key, value, pool);
            this.set_expireAt(key, unixTime, pool);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }
    }

    @Override
    public void set(String key, String value) {
        try {
            pool.set(key, value);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            
        }
    }

    @Override
    public void set(String key, String value, int seconds) {
        try {
            pool.set(key, value);
            this.set_expire(key, seconds, pool);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            
        }
    }

    @Override
    public void set(String key, String value, long unixTime) {
        try {
            pool.set(key, value);
            this.set_expireAt(key, unixTime, pool);
        } catch (Exception e) {
            //  e.printStackTrace();
        } finally {
            
        }
    }

    @Override
    public void set(String key, byte[] data) {
        try {
            pool.set(key.getBytes("utf8"), data);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            
        }
    }

    @Override
    public void set(String key, byte[] data, int seconds) {
        try {
            byte[] key_data = key.getBytes("utf8");
            pool.set(key_data, data);
            pool.expire(key_data, seconds);
        } catch (Exception e) {
            //  e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void set(String key, byte[] data, long unixTime) {
        try {
            byte[] key_data = key.getBytes("utf8");
            pool.set(key_data, data);
            pool.expireAt(key_data, unixTime);
        } catch (Exception e) {
            //  e.printStackTrace();
        } finally {
            
        }
    }

    @Override
    public void delete(String key) {
        try {
            pool.del(key);
        } catch (Exception e) {
            //  e.printStackTrace();
        } finally {
            
        }
    }

    @Override
    public void deleteBytes(String key) {
        try {
            pool.del(key.getBytes("utf8"));
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            
        }
    }

    private void set_v(String key, Object value, JedisCluster pool) throws JsonProcessingException {
        String value_str = mapper.writeValueAsString(value);
        pool.set(key, value_str);
    }

    private void set_expire(String key, int seconds, JedisCluster pool) {
        pool.expire(key, seconds);
    }

    private void set_expireAt(String key, long unixTime, JedisCluster pool) {
        pool.expireAt(key, unixTime);
    }

    public JedisCluster getPool() {
        return pool;
    }

    public void setPool(JedisCluster pool) {
        this.pool = pool;
    }
}
