package com.jxtb.scan;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jxtb.dubbo.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jxtb on 2019/6/25.
 */
@RestController
public class HelloWeb {
    @Reference(version = "1.0.0")
    HelloService helloService;
    @GetMapping("hello")
    public String sayHello(String name){
        return helloService.SayHello(name);
    }
}
