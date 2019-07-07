package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.*;
import com.jxtb.sdk.context.ApplicationContextHolder;
import com.jxtb.sdk.core.Pipeline;
import com.jxtb.sdk.model.ServiceConfigure;
import com.jxtb.sdk.util.ClassLoaderUtil;
import com.jxtb.sdk.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 尾部过滤器
 * Created by jxtb on 2019/6/19.
 */
public class TailFilter extends BaseFilter implements Filter{

    private static final Logger logger = LoggerFactory.getLogger(TailFilter.class);
    private Map<String, Class<?>> clazzMapping = new HashMap<>();
    private static Map<String, Method> methodMapping = new HashMap<>();

    @Override
    public void invoke(String requestParam) {
        Pipeline pipeline = this.getPipeline();
        ServiceConfigure serviceConfigure = pipeline.getServiceConfigure();
        JSONObject jsonObject = RequestContext.getJsonObject();
        String serviceName = serviceConfigure.getClassName();
        String methodName = serviceConfigure.getMethodName();
        Class<?> clazz = clazzMapping.get(serviceName);
        if(clazz == null){
            clazz = ClassLoaderUtil.loadClass(serviceName);
            clazzMapping.put(serviceName, clazz);
        }
        if(clazz == null){
            SendResponseFactory.createSendResponse(RequestContext.getResponse()).
                    sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010008, serviceName + "该服务未在网关部署", jsonObject));
            return;
        }
        Method method = methodMapping.get(methodName);
        if(method == null){
            method = ReflectUtil.getMethod(clazz, methodName, new Class[]{String.class});
            methodMapping.put(methodName, method);
        }
        if(method == null){
            SendResponseFactory.createSendResponse(RequestContext.getResponse()).
                    sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010007, serviceName + "该服务的该方法在网关配置的不正确", jsonObject));
            return;
        }
        String beanName = extractBeanName(clazz, jsonObject.getJSONObject(HEAD).getString("versionId"));
        Object service = ApplicationContextHolder.getBean(beanName);
        try{
            long st = System.currentTimeMillis();
            String response = (String)method.invoke(service, jsonObject.toJSONString());
            logger.error("invoke={}", (System.currentTimeMillis() - st));
            SendResponseFactory.createSendResponse(RequestContext.getResponse()).sendMessage(response);
        }catch (Throwable e){
            e.printStackTrace();
            logger.error("调用服务发生错误", e);
            SendResponseFactory.createSendResponse(RequestContext.getResponse()).
                    sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010008, serviceName + "." + methodName + "该服务的方法调用失败", jsonObject));
            return;
        }finally {
            RequestContext.removeChannelId();
            RequestContext.removeService();
            RequestContext.removeSid();
        }
    }

    private String extractBeanName(Class<?> clazz, String version){
        String clazzName = clazz.getSimpleName();
        String firstChar = clazzName.substring(0, 1).toLowerCase();
        String end = clazzName.substring(1);
        version = version.replaceAll("\\.", "_");
        //return firstChar + end + "_" + version;
        return firstChar + end;
    }

}
