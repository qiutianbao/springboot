package com.jxtb.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jxtb on 2019/6/14.
 */
public class MessageUtil {

    public static String buildNoBodyMessage(String errCode, String errMsg, JSONObject jsonObject){
        Map<String,Object> response = new HashMap<>();
        Map<String,Object> head = new HashMap<>();
        Map<String,Object> body = new HashMap<>();

        head.put("code", errCode);
        head.put("desc", errMsg);
        head.put("channelId", jsonObject.getJSONObject("head").getString("channelId"));
        head.put("serviceSn", jsonObject.getJSONObject("head").getString("serviceSn"));
        head.put("serviceTime", jsonObject.getJSONObject("head").getString("serviceTime"));

        response.put("head", head);
        response.put("response" ,body);

        return JSON.toJSONString(response);
    }

    public static String buildNoBodyNoHeadMessage(String errCode, String errMsg){
        Map<String,Object> response = new HashMap<>();
        Map<String,Object> head = new HashMap<>();
        Map<String,Object> body = new HashMap<>();

        head.put("code", errCode);
        head.put("desc", errMsg);

        response.put("head", head);
        response.put("response" ,body);

        return JSON.toJSONString(response);
    }

}
