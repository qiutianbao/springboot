package com.jxtb.sdk.model;


import lombok.Data;

import java.util.List;

/**
 * Created by jxtb on 2019/6/12.
 */
@Data
public class ServiceConfigure{
    private String id;
    private String className;
    private String methodName;
    private String keyPropertyName;
    private String keyPropertyType;
    private String isCustId;
    private String systemType;
    private String requestProtocol;
    List<String> filterDefs;
}

