package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.Filter;
import com.jxtb.sdk.RequestContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jxtb on 2019/6/19.
 */
public class PhoneCodeFilter extends BaseFilter implements Filter{
    @Override
    public void invoke(String requestParam) {
        JSONObject jsonObject = RequestContext.getJsonObject();
        Map<String, Object> hp = new HashMap<>();
        hp.put("requestId", jsonObject.getJSONObject(HEAD).getString("serviceSn"));
        hp.put("ssyType" ,"");
        hp.put("tranCode", jsonObject.getJSONObject(HEAD).getString("serviceId"));
        hp.put("name", jsonObject.getJSONObject(REQUEST).getString("name"));
        hp.put("groupNo", jsonObject.getJSONObject(REQUEST).getString("groupNo"));
        hp.put("smsReceiver", jsonObject.getJSONObject(REQUEST).getString("smsReceiver"));
        hp.put("sendTime", jsonObject.getJSONObject(REQUEST).getString("sendTime"));
        hp.put("content", jsonObject.getJSONObject(REQUEST).getString("content"));
        getNext().invoke(JSON.toJSONString(hp));
    }
}
