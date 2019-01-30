package com.jxtb.struts2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by jxtb on 2019/1/30.
 */
//定义全局事务
@EnableTransactionManagement
@SpringBootApplication
public class StrutsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StrutsApplication.class, args);
    }
}
