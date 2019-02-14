package com.jxtb.angular.controller;

import com.jxtb.angular.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jxtb on 2019/2/1.
 */
@RestController
public class AngularController {

    //http://127.0.0.1:8080/static/index.html
    @GetMapping("/test")
    public User test(){
        System.out.println("==================> augular");
        User user = new User();
        user.setUsername("jackson");
        user.setPassword("123456");
        return user;
    }

}
