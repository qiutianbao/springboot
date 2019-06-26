package com.jxtb.sdk;

import com.jxtb.redis.utils.SystemPropertyUtils;
import com.jxtb.sdk.core.HttpSendResponse;
import com.jxtb.sdk.core.NettySendResponse;
import com.jxtb.sdk.core.Response;

/**
 * Created by jxtb on 2019/6/14.
 */
public class SendResponseFactory {
    private static final String NETTY = "netty";
    public static SendResponse createSendResponse(Response response){
        String transportType = SystemPropertyUtils.get("transport.type");
        if(NETTY.equals(transportType)){
            return new NettySendResponse(response);
        }
        return new HttpSendResponse(response);
    }
}
