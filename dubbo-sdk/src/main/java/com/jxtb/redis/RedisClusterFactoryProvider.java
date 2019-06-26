package com.jxtb.redis;

import redis.clients.jedis.JedisCluster;

/**
 * Created by jxtb on 2019/6/12.
 */
public interface RedisClusterFactoryProvider {
    JedisCluster getRedis() throws Exception;
}
