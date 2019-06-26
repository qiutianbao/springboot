package com.jxtb.redis.utils;

/**
 * Created by jxtb on 2019/6/12.
 */
public class ClassLoaderUtil {
    public static Class<?> loadClass(String fullClassName) {
        Class clazz = null;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            clazz = loader.loadClass(fullClassName);
        }catch (Exception e){
            ;
        }
        if(clazz == null){
            try {
                clazz = Class.forName(fullClassName);
            }catch (ClassNotFoundException e){
                ;
            }
        }
        return clazz;
    }
}
