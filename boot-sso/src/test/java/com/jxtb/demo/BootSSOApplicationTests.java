package com.jxtb.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jxtb.BootSSOApplication;
import com.jxtb.pojo.User;
import com.jxtb.service.UserService;
import com.jxtb.utils.ItdragonUtils;

/**
 * @RunWith	它是一个运行器
 * @RunWith(SpringRunner.class) 表示让测试运行于Spring测试环境，不用启动spring容器即可使用Spring环境
 * @SpringBootTest(classes=StartApplication.class)  表示将StartApplication.class纳入到测试环境中，若不加这个则提示bean找不到。
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes= BootSSOApplication.class)
public class BootSSOApplicationTests {
	
	@Autowired
	private UserService userService;

	@Test	// 测试注册，新增数据
	public void registerUser() {
		User user = new User();
		user.setAccount("jxtb");
		user.setUserName("ITDragonGit");
		user.setEmail("jxtb@git.com");
		user.setIphone("12349857999");
		user.setPlainPassword("123456789");
		user.setPlatform("github");
		user.setCreatedDate(ItdragonUtils.getCurrentDateTime());
		user.setUpdatedDate(ItdragonUtils.getCurrentDateTime());
		ItdragonUtils.entryptPassword(user);
		System.out.println(user);
		userService.registerUser(user);
	}
	
}