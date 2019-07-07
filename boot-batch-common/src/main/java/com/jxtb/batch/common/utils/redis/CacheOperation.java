package com.jxtb.batch.common.utils.redis;

import java.util.List;
import java.util.Set;

/**
 * Created by jxtb on 2019/6/12.
 */
public interface CacheOperation {
    /**
     * 加入缓存
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * 加入缓存
     * @param key
     * @param value
     */
    void put(byte[] key, byte[] value);

    /**
     * 加入缓存
     * @param key
     * @param value
     * @param timeout
     */
    void put(byte[] key, byte[] value, int timeout);

    /**
     * 加入缓存
     * @param key
     * @param value
     * @param seconds seconds秒之后失效
     */
    void put(String key, String value, int seconds);
    void putAll(String key, List<String> value);

    /**
     * 从缓存中获取信息
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 从缓存中获取信息
     * @param key
     * @return
     */
    byte[] get(byte[] key);
    List<String> getAll(String key);

    /**
     * 缓存失效
     * @param key
     */
    void invalid(String key);

    /**
     * 删除缓存
     * @param key
     */
    void delete(byte[] key);

    /**
     * 缓存在seconds秒后失效
     * @param key
     * @param seconds
     */
    void expire(String key, int seconds);

    /**
     * 缓存在未来某个时间点失效
     * @param key
     * @param timestamp
     */
    void expire(String key, long timestamp);

    /**原子性的薪资指定值
     *
     * @param key
     * @param value
     * @return
     */
    long incrByValue(String key, long value);

    /**
     * 原子性的新增
     * @param key
     * @return
     */
    long incrByOne(String key);

    /**
     * 原子性的新增指定浮点值
     * @param key
     * @param value
     * @return
     */
    double incrByFloat(String key, double value);

    /**
     * 返回列表中的内容
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<String> lrange(String key, long start, long end);

    /**
     * 获取所以符合匹配条件的缓存
     * @param pattern
     * @return
     */
    Set<byte[]> keys(String pattern);

    /**
     * 原子性的减一
     * @param key
     * @return
     */
    long decrByOne(String key);

    /**
     * 原子性的减vaule
     * @param key
     * @param value
     * @return
     */
    long decrByValue(String key, long value);

}
