package com.jxtb.sdk.core;

/**
 * Created by jxtb on 2019/6/13.
 */
public class Response {

    private Object requestInstance;

    public Response() {
    }

    public Response(Object requestInstance) {
        this.requestInstance = requestInstance;
    }

    public Object getRequestInstance() {
        return requestInstance;
    }

    public void setRequestInstance(Object requestInstance) {
        this.requestInstance = requestInstance;
    }

}
