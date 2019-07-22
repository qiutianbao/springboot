package com.jxtb.mybatis;

import com.jxtb.mybatis.entity.Commodity;
import com.jxtb.mybatis.service.CommodityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by jxtb on 2019/7/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CommodityTest {

    @Resource
    private CommodityService commodityService;

    @Test
    public void testCommodity(){
        int userId = Integer.parseInt("15");
        Commodity user = this.commodityService.getCommodityById(userId);
        System.out.println(user.getId() + "<=====" + user.getName());
    }

    @Test
    public void testLog(){
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.trace("这是trace日志。。。");
        log.debug("这是debug日志。。。");
        log.info("这是info日志。。。");
        log.warn("这是warn日志。。。");
        log.error("这是error日志。。。");

    }

}
