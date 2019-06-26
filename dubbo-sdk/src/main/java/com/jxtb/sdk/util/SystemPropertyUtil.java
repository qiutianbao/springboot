package com.jxtb.sdk.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.regex.Pattern;

/**
 * Created by jxtb on 2019/6/19.
 */
public class SystemPropertyUtil {
    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?[0-9]+");

    public static String get(String key){
        return get(key,(String)null);
    }
    public static String get(final String key, String def){
        if(key == null){
            throw new NullPointerException("key");
        }else if(key.isEmpty()){
            throw new IllegalArgumentException("key must not be empty.");
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

    public static long getLong(String key, long def){
        String value = get(key);
        if(value == null){
            return def;
        }else{
            value = value.trim().toLowerCase();
            if(INTEGER_PATTERN.matcher(value).matches()){
                try{
                    return Long.parseLong(value);
                }catch (Exception e){
                    ;
                }
            }
        }
        return def;
    }
}
