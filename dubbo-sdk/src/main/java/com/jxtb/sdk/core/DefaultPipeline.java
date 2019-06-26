package com.jxtb.sdk.core;

import com.jxtb.sdk.Filter;
import com.jxtb.sdk.model.ServiceConfigure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jxtb on 2019/6/14.
 */
public class DefaultPipeline implements Pipeline{

    private List<Filter> filters = new ArrayList<>();
    private ServiceConfigure serviceConfigure;

    public DefaultPipeline(ServiceConfigure serviceConfigure) {
        this.serviceConfigure = serviceConfigure;
    }

    @Override
    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    @Override
    public Filter[] getFilters() {
        return filters.toArray(new Filter[]{});
    }

    @Override
    public void removeFilter(Filter filter) {
        filters.remove(filter);
    }

    @Override
    public Filter getFirst() {
        return filters.get(0);
    }

    @Override
    public Filter getLast() {
        return filters.get(filters.size() - 1);
    }

    @Override
    public ServiceConfigure getServiceConfigure() {
        return this.serviceConfigure;
    }
}
