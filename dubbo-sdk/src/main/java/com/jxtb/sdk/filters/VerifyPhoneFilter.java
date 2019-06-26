package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.Filter;
import com.jxtb.sdk.RequestContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证验证码
 * Created by jxtb on 2019/6/19.
 */
public class VerifyPhoneFilter extends BaseFilter implements Filter{
    @Override
    public void invoke(String requestParam) {
        JSONObject jsonObject = RequestContext.getJsonObject();
        Map<String, Object> hp = new HashMap<>();
        hp.put("requestId", jsonObject.getJSONObject(HEAD).getString("serviceSn"));
        hp.put("ssyType" ,"");
        hp.put("tranCode", jsonObject.getJSONObject(HEAD).getString("serviceId"));
        hp.put("phoneNo", jsonObject.getJSONObject(REQUEST).getString("phoneNo"));
        hp.put("verifyCode", jsonObject.getJSONObject(REQUEST).getString("verifyCode"));

        Map<String, Object> ver = new HashMap<>();
        ver.put("versionId", jsonObject.getJSONObject(HEAD).getString("versionId"));
        hp.put("head", ver);

        RequestContext.removeJsonObject();
        RequestContext.setJsonObject(JSON.parseObject(JSON.toJSONString(hp)));
        getNext().invoke(JSON.toJSONString(hp));
    }
}
