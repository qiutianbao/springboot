package com.jxtb.sdk.core;

/**
 * Created by jxtb on 2019/6/13.
 */
public class Request {

    private Object requestInstance;

    public Request() {
    }

    public Request(Object requestInstance) {
        this.requestInstance = requestInstance;
    }

    public Object getRequestInstance() {
        return requestInstance;
    }

    public void setRequestInstance(Object requestInstance) {
        this.requestInstance = requestInstance;
    }

}
