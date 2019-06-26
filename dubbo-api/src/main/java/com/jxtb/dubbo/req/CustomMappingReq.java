package com.jxtb.dubbo.req;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by jxtb on 2019/6/13.
 */
@Data
public class CustomMappingReq extends AbstractReq{
    private String org;
    private String custId;
    private String custName;
    private String custType;
    private String idNo;
    private String idType;
}
