package com.jxtb.sdk;

import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.core.Request;
import com.jxtb.sdk.core.Response;

/**
 * 请求线程变量
 * Created by jxtb on 2019/6/13.
 */
public final class RequestContext {

    private static final ThreadLocal<Request> request = new ThreadLocal<>(); //请求对象
    private static final ThreadLocal<Response> response = new ThreadLocal<>(); //响应对象
    private static final ThreadLocal<JSONObject> jsonObject = new ThreadLocal<>();
    private static final ThreadLocal<Object> paramObject = new ThreadLocal<>();
    private static final ThreadLocal<String> sidObject = new ThreadLocal<>();
    private static final ThreadLocal<String> channelObject = new ThreadLocal<>();
    private static final ThreadLocal<String> serviceObject = new ThreadLocal<>();

    public RequestContext() {
        super();
    }

    public static void setRequest(Request req){
        request.set(req);
    }

    public static Object getRequest(){
        return request.get();
    }

    public static void removeRequest(){
        request.remove();
    }

    public static void setResponse(Response res){
        response.set(res);
    }

    public static Response getResponse(){
        return response.get();
    }

    public static void removeResponse(){
        response.remove();
    }

    public static void setJsonObject(JSONObject json){
        jsonObject.set(json);
    }

    public static JSONObject getJsonObject(){
        return jsonObject.get();
    }

    public static void removeJsonObject(){
        jsonObject.remove();
    }

    public static void setParamObject(Object object){
        paramObject.set(object);
    }

    public static Object getParamObject(){
       return paramObject.get();
    }


    public static void removeParamObject(){
        paramObject.remove();
    }

    public static void setSid(String sid){
        sidObject.set(sid);
    }

    public static String getSid(){
       return sidObject.get();
    }

    public static void removeSid(){
        sidObject.remove();
    }

    public static void setChannelId(String channelId){
        channelObject.set(channelId);
    }

    public static String getChannelId(){
        return channelObject.get();
    }

    public static void removeChannelId(){
        channelObject.remove();
    }

    public static void setService(String service){
        serviceObject.set(service);
    }

    public static String getService(){
        return serviceObject.get();
    }

    public static void removeService(){
        serviceObject.remove();
    }

}
