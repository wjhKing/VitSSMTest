package com.vit.common.cache;

import java.util.List;

/**
 * Created by huangguoping on 15/4/15.
 */
public interface CacheManager {
    public <T>T get(String key, Class<T> classType);
    public <T> T get(String key, TypeOf<T> t);
    public String get(String key);
    public byte[] getBytes(String key);
    public List getList(String key, Class classType);

    public void setList(String key, List list, int seconds, Class classType);
    public void set(String key, Object value);
    public void set(String key, Object value, int seconds);
    public void set(String key, Object value, long unixTime);
    public void set(String key, String value);
    public void set(String key, String value, int seconds);
    public void set(String key, String value, long unixTime);
    public void set(String key, byte[] data);
    public void set(String key, byte[] data, int seconds);
    public void set(String key, byte[] data, long unixTime);
    public void delete(String key);
    public void deleteBytes(String key);


    public class TypeOf<T> {
    }
}
