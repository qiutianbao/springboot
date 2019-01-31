package com.jxtb.security.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jxtb on 2019/1/31.
 */
@Controller
public class SecurityController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/index")
    public String index2(){
        return "index";
    }

    @RequestMapping("/user")
    public String user(){
        return "user/index";
    }

    @RequestMapping("/admin")
    public String admin(){
        return "admin/index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/login_error")
    public String login_error(Model model){
        model.addAttribute("login_error", "用户名或密码错误");
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(Model model){
        model.addAttribute("login_error", "注销成功");
        return "login";
    }

    @RequestMapping("/401")
    public String error(){
        return "401";
    }
}
