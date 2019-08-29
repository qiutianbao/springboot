package com.jxtb.hutool.util;

import com.jxtb.hutool.exception.ProcessException;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by jxtb on 2019/8/1.
 */
public class CheckUtil {

    public static void rejectNull(Object o, String errorCode, String errorMsg) throws ProcessException{
        if(isEmpty(o)){
            if(errorMsg == null){
                throw new ProcessException(errorCode, "不允许为空");
            }else{
                throw new ProcessException(errorCode, errorMsg);
            }
        }
    }

    static final String ERRSO01 = "ERRSO01";
    public static void main(String[] args) {
        String name = "hello";
        CheckUtil.rejectNull(name, ERRSO01, "姓名不能为空");
        if(isEmpty(name)){
            System.out.println("不允许为空");
        }else{
            System.out.println("ok");
        }

        if(isBlank(name)){
            System.out.println("不允许为空");
        }else{
            System.out.println("ok");
        }
    }

}
