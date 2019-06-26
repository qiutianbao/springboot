package com.jxtb.sdk.util;

import java.lang.reflect.Method;

/**
 * Created by jxtb on 2019/6/19.
 */
public class ReflectUtil {

    public ReflectUtil(){

    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?> type){
        Method method;
        try{
            method = clazz.getMethod(methodName, type);
        }catch (Exception e){
            method = null;
        }
        return method;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] type){
        Method method;
        try{
            method = clazz.getMethod(methodName, type);
        }catch (Exception e){
            method = null;
        }
        return method;
    }

    public static Method getMethod(Class<?> clazz, String methodName){
        Method method;
        try{
            method = clazz.getMethod(methodName);
        }catch (Exception e){
            method = null;
        }
        return method;
    }

}
