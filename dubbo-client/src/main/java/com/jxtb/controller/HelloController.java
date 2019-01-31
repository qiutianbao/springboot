package com.jxtb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jxtb.dubbo.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @Reference(version = "1.0.0")
    HelloService helloService;

    // http://localhost:8080/hello?name=jxtb
    @GetMapping("hello")
    public String sayHello(String name){
        return helloService.SayHello(name);
    }
}
