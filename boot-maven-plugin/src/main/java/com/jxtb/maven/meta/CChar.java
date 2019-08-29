package com.jxtb.maven.meta;

import java.lang.annotation.*;
import java.math.RoundingMode;

/**
 * Created by jxtb on 2019/6/26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CChar {
    int value() default 1;
    boolean zeroPadding() default true;
    String formatPattern() default "";
    boolean leftPadding() default false;
    boolean autoTrim() default true;
    int precision() default 0;
    RoundingMode rounding() default  RoundingMode.HALF_UP;
    String datePattern() default "";
    boolean required() default false;
    int order();
}
