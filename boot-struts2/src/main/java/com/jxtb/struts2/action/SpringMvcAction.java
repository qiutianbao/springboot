package com.jxtb.struts2.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jxtb.struts2.entity.Commodity;
import com.jxtb.struts2.service.CommodityService;
import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Controll层
 * @author Administrator
 * 因为这里需要解决对Oracle数据中EMP表的CRUD所以我定义了一个实体类
 * 根据页面传入的值，这里需要使用模型驱动
 * curPage则是分页    表示当前页
 * 这里整合了Ajax 而stuts2又不能直接返回值
 * 所以这里我使用了JSONArray将最后的结果转换为Json数组
 * OutputStream对象将结果返回给页面
 */
@Namespace("/")
public class SpringMvcAction extends ActionSupport implements ModelDriven<Commodity>{

    @Autowired
    private CommodityService commodityService;
    //实体类对象
    private Commodity commodity;

    /**
     * 查询方法
     * @return
     * @throws IOException
     */
    @Action(value="/query")
    public String query() throws IOException{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/json");
        response.setCharacterEncoding("UTF-8");
        //获取 输出流
        OutputStream os=ServletActionContext.getResponse().getOutputStream();
        //获取查询出来经过处理的分页结果
        List<Commodity> list=commodityService.findAll();
        PageInfo<Commodity> pageInfo=new PageInfo<Commodity>(list);
        //将分页结果转换为json数组并且转换为字符串
        String json=JSONArray.fromObject(pageInfo).toString();
        //将结果返回给页面
        os.write(json.getBytes("UTF-8"));
        return NONE;
    }


    @Action(value="/queryById")
    public Commodity queryById(Integer id){
        return commodityService.findById(id);
    }

    /**
     * 修改方法
     * @throws IOException
     */
    @Action(value="/update")
    public void update(Commodity commodity) throws IOException{
        OutputStream os=ServletActionContext.getResponse().getOutputStream();
        try {
            commodityService.updateCommodity(commodity);
            os.write("1".getBytes());
        } catch (Exception e) {
            os.write("2".getBytes());
        }
    }

    /**
     * 删除方法
     * @throws IOException
     */
    @Action(value="/del")
    public void delete(Integer id) throws IOException{
        OutputStream os=ServletActionContext.getResponse().getOutputStream();
        try {
            commodityService.deleteById(id);
            os.write("1".getBytes());
        } catch (Exception e) {
            os.write("2".getBytes());
        }

    }

    /**
     * 增加方法
     * @throws IOException
     */
    @Action(value="/add")
    public void add(Commodity commodity) throws IOException{
        OutputStream os=ServletActionContext.getResponse().getOutputStream();
        try {
            commodityService.insertCommodity(commodity);
            os.write("1".getBytes());
        } catch (Exception e) {
            os.write("2".getBytes());
        }
    }

    @Override
    public Commodity getModel() {
        if(commodity==null){
            commodity=new Commodity();
        }
        return commodity;
    }
}
