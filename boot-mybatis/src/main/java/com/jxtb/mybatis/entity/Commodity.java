package com.jxtb.mybatis.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
* 商品实体类
* */
@Entity
@ApiModel(description = "commodity")
public class Commodity {
    //主键id
    @ApiModelProperty(value = "主键id", hidden = true)
    @GeneratedValue
    @Id
    private Integer id;
    //商品名称
    @ApiModelProperty(value = "商品名称")
    @Column
    private String name;
    //商品规格
    @ApiModelProperty(value = "商品规格")
    @Column
    private String standard;
    //商品温度
    @ApiModelProperty(value = "商品温度")
    @Column
    private String temperature;
    //商品价格
    @ApiModelProperty(value = "商品价格")
    @Column
    private int price;
    //商品描述
    @ApiModelProperty(value = "商品描述")
    @Column
    private String description;

    public Commodity() {
    }

    public Commodity(Integer id,String name, String standard, String temperature, int price, String description) {
        this.id=id;
        this.name = name;
        this.standard = standard;
        this.temperature = temperature;
        this.price = price;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
