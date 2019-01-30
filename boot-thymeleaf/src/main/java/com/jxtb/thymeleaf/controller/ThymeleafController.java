package com.jxtb.thymeleaf.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxtb.thymeleaf.entity.Commodity;
import com.jxtb.thymeleaf.entity.User;
import com.jxtb.thymeleaf.service.CommodityService;
import com.jxtb.thymeleaf.common.ResultUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/commodity")
public class ThymeleafController {
	
	@RequestMapping("home")
	public String hello(Map<String,Object> map){
		User user = new User("1", "fei", 22, "爱好：篮球","admin");
		map.put("user",user);
		
		List<User> list = new ArrayList<>();
		for(int i =0;i<5;i++){
			User u = new User(""+(i+2), "fei"+(i+2), 22+(i+2), "爱好：篮球"+(i+2),"user"+i);
			list.add(u);
		}
		map.put("userList",list);
		
		return "home";
	}

	@Autowired
	private CommodityService commodityService;

	/*
     * description:获取商品
     * @ApiOperation：描述接口
     */
	@ApiOperation(value = "查询所有商品", notes = "查询所有商品")
	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	public String findAll(Map<String,Object> map,@RequestParam(value = "currentPage", defaultValue = "1", required = false) int currentPage,
								@RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize) {
		PageHelper.startPage(currentPage, pageSize);
		List<Commodity> list=commodityService.findAll();
		PageInfo<Commodity> pageInfo=new PageInfo<Commodity>(list);
		map.put("page",pageInfo );
		return "commodity/commodity_list";
	}

	/*
     * description:根据id获取商品
     */
	@ApiOperation(value = "根据id获取商品", notes = "根据id获取商品")
	//@ApiImplicitParams：多个请求参数
	@ApiImplicitParams({
			//@ApiImplicitParam：一个请求参数
			@ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "query", dataType = "int")
	})
	@RequestMapping(value = "findById", method = RequestMethod.GET)
	public String findById(Integer id,Map<String,Object> map) {
		if (id != null && id != 0) {
			map.put("commodity", commodityService.findById(id));
		}
		return "commodity/edit";
	}

	/*
     * description:根据id删除商品
     */
	@ApiOperation(value = "根据id删除商品", notes = "根据id删除商品")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "主键id", required = true, paramType = "query", dataType = "int")
	})
	@RequestMapping(value = "deleteById", method = RequestMethod.GET)
	@ResponseBody
	public Object deleteById(Integer id) {
		ResultUtils res = new ResultUtils();
		try {
			commodityService.deleteById(id);
		} catch (Exception e) {
			res.errorResult();
		}
		return res.successResult();
	}

	/*
     * description:修改商品
     */
	@ApiOperation(value = "修改商品", notes = "修改商品")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "commodity", value = "实体对象", required = true, paramType = "body", dataType = "Commodity")
	})
	@RequestMapping(value = "editCommodity")
	@ResponseBody
	public Object editCommodity(Commodity commodity) {
		ResultUtils res = new ResultUtils();
		try {
			if (commodity.getId() != null) {
				commodityService.updateCommodity(commodity);
			} else {
				commodityService.insertCommodity(commodity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return res.errorResult();
		}
		return res.successResult();
	}

}
