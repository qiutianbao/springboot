package com.jxtb.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.jxtb.dubbo.HelloService;


@Service(version = "1.0.0")
public class HelloServiceProvider implements HelloService {

    @Override
    public String SayHello(String name) {
        return "Hello , "+name;
    }
}
