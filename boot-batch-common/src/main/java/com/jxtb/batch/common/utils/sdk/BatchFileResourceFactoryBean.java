package com.jxtb.batch.common.utils.sdk;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 批量资源Bean工程
 * Created by jxtb on 2019/7/3.
 */
public class BatchFileResourceFactoryBean implements FactoryBean<YakResource>, InitializingBean{

    private final static Logger logger = LoggerFactory.getLogger(BatchFileResourceFactoryBean.class);

    //特殊符号/,key值中绝对不允许出现
    private static final String PLACESLASH = "/";
    //特殊符号\.key值中绝对不允许
    private static final String PLACEBACKLASH = "\\";
    //日期占位符（正则表达式）
    private static final String DATE_PLACEHOLDER = "\\$yyyymmdd";
    private Map<String, String> mappingPlace = new HashMap<>();
    //含有占位符文件路径
    private String filePath;
    //默认批量日期
    @Value("#{jobParameters['date']}")
    private String resourceDate;
    //是否需要做日期转换,默认为否
    private boolean needReplace = false;

    /**
     * 目的：根据注入的filePath经过封装日期最终生成的资源Bean
     * 承诺：配置文件中的filePath必须为file：协议开始
     * @return
     * @throws Exception
     */
    @Override
    public YakResource getObject() throws Exception {
        logger.info("resourceDate:" + resourceDate);
        YakResource resource = new YakResource(getRealUrl(resourceDate), resourceDate);
        logger.info(resource.toString() + "," + toString());
        return resource;
    }

    /**
     * 根据文件路径生成真实的url
     * @param resourceDate
     * @return
     */
    private File getRealUrl(String resourceDate) {
        String realUrl;
        if(needReplace){
            realUrl = filePath.replaceAll(DATE_PLACEHOLDER, resourceDate);
            for(String key : mappingPlace.keySet()){
                realUrl = StringUtils.replace(realUrl, key, mappingPlace.get(key));
            }
        }else{
            realUrl = filePath;
        }
        return new File(realUrl);
    }

    @Override
    public Class<?> getObjectType() {
        return YakResource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for(String key : mappingPlace.keySet()){
            if(-1 != key.indexOf(PLACESLASH) || -1 != key.indexOf(PLACEBACKLASH)){
                throw new IllegalArgumentException("资源初始化时提供的占位符映射key值不合法");
            }
        }
    }

    @Override
    public String toString() {
        return "当前Bean配置[mappingPlace=" + mappingPlace.toString() + ",filePath= " + filePath + "]";
    }

    public Map<String, String> getMappingPlace() {
        return mappingPlace;
    }

    public void setMappingPlace(Map<String, String> mappingPlace) {
        this.mappingPlace = mappingPlace;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getResourceDate() {
        return resourceDate;
    }

    public void setResourceDate(String resourceDate) {
        this.resourceDate = resourceDate;
    }

    public boolean isNeedReplace() {
        return needReplace;
    }

    public void setNeedReplace(boolean needReplace) {
        this.needReplace = needReplace;
    }
}
