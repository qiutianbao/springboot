package com.jxtb.sdk.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jxtb on 2019/6/19.
 */
public class DateUtils {
    public static String getServiceTraceNo(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String nowdate = sdf.format(new Date());
        int randostr = (int)(Math.random() * 90.0D) + 10;
        return nowdate + String.valueOf(randostr);
    }
}
