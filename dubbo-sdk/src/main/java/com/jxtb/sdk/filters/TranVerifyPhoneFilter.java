package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.Filter;
import com.jxtb.sdk.RequestContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成验证码
 * Created by jxtb on 2019/6/19.
 */
public class TranVerifyPhoneFilter extends BaseFilter implements Filter{
    @Override
    public void invoke(String requestParam) {
        JSONObject jsonObject = RequestContext.getJsonObject();
        Map<String, Object> hp = new HashMap<>();
        hp.put("requestId", jsonObject.getJSONObject(HEAD).getString("serviceSn"));
        hp.put("ssyType" ,"");
        hp.put("tranCode", jsonObject.getJSONObject(HEAD).getString("serviceId"));
        hp.put("name", "");
        hp.put("groupNo", jsonObject.getJSONObject(REQUEST).getString("groupNo"));
        hp.put("sendTime", "");


        Map<String, Object> ver = new HashMap<>();
        ver.put("versionId", jsonObject.getJSONObject(HEAD).getString("versionId"));
        hp.put("head", ver);

        RequestContext.removeJsonObject();
        JSONObject temp = JSON.parseObject(JSON.toJSONString(hp));
        temp.put("smsReceiver", jsonObject.getJSONObject(REQUEST).getString("smsReceiver"));
        temp.put("content", jsonObject.getJSONObject(REQUEST).getString("content"));
        temp.getJSONObject("content").put("verifyCode", "");
        RequestContext.setJsonObject(temp);

        getNext().invoke(temp.toJSONString());
    }
}
