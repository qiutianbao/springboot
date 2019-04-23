package com.jxtb.mybatis.service;

import com.jxtb.mybatis.dao.CommodityDao;
import com.jxtb.mybatis.entity.Commodity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jxtb on 2019/4/23.
 */
@Service("commodityService")
public class CommodityServiceImpl implements CommodityService{

    @Resource
    private CommodityDao commodityDao;


    public Commodity getCommodityById(int id) {
        return commodityDao.selectByPrimaryKey(id);
    }

    public boolean addCommodity(Commodity record){
        boolean result = false;
        try {
            commodityDao.insertSelective(record);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
