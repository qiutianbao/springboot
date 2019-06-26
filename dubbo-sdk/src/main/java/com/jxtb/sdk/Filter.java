package com.jxtb.sdk;

import com.jxtb.sdk.core.Pipeline;

/**
 * 过滤器
 * Created by jxtb on 2019/6/12.
 */
public interface Filter {
    Filter getNext();
    void setNext(Filter filter);
    void invoke(String requestParam);
    Pipeline getPipeline();
    void setPipeline(Pipeline pipeline);
}
