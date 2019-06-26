package com.jxtb.sdk;

/**
 * Created by jxtb on 2019/6/14.
 */
public interface SendResponse {
    void sendMessage(String jsonResponse);
    void sendError(String errorResponse);
    void sendECfcaMessage(String jsonResponse);
}
