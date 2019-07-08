package com.jxtb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * SpringBoot整合servlet方式一 这种方式在控制台看到了相关的输出信息，但是在浏览器打开的时候是错误的页面信息
 * Created by jxtb on 2019/6/24.
 */
@SpringBootApplication
@ServletComponentScan // 在SppringBoot启动时扫描@WebServlet，并将该类实例化
public class DubboWebServletApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboWebServletApplication.class, args);
    }
}
