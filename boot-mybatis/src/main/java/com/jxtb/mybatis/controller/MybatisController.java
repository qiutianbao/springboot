package com.jxtb.mybatis.controller;

import com.jxtb.mybatis.entity.Commodity;
import com.jxtb.mybatis.service.CommodityService;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by jxtb on 2019/4/22.
 */
@RestController
public class MybatisController {

    @RequestMapping("commodity")
    public Commodity getCommodity() {
        Commodity user = new Commodity();
        user.setName("test");
        return user;
    }

    @Resource
    private CommodityService commodityService;

    //http://localhost:8080/json?id=15
    @RequestMapping(value = "/json",produces = MediaType.APPLICATION_JSON_VALUE)
    public Commodity toIndex(HttpServletRequest request, Model model){
        int userId = Integer.parseInt(request.getParameter("id"));
        Commodity user = this.commodityService.getCommodityById(userId);
        return user;
    }

    //http://localhost:8080/xml?id=15
    @RequestMapping(value = "/xml",produces = MediaType.APPLICATION_XML_VALUE)
    public Commodity xml(HttpServletRequest request, Model model){
        int userId = Integer.parseInt(request.getParameter("id"));
        Commodity user = this.commodityService.getCommodityById(userId);
        return user;
    }

}




