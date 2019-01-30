package com.jxtb.freemarker.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxtb.freemarker.entity.Commodity;
import com.jxtb.freemarker.service.CommodityService;
import com.jxtb.freemarker.common.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Api(description = "商品信息接口")
@Controller
@RequestMapping("/commodity")
public class CommodityController {
    @Autowired
    private CommodityService commodityService;

    /*
     * description:获取商品
     * @ApiOperation：描述接口
     */
    @ApiOperation(value = "查询所有商品", notes = "查询所有商品")
    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    public ModelAndView findAll(@RequestParam(value = "currentPage", defaultValue = "1", required = false) int currentPage,
                                @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        ModelAndView model = new ModelAndView("commodity/commodity_list");
        List<Commodity> list=commodityService.findAll();
        PageInfo<Commodity> pageInfo=new PageInfo<Commodity>(list);
        model.addObject("page",pageInfo );
        return model;
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
    public ModelAndView findById(Integer id) {
        ModelAndView model = new ModelAndView("commodity/edit");
        if (id != null && id != 0) {
            model.addObject("commodity", commodityService.findById(id));
        }
        return model;
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


