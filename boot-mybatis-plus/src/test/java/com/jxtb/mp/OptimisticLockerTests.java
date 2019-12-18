package com.jxtb.mp;

import com.jxtb.mp.dao.UserMapper;
import com.jxtb.mp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jxtb on 2019/12/1.
 * 乐观锁版本号测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OptimisticLockerTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void updateById(){

        User user = userMapper.selectById(2L);
        user.setAge(18);

        int rows = userMapper.updateById(user);
        System.out.println("影响行数：" + rows);

    }

}
