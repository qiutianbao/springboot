package com.jxtb.velocity;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jxtb on 2019/2/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VelocityApplicationTests {
    @Test
    public void contextLoads() {
    }

    @Autowired
    VelocityEngine velocityEngine;

    @Test
    public void velocityTest(){
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("message", "这是测试的内容。。。");
        model.put("toUserName", "张三");
        model.put("fromUserName", "吉祥天宝");
        model.put("time",new Date());
        System.out.println(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/templates/index.vm", "UTF-8", model));
    }

}
