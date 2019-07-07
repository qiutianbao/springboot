package com.jxtb.scan;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jxtb.dubbo.HelloService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jxtb on 2019/7/2.
 */
@RestController
public class helloService {
    @Reference(version = "1.0.0")
    HelloService helloService;
}
