package com.jxtb.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by jxtb on 2019/1/31.
 */
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    // /css/**和/index的资源不需要验证，直接可以请求
    // /user/**的资源需要验证，权限是USER /admin/**的资源需要验证，权限是ADMIN
    // 登录地址是/login 登录失败地址是 /login_error
    // 异常重定向到 /401
    // 注销跳转到 /logout
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeRequests()
                .antMatchers("/css/**","/index").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin().loginPage("/login").failureUrl("/login_error")
                .and()
                .exceptionHandling().accessDeniedPage("/401");

        httpSecurity.logout().logoutSuccessUrl("/logout");
    }


    //内存中创建用户，用户名为jxtb，密码123，权限是USER
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("jxtb").password("123").roles("USER");
    }
}
