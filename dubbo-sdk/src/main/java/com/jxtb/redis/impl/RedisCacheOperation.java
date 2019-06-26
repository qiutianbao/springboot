package com.jxtb.redis.impl;

import com.jxtb.redis.AbstractCacheOperation;
import com.jxtb.redis.CacheOperation;
import com.jxtb.redis.RedisClusterFactoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * Created by jxtb on 2019/6/12.
 */
//@Service("cacheOperation")
public class RedisCacheOperation extends AbstractCacheOperation implements CacheOperation{
    Logger logger = LoggerFactory.getLogger(RedisCacheOperation.class);

    @Autowired
    private RedisClusterFactoryProvider jedisPool;
    @Value("#{env['redis_prefix']}")
    private String redis_prefix;

    public RedisCacheOperation() {
    }

    public RedisClusterFactoryProvider getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(RedisClusterFactoryProvider jedisPool) {
        this.jedisPool = jedisPool;
    }

    @PostConstruct
    public void init(){
        this.prefix = this.redis_prefix;
    }

    @Override
    protected void doPut(String key, String value) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            jedis.set(key,value);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected void doPut(byte[] key, byte[] value) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            jedis.set(key,value);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected void doPut(byte[] key, byte[] value, int timeout) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            jedis.setex(key, timeout, value);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected void doPut(String key, String value, int seconds) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            jedis.setex(key, seconds, value);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected void doPutAll(String key, List<String> lists) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            jedis.rpush(key,(String[])lists.toArray(new String[0]));
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected String doGet(String key) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            return jedis.get(key);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected byte[] doGet(byte[] key) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            return jedis.get(key);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected List<String> doGetAll(String key) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            return jedis.lrange(key, 0L, -1L);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected void doInvalid(String key) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            jedis.del(key);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected void doDelete(byte[] key) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            jedis.del(key);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected void doExpire(String key, int seconds) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            jedis.expire(key, seconds);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected void doExpire(String key, long timestamp) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            jedis.expireAt(key, timestamp);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected long doIncreByValue(String key, long value) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            return jedis.incrBy(key, value);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected long doIncrByOne(String key) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            return jedis.incrBy(key, Long.parseLong("1"));
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected double doIncrByFloat(String key, double value) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            return jedis.incrByFloat(key, value);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected List<String> doLrange(String key, long start, long end) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            return jedis.lrange(key, start, end);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected Set<byte[]> doKeys(String pattern) {
        Set<byte[]> keys;
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            keys = jedis.hkeys(pattern.getBytes());
            return keys;
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected long doDecrByOne(String key) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            return jedis.decrBy(key, Long.parseLong("1"));
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

    @Override
    protected long doDecrByValue(String key, long value) {
        JedisCluster jedis;
        try{
            jedis = this.jedisPool.getRedis();
            return jedis.decrBy(key, value);
        }catch (Throwable var1){
            this.logger.error("",var1);
            throw new RuntimeException(var1);
        }
    }

}
