package com.jxtb.cxf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by jxtb on 2019/7/11.
 */
@SpringBootApplication
public class CxfApplication {

    //http://localhost:8080/demo/api?wsdl

    public static void main(String[] args) {
        SpringApplication.run(CxfApplication.class,args);
    }

}
