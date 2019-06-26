package com.jxtb.sdk.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.support.PropertiesLoaderSupport;

import java.util.Map;
import java.util.Properties;

/**
 * Created by jxtb on 2019/6/25.
 */
public class EnvFactoryBean extends PropertiesLoaderSupport implements FactoryBean<Properties>{

    private Logger logger = LoggerFactory.getLogger(EnvFactoryBean.class);

    public EnvFactoryBean(){

    }

    @Override
    public Properties getObject() throws Exception {
        Properties props = new Properties();
        Map<String, String> env = System.getenv();
        super.loadProperties(props);
        if(StringUtils.isNotEmpty((String)env.get("instanceName"))){
            this.logger.info("发现instanceName环境变量，开始从环境变量取值");
            props.putAll(System.getenv());
        }
        props.putAll(System.getProperties());
        return props;
    }

    @Override
    public Class<?> getObjectType() {
        return Properties.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
