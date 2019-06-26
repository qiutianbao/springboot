package com.jxtb.web;

import com.jxtb.sdk.config.ConfigurationManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * // 在哪个class添加了这个注释就意味着哪个class就是servlet
 * Created by jxtb on 2019/6/24.
 */
@WebServlet(name = "WebServletDemo", urlPatterns = "/test")
public class WebServletDemo extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("init()");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("启动成功！");
    }
    @Override
    public void destroy() {
        System.out.println("destroy()");
    }
}
