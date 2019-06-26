package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSONObject;
import com.jxtb.dubbo.LookupCustomIdService;
import com.jxtb.dubbo.req.CustomMappingReq;
//import CacheOperation;
//import CacheOperationFactory;
import com.jxtb.sdk.Filter;
import com.jxtb.sdk.context.ApplicationContextHolder;
import com.jxtb.sdk.core.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

/**
 * 过滤器基本类
 * Created by jxtb on 2019/6/12.
 */
public abstract class BaseFilter implements Filter{

    @Value("#{env['org']}")
    protected String org = "00000000001";

    protected static final String SERVICE_ID = "serviceId"; //服务编码
    protected static final String CHANNEL_ID = "channelId"; //技术渠道
    protected static final String VERSION_ID = "versionId"; //服务版本
    protected static final String REQUEST_TIME = "requestTime"; //请求时间
    protected static final String INPUT_SOURCE = "inputSource"; //请求来源
    protected static final String CUST_ID_FIELD = "custId"; //客户id
    protected static final String HEAD = "head"; //请求头
    protected static final String REQUEST = "request"; //请求体

    protected Filter next;
    private Pipeline pipeline;
    private static final String LOOKUP_CUSTOM = "lookupCustomIdService";
    private static final Logger logger = LoggerFactory.getLogger(BaseFilter.class);

    protected String getCustId(String propertyType,String propertyName,JSONObject jsonObject){
        String property = jsonObject.getJSONObject("request").getString(propertyName);
        if(StringUtils.isEmpty(property)){
            logger.error("key_value is empty {}",jsonObject.toJSONString());
            return null; //增加空判断
        }
//        CacheOperation cacheOperation = getCacheOperation();
//        long st = System.currentTimeMillis();
//        String redisKey = propertyType + ":" + property;
//        String custId = cacheOperation.get("new:" + redisKey);
//        logger.debug("走缓存中取custId【{}】", custId);
//        if(StringUtils.isEmpty(custId)){
//            LookupCustomIdService service = ApplicationContextHolder.getBean(LOOKUP_CUSTOM);
//            CustomMappingReq req = new CustomMappingReq();
//            req.setOrg(jsonObject.getJSONObject("head").getString("org"));
//            req.setIdType(propertyType);
//            req.setIdNo(property);
//            custId = service.lookupCustomId(req);
//        }
//        logger.error("cust_look={}", (System.currentTimeMillis() - st));
        return property;
    }

    @Override
    public Filter getNext() {
        return next;
    }

    @Override
    public void setNext(Filter filter) {
        this.next = filter;
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    @Override
    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

//    public CacheOperation getCacheOperation() {
//        return CacheOperationFactory.newCacheOperation(ApplicationContextHolder.getApplicationContext());
//    }

}
