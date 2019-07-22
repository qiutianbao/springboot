package com.jxtb.cxf.service.impl;

import com.jxtb.cxf.service.DemoService;

import javax.jws.WebService;
import java.util.Date;

/**
 * Created by jxtb on 2019/7/11.
 */
@WebService(serviceName = "DemoService", // 与接口中指定的name一致
        targetNamespace = "http://service.cxf.jxtb.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.jxtb.cxf.service.DemoService"// 接口地址
)
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String user) {
        return user+"，现在时间："+"("+new Date()+")";
    }
}
