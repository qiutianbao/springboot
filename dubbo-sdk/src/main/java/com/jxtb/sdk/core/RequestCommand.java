package com.jxtb.sdk.core;

import com.jxtb.sdk.RequestContext;
import com.jxtb.sdk.RequestProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jxtb on 2019/6/14.
 */
public class RequestCommand implements Runnable{

    private Logger logger = LoggerFactory.getLogger(RequestCommand.class);
    private RequestProcess process;
    private String requestContent;
    private Request request;
    private Response response;

    public RequestCommand(RequestProcess process, String requestContent, Request request, Response response) {
        this.process = process;
        this.requestContent = requestContent;
        this.request = request;
        this.response = response;
    }

    @Override
    public void run() {
        logger.debug("thread name is :" + Thread.currentThread().getName());
        try{
            RequestContext.setRequest(request);
            RequestContext.setResponse(response);
            process.process(requestContent);
        }catch (Throwable t){
            ;
        }finally {
            RequestContext.removeRequest();
            RequestContext.removeResponse();
            RequestContext.removeJsonObject();
        }
    }
}
