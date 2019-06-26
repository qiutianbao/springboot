package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSONObject;
//import CacheOperation;
import com.jxtb.sdk.*;

/**
 * Created by jxtb on 2019/6/19.
 */
public class ServiceControllerFilter extends BaseFilter implements Filter {

    private static final String OPEN_FLAG = "Y";
    private static final String ALL_CHANNEL = "00";

    @Override
    public void invoke(String requestParam) {
        JSONObject jsonObject = RequestContext.getJsonObject();
        String serviceId = jsonObject.getJSONObject(HEAD).getString(SERVICE_ID);
        String channelId = jsonObject.getJSONObject(HEAD).getString(CHANNEL_ID);
        String key = org + ":" + serviceId + ":" + channelId;
//        CacheOperation cacheOperation = getCacheOperation();
//        String openFlag = cacheOperation.get(key);
//        if(!OPEN_FLAG.equals(openFlag)){ //未开通
//            String key_1 = org + ":" + serviceId + ":" +ALL_CHANNEL;
//            String allFlag = cacheOperation.get(key_1);
//            if(!OPEN_FLAG.equals(allFlag)){
//                SendResponseFactory.createSendResponse(RequestContext.getResponse()).
//                        sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010015, "交易代码为" + serviceId + "针对渠道" + channelId + "未开通", jsonObject));
//                return;
//            }
//        }
        getNext().invoke(requestParam);
    }
}
