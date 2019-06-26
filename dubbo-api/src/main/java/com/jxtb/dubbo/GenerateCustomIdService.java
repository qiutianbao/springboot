package com.jxtb.dubbo;

import com.jxtb.dubbo.req.CustomMappingReq;

/**
 * Created by jxtb on 2019/6/18.
 */
public interface GenerateCustomIdService {
    String generateCustomId(CustomMappingReq req);
}
