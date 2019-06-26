package com.jxtb.dubbo.res;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by jxtb on 2019/6/13.
 */
@Data
public class AbstractRes implements Serializable{

    /**
     * 请求服务码
     */
    private String requestId;
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回描述
     */
    private String desc;

}
