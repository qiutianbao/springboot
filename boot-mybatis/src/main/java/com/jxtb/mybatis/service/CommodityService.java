package com.jxtb.mybatis.service;

import com.jxtb.mybatis.entity.Commodity;

/**
 * Created by jxtb on 2019/4/23.
 */
public interface CommodityService {
    public Commodity getCommodityById(int id);

    boolean addCommodity(Commodity record);
}
