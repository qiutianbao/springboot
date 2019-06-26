package com.jxtb.dubbo.req;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by jxtb on 2019/6/13.
 */
@Data
public abstract class AbstractReq implements Serializable{
    /**
     * 交易服务码
     */
    private String serviceId;
    /**
     * 请求服务码
     */
    private String requestId;

    public AbstractReq() {
    }

    public AbstractReq(String serviceId, String requestId) {
        this.serviceId = serviceId;
        this.requestId = requestId;
    }
}
