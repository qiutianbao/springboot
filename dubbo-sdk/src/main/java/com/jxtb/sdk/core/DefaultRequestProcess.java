package com.jxtb.sdk.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by jxtb on 2019/6/14.
 */
public class DefaultRequestProcess implements RequestProcess {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRequestProcess.class);

    private Map<String, Pipeline> txnMapping;
    private static final String TXN_CODE = "serviceId";
    private static final String HEAD = "head";
    private ExecutorService executors;

    @Override
    public void process(String requestParam) {
        JSONObject jsonObject = null;
        try{
            jsonObject = JSON.parseObject(requestParam);
            String txnValue = jsonObject.getJSONObject(HEAD).getString(TXN_CODE);
            if(StringUtils.isEmpty(txnValue)){
                SendResponseFactory.createSendResponse(RequestContext.getResponse())
                        .sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010002, "交易代码未填写", jsonObject));
                return;
            }

            RequestContext.setJsonObject(jsonObject);

            Pipeline pipeline = txnMapping.get(txnValue);
            if(pipeline == null){
                SendResponseFactory.createSendResponse(RequestContext.getResponse())
                        .sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010002, "交易代码未配置", jsonObject));
                return;
            }
            pipeline.getFirst().invoke(requestParam);

        }catch (Throwable t){
            t.printStackTrace();
            logger.error("请求的报文不符合json格式",t);
            SendResponseFactory.createSendResponse(RequestContext.getResponse())
            .sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010017, "请求的报文不符合json格式", jsonObject));
            return;
        }finally {
            RequestContext.removeJsonObject();
        }
    }

    @Override
    public void destory() {

    }

    @Override
    public ExecutorService getExecutorService() {
        return executors;
    }

    @Override
    public void setExecutorService(ExecutorService executor) {
        if(this.executors == null){
            this.executors = executor;
        }
    }

    public Map<String, Pipeline> getTxnMapping() {
        return txnMapping;
    }

    public void setTxnMapping(Map<String, Pipeline> txnMapping) {
        this.txnMapping = txnMapping;
    }
}
