package com.jxtb.rabbitmq.controller;

import com.jxtb.rabbitmq.sender.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jxtb on 2019/2/1.
 */
@RestController
public class RabbbitController {
    @Autowired
    private Sender sender;

    @GetMapping("hello")
    public String helloTest(){
        sender.send();
        return "success";
    }
}
