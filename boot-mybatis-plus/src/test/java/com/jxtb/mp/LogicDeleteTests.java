package com.jxtb.mp;

import com.jxtb.mp.dao.UserMapper;
import com.jxtb.mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by jxtb on 2019/12/1.
 * 逻辑删除测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogicDeleteTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void deleteById(){
        int rows = userMapper.deleteById(1200665957077467138L);
        System.out.println("影响行数：" + rows);
    }

    @Test
    public void select(){
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }

    @Test
    public void updateById(){
        int rows = userMapper.deleteById(1200665957077467138L);
        System.out.println("影响行数：" + rows);
    }

}
