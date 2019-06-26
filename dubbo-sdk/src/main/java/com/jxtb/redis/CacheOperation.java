package com.jxtb.redis;

import java.util.List;
import java.util.Set;

/**
 * Created by jxtb on 2019/6/12.
 */
public interface CacheOperation {
    void put(String key, String value);
    void put(byte[] key, byte[] value);
    void put(byte[] key, byte[] value, int timeout);
    void put(String key, String value, int seconds);
    void putAll(String key, List<String> value);
    String get(String key);
    byte[] get(byte[] key);
    List<String> getAll(String key);
    void invalid(String key);
    void delete(byte[] key);
    void expire(String key,int seconds);
    void expire(String key,long timestamp);
    long incrByValue(String key, long value);
    long incrByOne(String key);
    double incrByFloat(String key, double value);
    List<String> lrange(String key, long start, long end);
    Set<byte[]> keys(String pattern);
    long decrByOne(String key);
    long decrByValue(String key, long value);
}
