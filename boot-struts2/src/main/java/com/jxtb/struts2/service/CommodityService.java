package com.jxtb.struts2.service;

import com.jxtb.struts2.entity.Commodity;
import com.jxtb.struts2.mapper.CommodityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* 商品Service实现类
* */
@Service
public class CommodityService {
    @Autowired
    private CommodityMapper commodityMapper;

    //获取全部商品
    public List<Commodity> findAll(){
        return commodityMapper.findAll();
    }

    //新增商品
    public void insertCommodity(Commodity commodity){
        commodityMapper.insertCommodity(commodity);
    }

    //修改商品
    public void updateCommodity(Commodity commodity){
        commodityMapper.updateCommodity(commodity);
    }

    //根据id获取商品
    public Commodity findById(Integer id){
        return commodityMapper.findById(id);
    }

    //根据id删除商品
    public void deleteById(Integer id){
        commodityMapper.deleteById(id);
    }
}
