package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.*;
import com.jxtb.sdk.model.ServiceConfigure;
import com.jxtb.sdk.util.IdWorker;
import org.apache.commons.lang.StringUtils;

/**
 * 接口公共头部必填项验证、接口开通验证、产生全局流水
 * Created by jxtb on 2019/6/18.
 */
public class HeadFilter extends BaseFilter implements Filter{
    @Override
    public void invoke(String requestParam) {
        JSONObject jsonObject = RequestContext.getJsonObject();
        String serviceId = jsonObject.getJSONObject(HEAD).getString(SERVICE_ID);
        String channelId = jsonObject.getJSONObject(HEAD).getString(CHANNEL_ID);
        String versionId = jsonObject.getJSONObject(HEAD).getString(VERSION_ID);
        String requestTime = jsonObject.getJSONObject(HEAD).getString(REQUEST_TIME);
        String inputSource = jsonObject.getJSONObject(HEAD).getString(INPUT_SOURCE);

        RequestContext.setChannelId(channelId);
        long sid = IdWorker.getInstance().nextId();
        RequestContext.setSid(String.valueOf(sid));

        ServiceConfigure serviceConfigure = this.getPipeline().getServiceConfigure();
        String bizNo = exactName(serviceConfigure.getClassName() + "_" + serviceConfigure.getMethodName());
        RequestContext.setService(bizNo);

        if(StringUtils.isBlank(serviceId)){
            SendResponseFactory.createSendResponse(RequestContext.getResponse()).
                    sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010002, "交易代码未填写", jsonObject));
        }

        if(StringUtils.isBlank(channelId)){
            SendResponseFactory.createSendResponse(RequestContext.getResponse()).
                    sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010003, "渠道未填写", jsonObject));
        }

        if(StringUtils.isBlank(versionId)){
            SendResponseFactory.createSendResponse(RequestContext.getResponse()).
                    sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010004, "版本号未填写", jsonObject));
        }

        if(StringUtils.isBlank(requestTime)){
            SendResponseFactory.createSendResponse(RequestContext.getResponse()).
                    sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010005, "请求时间未填写", jsonObject));
        }

        if(StringUtils.isBlank(inputSource)){
            SendResponseFactory.createSendResponse(RequestContext.getResponse()).
                    sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010014, "请求来源未填写", jsonObject));
        }

        jsonObject.getJSONObject(HEAD).put("sid", sid);
        jsonObject.getJSONObject(HEAD).put("org", org);

        getNext().invoke(requestParam);
    }

    private String exactName(String className){
        int pos = className.lastIndexOf(".");
        return className.substring(pos + 1);
    }

}
