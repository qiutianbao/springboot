package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSONObject;
import com.jxtb.dubbo.GenerateCustomIdService;
import com.jxtb.dubbo.req.CustomMappingReq;
import com.jxtb.sdk.Filter;
import com.jxtb.sdk.RequestContext;
import com.jxtb.sdk.context.ApplicationContextHolder;
import com.jxtb.sdk.model.ServiceConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 产生客户号
 * Created by jxtb on 2019/6/14.
 */
public class BornCustIdFilter extends BaseFilter implements Filter{

    private static final Logger logger = LoggerFactory.getLogger(BornCustIdFilter.class);
    private static final String GENERATE_CUSTOM = "generateCustomIdService";
    @Override
    public void invoke(String requestParam) {
        JSONObject jsonObject = RequestContext.getJsonObject();
        ServiceConfigure serviceConfigure = this.getPipeline().getServiceConfigure();
        String custId = getCustId(serviceConfigure.getKeyPropertyType(), serviceConfigure.getKeyPropertyName(), jsonObject);
        logger.info("客户号{}", custId);
        if(custId == null || custId.length() == 0){
            GenerateCustomIdService service = ApplicationContextHolder.getBean(GENERATE_CUSTOM);
            long st = System.currentTimeMillis();
            CustomMappingReq req = new CustomMappingReq();
            req.setOrg(org);
            req.setIdType(serviceConfigure.getKeyPropertyType());
            req.setIdNo(serviceConfigure.getKeyPropertyName());
            custId = service.generateCustomId(req);
            logger.error("cust={}", (System.currentTimeMillis() - st));
            jsonObject.getJSONObject(HEAD).put(CUST_ID_FIELD, custId);
        }else{
            jsonObject.getJSONObject(HEAD).put(CUST_ID_FIELD, custId);
        }
        getNext().invoke(requestParam);
    }
}
