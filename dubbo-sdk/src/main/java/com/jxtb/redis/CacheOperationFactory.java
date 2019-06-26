package com.jxtb.redis;


import com.jxtb.redis.exception.RedisException;
import com.jxtb.redis.impl.RedisCacheOperation;
import com.jxtb.redis.impl.RedisClusterFactoryImpl;
import com.jxtb.redis.utils.ClassLoaderUtil;
import com.jxtb.redis.utils.SystemPropertyUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jxtb on 2019/6/12.
 */
public class CacheOperationFactory {
    private static final String CACHE_CLASS = "redis_cache";
    private static final String REDIS_CLASS = "RedisCacheOperation";
    private static CacheOperation instance = doNewCacheOperation();

    public CacheOperationFactory() {
    }

    public static CacheOperation newCacheOperation(ApplicationContext application){
        String className = SystemPropertyUtils.get("redis_cache");
        if(StringUtils.isEmpty(className)){
            className = "RedisCacheOperation";
        }
        Class<?> clazz = ClassLoaderUtil.loadClass(className);
        if(clazz == null){
            throw new RedisException("110008","缓存初始化异常");
        }else{
           try{
               CacheOperation cacheOperation = (CacheOperation)application.getBean(clazz);
               return cacheOperation;
           }catch (Exception e){
               throw new RedisException("110008","缓存初始化异常");
           }
        }
    }

    public static CacheOperation newCacheOperation(){
        return instance;
    }

    private static CacheOperation doNewCacheOperation() {
        String className = SystemPropertyUtils.get("redis_cache");
        if(StringUtils.isEmpty(className)){
            className = "RedisCacheOperation";
        }
        Class<?> clazz = ClassLoaderUtil.loadClass(className);
        if(clazz == null){
            throw new RedisException("110008","缓存初始化异常");
        }else{
            RedisCacheOperation redisCacheOperation = new RedisCacheOperation();
            redisCacheOperation.prefix = SystemPropertyUtils.get("redis_prefix");
            redisCacheOperation.setJedisPool(buildShardedJedisPool());
            return redisCacheOperation;
        }
    }

    private static RedisClusterFactoryProvider buildShardedJedisPool() {
        String ips = makeAddressConfig();
        RedisClusterFactoryImpl redis = new RedisClusterFactoryImpl();
        redis.setAddressConfig(ips);
        redis.setMaxRedirections(3);
        redis.setTimeout(300000);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxWaitMillis(-1L);
        poolConfig.setMaxTotal(1000);
        poolConfig.setMinIdle(50);
        poolConfig.setMaxIdle(500);
        redis.setGenericObjectPoolConfig(poolConfig);
        Set<HostAndPort> haps = parseHostAndPort(ips);
        redis.setJedisCluster(new JedisCluster(haps,300000,3,poolConfig));
        return redis;
    }

    private static Set<HostAndPort> parseHostAndPort(String addressConfig) {
        try{
            Set<HostAndPort> haps = new HashSet<>();
            String[] address = addressConfig.split(",");
            for(int i=0 ;i < address.length; i++){
                String[] ipAndPort = address[i].split(":");
                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }
            return haps;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("解析 redis 配置文件失败",e);
        }
    }

    private static String makeAddressConfig() {
        return SystemPropertyUtils.get("redis_config");
    }

}
