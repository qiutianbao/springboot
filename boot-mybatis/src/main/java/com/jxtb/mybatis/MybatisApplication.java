package com.jxtb.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * Created by jxtb on 2019/4/23.
 */
@SpringBootApplication
@MapperScan("com.jxtb.mybatis.dao")
@ServletComponentScan(value = "com.jxtb.mybatis.web")
public class MybatisApplication {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class,args);
    }
}
