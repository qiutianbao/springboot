package com.jxtb.mp.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxtb.mp.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jxtb on 2019/11/30.
 * 用户数据库访问接口
 */
@Repository
public interface UserMapper extends BaseMapper<User>{

    @Select("select * from sys_user ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER)Wrapper<User> wrappers);

    List<User> selectAllInfo(@Param(Constants.WRAPPER)Wrapper<User> wrappers);

    IPage<User> selectUserPage(Page<User> page, @Param(Constants.WRAPPER)Wrapper<User> wrappers);

}
