package com.jxtb.velocity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Map;

/**
 * Created by jxtb on 2019/2/1.
 */
@Controller
@SpringBootApplication
public class VelocityApplication {
    @RequestMapping("/")
    public String velocityTest(Map map){
        map.put("message", "这是测试的内容。。。");
        map.put("toUserName", "张三1");
        map.put("fromUserName", "吉祥天宝");
        map.put("time",new Date());
        return "index";
    }


    public static void main(String[] args) {
        SpringApplication.run(VelocityApplication.class, args);
    }
}
