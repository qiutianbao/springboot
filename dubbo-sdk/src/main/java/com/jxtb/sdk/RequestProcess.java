package com.jxtb.sdk;

import java.util.concurrent.ExecutorService;

/**
 * Created by jxtb on 2019/6/14.
 */
public interface RequestProcess {
    void process(String requestParam);
    void destory();
    ExecutorService getExecutorService();
    void setExecutorService(ExecutorService executor);
}
