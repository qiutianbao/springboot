package com.jxtb.hutool.util;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by jxtb on 2019/8/2.
 */

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static Map<String, String> json2map(String str){
        try{
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.create();
            Map<String, String> map = gson.fromJson(str, new TypeToken<Map<String, String>>() {
            }.getType());
            return map;
        }catch (Exception e){
            logger.error("JsonUtil.json2map();parse {} exception", str, e);
            return null;
        }
    }

    public static String map2json(Map<String, String> map){
        try{
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.create();
            return gson.toJson(map);
        }catch (Exception e){
            logger.error("JsonUtil.map2json();parse {} exception", map, e);
            return null;
        }
    }

    public static Map<String, Object> json2mapObj(String str){
        try{
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.create();
            Map<String, Object> map = gson.fromJson(str, new TypeToken<Map<String, Object>>() {
            }.getType());
            return map;
        }catch (Exception e){
            logger.error("JsonUtil.json2mapObj();parse {} exception", str, e);
            return null;
        }
    }

    public static String mapObj2json(Map<String, Object> map){
        try{
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.create();
            return gson.toJson(map);
        }catch (Exception e){
            logger.error("JsonUtil.mapObj2json();parse {} exception", map, e);
            return null;
        }
    }

    public static String obj2json(Object o){
        String targetStr = "";
        try{
            targetStr = new Gson().toJson(o);
        }catch (Exception e){
            logger.error("JsonUtil.obj2json();parse {} exception", o, e);
        }
        return targetStr;
    }

    public static Object json2obj(String jsonStr, Class clazz){
        try{
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, clazz);
        }catch (Exception e){
            logger.error("JsonUtil.json2obj();parse {} exception", e);
        }
        return null;
    }

    public static void main(String[] args) {
        String str = "{\"goods_id\":\"140861765\",\"cat_id\":\"210\",\"goods_sn\":\"171073501\",\"goods_sn_back\":\"171073501\",\"goods_upc\":null,\"goods_name\":\"Lace-Up Boxer Swimming Trunks\"}";
        System.out.println(str);
        Map<String, String> map = JsonUtil.json2map(str);
        System.out.println(map);
        System.out.println(map.get("cat_id"));
        System.out.println(map.get("hello"));
    }

}
