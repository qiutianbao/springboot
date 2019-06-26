package com.jxtb.dubbo;

import com.jxtb.dubbo.req.CustomMappingReq;

/**
 * Created by jxtb on 2019/6/13.
 */
public interface LookupCustomIdService {
    String lookupCustomId(CustomMappingReq req);
}
