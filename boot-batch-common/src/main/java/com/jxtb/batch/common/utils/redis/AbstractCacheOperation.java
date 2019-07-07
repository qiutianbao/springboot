package com.jxtb.batch.common.utils.redis;

import java.util.List;
import java.util.Set;

/**
 * Created by jxtb on 2019/6/12.
 */
public abstract class AbstractCacheOperation implements CacheOperation{
    protected String prefix;

    public AbstractCacheOperation() {
    }

    @Override
    public void put(String key, String value) {
        this.doPut(this.prefix + key, value);
    }

    protected abstract  void doPut(String key, String value);

    @Override
    public void put(byte[] key, byte[] value) {
        String keys = new String(key);
        this.doPut((this.prefix + keys).getBytes(), value);
    }

    protected abstract  void doPut(byte[] key, byte[] value);

    @Override
    public void put(byte[] key, byte[] value, int timeout) {
        String keys = new String(key);
        this.doPut((this.prefix + keys).getBytes(), value, timeout);
    }

    protected abstract void doPut(byte[] key, byte[] value, int timeout);

    @Override
    public void put(String key, String value, int seconds) {
        this.doPut(this.prefix + key, value, seconds);
    }

    protected abstract void doPut(String key, String value, int seconds);

    @Override
    public void putAll(String key, List<String> lists) {
        this.doPutAll(this.prefix + key, lists);
    }

    protected abstract  void doPutAll(String key, List<String> lists);

    @Override
    public String get(String key) {
        return this.doGet(this.prefix + key);
    }

    protected abstract String doGet(String key);

    @Override
    public byte[] get(byte[] key) {
        String keys = new String(key);
        return this.doGet((this.prefix + keys).getBytes());
    }

    protected abstract byte[] doGet(byte[] key);

    @Override
    public List<String> getAll(String key) {
        return this.doGetAll(this.prefix + key);
    }

    protected abstract List<String> doGetAll(String key);

    @Override
    public void invalid(String key) {
        this.doInvalid(this.prefix + key);
    }

    protected abstract void doInvalid(String key);

    @Override
    public void delete(byte[] key) {
        String keys = new String(key);
        this.doDelete((this.prefix + keys).getBytes());
    }

    protected abstract void doDelete(byte[] key);

    @Override
    public void expire(String key, int seconds) {
        this.doExpire(this.prefix + key, seconds);
    }

    protected abstract void doExpire(String key, int seconds);

    @Override
    public void expire(String key, long timestamp) {
        this.doExpire(this.prefix + key, timestamp);
    }

    protected abstract void doExpire(String key, long timestamp);

    @Override
    public long incrByValue(String key, long value) {
        return this.doIncreByValue(this.prefix + key, value);
    }

    protected abstract long doIncreByValue(String key, long value);

    @Override
    public long incrByOne(String key) {
        return this.doIncrByOne(this.prefix + key);
    }

    protected abstract long doIncrByOne(String key);

    @Override
    public double incrByFloat(String key, double value) {
        return this.doIncrByFloat(this.prefix + key, value);
    }

    protected abstract double doIncrByFloat(String key, double value);

    @Override
    public List<String> lrange(String key, long start, long end) {
        return this.doLrange(this.prefix + key, start, end);
    }

    protected abstract List<String> doLrange(String key, long start, long end);

    @Override
    public Set<byte[]> keys(String pattern) {
        return this.doKeys(this.prefix + pattern);
    }

    protected abstract Set<byte[]> doKeys(String pattern);

    @Override
    public long decrByOne(String key) {
        return this.doDecrByOne(this.prefix + key);
    }

    protected abstract long doDecrByOne(String key);

    @Override
    public long decrByValue(String key, long value) {
        return this.doDecrByValue(this.prefix + key, value);
    }

    protected abstract long doDecrByValue(String key, long value);

}
