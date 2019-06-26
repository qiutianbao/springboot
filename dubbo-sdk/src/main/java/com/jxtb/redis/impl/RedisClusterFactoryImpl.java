package com.jxtb.redis.impl;

import com.jxtb.redis.RedisClusterFactoryProvider;
import com.jxtb.redis.exception.RedisException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jxtb on 2019/6/12.
 */
public class RedisClusterFactoryImpl implements RedisClusterFactoryProvider, InitializingBean{

    private JedisCluster jedisCluster;
    private Integer timeout;
    private Integer maxRedirections;
    private GenericObjectPoolConfig genericObjectPoolConfig;
    private String addressConfig;

    public RedisClusterFactoryImpl() {
    }

    @Override
    public JedisCluster getRedis() throws Exception {
        return this.getJedisCluster();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort();
        this.setJedisCluster(new JedisCluster(haps,this.timeout,this.maxRedirections,this.genericObjectPoolConfig));
    }

    private Set<HostAndPort> parseHostAndPort() {
        try{
            Set<HostAndPort> haps = new HashSet<>();
            String[] address = this.getAddressConfig().split(",");
            for(int i=0; i < address.length; i++){
                String[] ipAndPort = address[i].split(":");
                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }
            return haps;
        }catch (Exception e){
            e.printStackTrace();
            throw new RedisException("解析 jedis 配置文件失败",e);
        }
    }

    public Class<? extends JedisCluster> getObjectType(){
        return this.getJedisCluster() != null ? this.getJedisCluster().getClass() : JedisCluster.class;
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxRedirections() {
        return maxRedirections;
    }

    public void setMaxRedirections(Integer maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public String getAddressConfig() {
        return addressConfig;
    }

    public void setAddressConfig(String addressConfig) {
        this.addressConfig = addressConfig;
    }
}
