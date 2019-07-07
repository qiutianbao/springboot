package com.jxtb.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by jxtb on 2019/7/5.
 */
@SpringBootApplication
@ImportResource({"classpath*:spring/*.xml"})
@ComponentScan("com.jxtb.batch")
public class BatchJxtbApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchJxtbApplication.class,args);
    }

}
