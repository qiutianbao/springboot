package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.*;
import com.jxtb.sdk.model.ServiceConfigure;
import org.springframework.util.StringUtils;

/**
 * Created by jxtb on 2019/6/12.
 */
public class LookupFilter extends BaseFilter implements Filter{
    @Override
    public void invoke(String requestParam) {
        ServiceConfigure serviceConfigure = this.getPipeline().getServiceConfigure();
        if("0".equals(serviceConfigure.getIsCustId())){
            JSONObject jsonObject = RequestContext.getJsonObject();
            String custId = getCustId(serviceConfigure.getKeyPropertyType(), serviceConfigure.getKeyPropertyName() ,jsonObject);
            if(StringUtils.isEmpty(custId)){
                SendResponseFactory.createSendResponse(RequestContext.getResponse()).sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010013,"未找到匹配的客户号",jsonObject));
                return;
            }
            jsonObject.getJSONObject(HEAD).put(CUST_ID_FIELD,custId);
            this.getNext().invoke(requestParam);
        }
    }
}
