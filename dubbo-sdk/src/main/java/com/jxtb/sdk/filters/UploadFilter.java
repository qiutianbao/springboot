package com.jxtb.sdk.filters;

import com.alibaba.fastjson.JSONObject;
import com.jxtb.sdk.*;
import com.jxtb.sdk.util.DateUtils;
import com.jxtb.sdk.util.MD5Util;

import java.util.*;

/**
 * Created by jxtb on 2019/6/19.
 */
public class UploadFilter extends BaseFilter implements Filter{
    private static final String ACCESSORY_FILE_LIST = "accessoryFileList";
    @Override
    public void invoke(String requestParam) {
        List<Map<String, Object>> fileList = new ArrayList<>();
        Map<String, Object> hp ;
        JSONObject jsonObject = RequestContext.getJsonObject();
        Hashtable requestTb =  (Hashtable)RequestContext.getParamObject();
        if(requestTb == null){
            getNext().invoke(requestParam);
            return;
        }
        Enumeration paramEnum = requestTb.elements();
        Multipart mp;
        while (paramEnum.hasMoreElements()){
            mp = (Multipart)paramEnum.nextElement();
            String fileName = mp.getFileName();
            if(fileName == null){
                continue;
            }
            int extPos = fileName.lastIndexOf(".");
            String ext,newFileName;
            if(extPos > 0){
                ext = fileName.substring(extPos);
                newFileName = fileName.substring(0, extPos);
            }else{
                ext = "";
                newFileName = fileName;
            }
            String custId = jsonObject.getJSONObject("head").getString(CUST_ID_FIELD);
            String bdName = buildFileName(newFileName, custId) + ext;
            String retFileName = uploadFile(bdName, mp.getContent());
            if(requestParam == null){
                SendResponseFactory.createSendResponse(RequestContext.getResponse()).
                        sendError(MessageUtil.buildNoBodyMessage(Constants.ERROR_CODE_010009, "上传文件失败", jsonObject));
                return;
            }
            hp = new HashMap<>();
            hp.put("accessoryName", fileName);
            hp.put("accessoryType", fileName);
            hp.put("accessoryId", retFileName);
            fileList.add(hp);
        }
        jsonObject.getJSONObject("request").put(ACCESSORY_FILE_LIST, fileList);
        getNext().invoke(requestParam);
    }

    private String buildFileName(String orgFileName, String custNo){
        String newFileName = MD5Util.md5Hex(DateUtils.getServiceTraceNo() + custNo);
        return newFileName;
    }

    private String uploadFile(String readFileName, byte[] fileContent){
        try{
            //调用文件的上传接口
            return null;
        }catch (Exception e){
            return null;
        }
    }

}
