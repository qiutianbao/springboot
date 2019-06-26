package com.jxtb.sdk.core;

import com.jxtb.sdk.Filter;
import com.jxtb.sdk.model.ServiceConfigure;

/**
 * Created by jxtb on 2019/6/12.
 */
public interface Pipeline {
    void  addFilter(Filter filter);
    Filter[] getFilters();
    void removeFilter(Filter filter);
    Filter getFirst();
    Filter getLast();
    ServiceConfigure getServiceConfigure();
}
