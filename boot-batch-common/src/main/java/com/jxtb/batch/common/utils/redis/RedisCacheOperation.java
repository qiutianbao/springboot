package com.jxtb.batch.common.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by jxtb on 2019/7/5.
 */
@Service("cacheOperation")
public class RedisCacheOperation extends AbstractCacheOperation implements CacheOperation{

    private Logger logger = LoggerFactory.getLogger(RedisCacheOperation.class);

    @Override
    protected void doPut(String key, String value) {

    }

    @Override
    protected void doPut(byte[] key, byte[] value) {

    }

    @Override
    protected void doPut(byte[] key, byte[] value, int timeout) {

    }

    @Override
    protected void doPut(String key, String value, int seconds) {

    }

    @Override
    protected void doPutAll(String key, List<String> lists) {

    }

    @Override
    protected String doGet(String key) {
        return null;
    }

    @Override
    protected byte[] doGet(byte[] key) {
        return new byte[0];
    }

    @Override
    protected List<String> doGetAll(String key) {
        return null;
    }

    @Override
    protected void doInvalid(String key) {

    }

    @Override
    protected void doDelete(byte[] key) {

    }

    @Override
    protected void doExpire(String key, int seconds) {

    }

    @Override
    protected void doExpire(String key, long timestamp) {

    }

    @Override
    protected long doIncreByValue(String key, long value) {
        return 0;
    }

    @Override
    protected long doIncrByOne(String key) {
        return 0;
    }

    @Override
    protected double doIncrByFloat(String key, double value) {
        return 0;
    }

    @Override
    protected List<String> doLrange(String key, long start, long end) {
        return null;
    }

    @Override
    protected Set<byte[]> doKeys(String pattern) {
        return null;
    }

    @Override
    protected long doDecrByOne(String key) {
        return 0;
    }

    @Override
    protected long doDecrByValue(String key, long value) {
        return 0;
    }
}
