package com.jxtb.sdk.core;

import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.RequestContext;
import com.jxtb.sdk.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jxtb on 2019/6/14.
 */
public class HttpSendResponse implements SendResponse{
    private static final Logger logger = LoggerFactory.getLogger(HttpSendResponse.class);
    private HttpServletResponse response;
    
    public HttpSendResponse(Response response){
        this.response = (HttpServletResponse) response.getRequestInstance();
    }
    
    @Override
    public void sendMessage(String jsonResponse) {
        try{
            doSendMessage(jsonResponse, true);
        }catch (Exception e){
            ;
        }
    }

    private void doSendMessage(String jsonResponse, boolean isLog) throws IOException{
        if(isLog){
            logger.info("http response: " + jsonResponse);
        }else{
            String logResponse = dealCfcaResp(jsonResponse);
            logger.info("http response: " + logResponse);
        }
        byte[] data = jsonResponse.getBytes("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setContentLength(data.length);
        OutputStream out = response.getOutputStream();
        out.write(data);
        out.flush();
    }

    /**
     * 处理电子签章文件出参
     * @param jsonResponse
     * @return
     */
    private String dealCfcaResp(String jsonResponse) {
        JSONObject json;
        JSONObject resp;
        try{
            json = JSONObject.parseObject(jsonResponse);
            resp = (JSONObject) json.get("response");
        }catch (Exception e){
            return jsonResponse;
        }
        if(resp != null){
            String fileByte = resp.getString("fileByte");
            if(StringUtils.isNotBlank(fileByte)){
                resp.put("fileByte","CFCA日志打印省略文件串");
            }
        }
        return json.toJSONString();
    }

    @Override
    public void sendError(String errorResponse) {
        try{
            doSendMessage(errorResponse,true);
        }catch (IOException e){
            ;
        }
    }

    @Override
    public void sendECfcaMessage(String jsonResponse) {
        try{
            doSendMessage(jsonResponse,false);
        }catch (IOException e){
            ;
        }
    }
}
