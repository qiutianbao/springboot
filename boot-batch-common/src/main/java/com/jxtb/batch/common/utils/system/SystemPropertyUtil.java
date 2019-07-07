package com.jxtb.batch.common.utils.system;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.regex.Pattern;

/**
 * Created by jxtb on 2019/7/1.
 */
public class SystemPropertyUtil {

    public static boolean contains(String key) {
        return get(key) != null;
    }

    public static String get(String key){
        return get(key, null);
    }

    public static String get(final String key, String def){
        if(key == null){
            throw new NullPointerException("key");
        }
        if(key.isEmpty()){
            throw new IllegalArgumentException("key must not be empty");
        }
        String value = null;
        try{
            if(System.getSecurityManager() == null){
                value = System.getProperty(key);
            }else{
                value = AccessController.doPrivileged(new PrivilegedAction<String>() {
                    @Override
                    public String run() {
                        return System.getProperty(key);
                    }
                });
            }
        }catch (Exception e){
            ;
        }
        if(value == null){
            return def;
        }
        return value;
    }

    public static boolean getBoolean(String key, boolean def){
        String value = get(key);
        if(value == null){
            return def;
        }
        value = value.trim().toLowerCase();
        if(value.isEmpty()){
            return true;
        }
        if("true".equals(value) || "yes".equals(value) || "1".equals(value)){
            return true;
        }
        if("false".equals(value) || "no".equals(value) || "0".equals(value)){
            return false;
        }
        return def;
    }

    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?[0-9]+");

    public static int getInt(String key, int def){
        String value = get(key);
        if(value == null){
            return def;
        }
        value = value.trim().toLowerCase();
        if(INTEGER_PATTERN.matcher(value).matches()){
            try {
                return Integer.parseInt(value);
            }catch (Exception e){
                ;
            }
        }
        return def;
    }

    public static long getLong(String key, long def){
        String value = get(key);
        if(value == null){
            return def;
        }
        value = value.trim().toLowerCase();
        if(INTEGER_PATTERN.matcher(value).matches()){
            try {
                return Long.parseLong(value);
            }catch (Exception e){
                ;
            }
        }
        return def;
    }

}
