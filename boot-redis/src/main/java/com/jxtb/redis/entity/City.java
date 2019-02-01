package com.jxtb.redis.entity;

import java.io.Serializable;

/**
 * Created by jxtb on 2019/1/31.
 */
public class City implements Serializable {
    private int cityId;
    private String cityName;
    private String cityIntroduce;

    public City(int cityId, String cityName, String cityIntroduce) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityIntroduce = cityIntroduce;
    }

    public City(String cityName, String cityIntroduce) {
        this.cityName = cityName;
        this.cityIntroduce = cityIntroduce;
    }

    public City() {
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityIntroduce() {
        return cityIntroduce;
    }

    public void setCityIntroduce(String cityIntroduce) {
        this.cityIntroduce = cityIntroduce;
    }
}

