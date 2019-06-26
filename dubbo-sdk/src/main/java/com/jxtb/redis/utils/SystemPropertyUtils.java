package com.jxtb.redis.utils;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by jxtb on 2019/6/12.
 */
public class SystemPropertyUtils {
    public static String get(String key) {
        return get(key, (String)null);
    }

    public static String get(final String key, String def){
        if(key == null){
            throw new NullPointerException("key");
        }else if(key.isEmpty()){
            throw new IllegalArgumentException("key must not be empty");
        }else{
            String value = null;
            try{
                if(System.getSecurityManager() == null){
                    value = System.getProperty(key);
                }else{
                    value = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
                        public String run(){
                            return System.getProperty(key);
                        }
                    });
                }
            }catch (Exception e){
                ;
            }
            return value == null ? def : value;
        }
    }

}
