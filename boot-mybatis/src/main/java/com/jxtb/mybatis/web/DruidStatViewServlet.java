package com.jxtb.mybatis.web;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Created by jxtb on 2019/7/10..
 *  http://localhost:8080/druid/index.html
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*", initParams = {
        @WebInitParam(name = "allow", value = ""), // IP白名单
        @WebInitParam(name = "deny", value = ""),
        // IP黑名单
        @WebInitParam(name = "loginUsername", value = "admin"), // 用户名
        @WebInitParam(name = "loginPassword", value = "admin*druid"), // 密码
        @WebInitParam(name = "resetEnable", value = "true")})
public class DruidStatViewServlet extends StatViewServlet {
}
