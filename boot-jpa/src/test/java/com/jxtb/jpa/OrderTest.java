//package com.jxtb.jpa;
//
//import java.util.Date;
//
//import com.jxtb.jpa.dao.OrderRespoistory;
//import com.jxtb.jpa.entity.Order;
//import com.jxtb.jpa.entity.User;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = JpaApplication.class)
//public class OrderTest {
//
//	@Autowired
//    OrderRespoistory orderRepository;
//
//	@Test
//	public void addOrder() {
//		User user = new User(1,"cm2",22);
//		Order order = new Order( "x0002", new Date());
//		order.setUser(user);
//		orderRepository.save(order);
//	}
//
//
//}
