package com.jxtb.mp;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jxtb.mp.entity.User;
import com.jxtb.mp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jxtb on 2019/12/1.
 * service测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void getOne(){
       User user = userService.getOne(Wrappers.<User> lambdaQuery().gt(User::getUserId,2),false);
        System.out.println(user);
    }

}
