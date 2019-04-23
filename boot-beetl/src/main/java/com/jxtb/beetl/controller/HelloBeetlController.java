package com.jxtb.beetl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jxtb on 2019/3/28.
 */
@Controller
@RequestMapping("/home")
public class HelloBeetlController {

    private static Logger logger = LoggerFactory.getLogger(HelloBeetlController.class);

    /**
     * 测试beetl模板
     * http://localhost:8080/boot-beetl/home/add
     * @return
     */
    @RequestMapping("/add")
    public ModelAndView home() {

        ModelAndView modelAndView = new ModelAndView();
        logger.info("add request");
        modelAndView.addObject("email", "apk2sf@163.com");
        modelAndView.setViewName("add");

        return modelAndView;
    }


}
