package com.jxtb.redis.service.impl;

import com.jxtb.redis.entity.RedisModel;
import com.jxtb.redis.service.IRedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jxtb on 2019/1/31.
 */
@Service
public class RedisServiceImpl extends IRedisService<RedisModel> {
    private static final String REDIS_KEY = "TEST_REDIS_KEY";

    @Override
    protected String getRedisKey() {
        return this.REDIS_KEY;
    }

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public void setKey(String key, Object value) {
        //更改在redis里面查看key编码问题
        RedisSerializer redisSerializer =new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    public Object getKey(String key) {
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

}
