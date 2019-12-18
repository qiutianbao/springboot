package com.jxtb.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxtb.mp.dao.UserMapper;
import com.jxtb.mp.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by jxtb on 2019/11/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusTests {
    /**
     * 用户持久层接口注入
     */
    @Autowired
    private UserMapper userMapper;

    @Test
    public void select(){
        List<User> list = userMapper.selectList(null);
        Assert.assertEquals(2,list.size());
        list.forEach(System.out::println);
    }

    @Test
    public void insert(){
        User user = new User();
        user.setName("小黑");
        user.setAge(18);
        user.setEmail("xiaohei@163.com");
        user.setManagerId(1L);
        user.setCreateTime(LocalDateTime.now());
        User.setRemark("我是一个备注信息");
        int rows = userMapper.insert(user);
        System.out.println("影响记录数：" + rows);
    }

    @Test
    public void selectLambda(){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName,"小");
        List<User> userList = userMapper.selectList(lambdaQueryWrapper);
        userList.forEach(System.out::println);
        System.out.println("==================");
        List<User> userList2 = userMapper.selectAll(lambdaQueryWrapper);
        userList2.forEach(System.out::println);

        System.out.println("==================");
        List<User> userList3 = userMapper.selectAllInfo(lambdaQueryWrapper);
        userList3.forEach(System.out::println);

    }

    @Test
    public void selectPage(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name","小");

        Page<User> page = new Page<>(1,2);
        IPage<User> iPage = userMapper.selectPage(page,queryWrapper);
        System.out.println(iPage);

        System.out.println("==============");

        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(page,queryWrapper);
        System.out.println(mapIPage);

        System.out.println("============");
        IPage<User> userIPage = userMapper.selectUserPage(page,queryWrapper);
        System.out.println(userIPage);
    }

    @Test
    public void updateById(){
        User user = new User();
        user.setUserId(2L);
        user.setAge(29);
        int rows = userMapper.updateById(user);
        System.out.println("影响记录数：" + rows);
    }

    @Test
    public void updateByWrapper(){
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("user_id", 2).set("age", 33);
//        User user = new User();
//        user.setAge(30);
//        int rows = userMapper.update(user, userUpdateWrapper);
        int rows = userMapper.update(null, userUpdateWrapper);
        System.out.println("影响记录数：" + rows);
    }

    @Test
    public void updateByWrapperLambda(){
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUserId, 2).set(User::getAge, 25);
        int rows = userMapper.update(null, lambdaUpdateWrapper);
        System.out.println("影响记录数：" + rows);
    }

}
