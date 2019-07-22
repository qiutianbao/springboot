package com.jxtb.cxf.service;

import javax.jws.WebService;

/**
 * Created by jxtb on 2019/7/11.
 */
@WebService(name = "DemoService", // 暴露服务名称
        targetNamespace = "http://service.cxf.jxtb.com"// 命名空间,一般是接口的包名倒序
)
public interface DemoService {

    String sayHello(String user);

}
