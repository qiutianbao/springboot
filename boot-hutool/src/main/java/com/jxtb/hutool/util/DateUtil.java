package com.jxtb.hutool.util;

import java.text.SimpleDateFormat;

/**
 * Created by jxtb on 2019/8/7.
 */
public class DateUtil {

    //使用ThreadLocal代替原来的new SimpleDateFormat
    private static final ThreadLocal<SimpleDateFormat> dateFormatter = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return  new SimpleDateFormat("yyyy-MM-dd");
        }
    };

}
