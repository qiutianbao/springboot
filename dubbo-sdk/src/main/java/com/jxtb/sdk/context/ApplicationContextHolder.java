package com.jxtb.sdk.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.springframework.util.Assert.notNull;

/**
 * Created by jxtb on 2019/6/13.
 */
public class ApplicationContextHolder implements ApplicationContextAware,InitializingBean,DisposableBean{
    private static Logger logger = LoggerFactory.getLogger(ApplicationContextHolder.class);
    private static ApplicationContext applicationContext = null;

    /**
     * 取得存储在静态变量中的ApplicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        assertContextInjected();
        return applicationContext;
    }

    /**
     * 检查ApplicationContext不为空
     */
    private static void assertContextInjected() {
        if(applicationContext == null){
            applicationContext = new ClassPathXmlApplicationContext("spring/spring-context.xml");
        }
        notNull(applicationContext,"应用上下分组件未初始化");
    }

    /**
     * 实现ApplicationContextAware接口，注入Context到静态变量中
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(ApplicationContextHolder.applicationContext != null){
            logger.info("应用上下文被覆盖，原有应用上下文：" + ApplicationContextHolder.applicationContext);
        }
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        ApplicationContextHolder.clearHolder();
    }

    /**
     * 清除ContextHolder中的ApplicationContext为Null
     */
    private static void clearHolder() {
        logger.debug("清除应用上下文：" + applicationContext);
        applicationContext = null;
    }

    /**
     * 从静态变量applicationContext中取得Bean，自动转型为所赋值对象的类型
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name){
        assertContextInjected();
        return (T)applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean，自动转型为所赋值对象的类型
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requiredType){
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    public static void setContext(ApplicationContext context){
        applicationContext = context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(">>>>>>" + name);
        }
        System.out.println("------\nBean 总计:" + applicationContext.getBeanDefinitionCount());
    }
}
