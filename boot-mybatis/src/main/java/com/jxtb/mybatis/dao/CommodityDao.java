package com.jxtb.mybatis.dao;

import com.jxtb.mybatis.entity.Commodity;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jxtb on 2019/3/26.
 */
public interface CommodityDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Commodity record);

    int insertSelective(Commodity record);

    Commodity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Commodity record);

    int updateByPrimaryKey(Commodity record);

}
