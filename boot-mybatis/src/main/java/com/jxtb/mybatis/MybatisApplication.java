package com.jxtb.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by jxtb on 2019/4/23.
 */
@SpringBootApplication
@MapperScan("com.jxtb.mybatis.dao")
public class MybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class,args);
    }
}
