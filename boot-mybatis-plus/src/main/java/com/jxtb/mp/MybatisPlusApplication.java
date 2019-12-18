package com.jxtb.mp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by jxtb on 2019/11/30.
 * mybatisplus启动类
 */
@SpringBootApplication
@MapperScan("com.jxtb.mp.dao")
public class MybatisPlusApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class,args);
    }
}
