package com.jxtb.batch.common.utils.sdk.annotation;

import java.lang.annotation.*;

/**
 * 二进制字段，只取整数部分，最多6位
 * Created by jxtb on 2019/7/3.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CBinaryInt {
    boolean bigEndian() default true;
    /**
     * 字节数，最大为8
     */
    int length() default 4;
    int order();
}
