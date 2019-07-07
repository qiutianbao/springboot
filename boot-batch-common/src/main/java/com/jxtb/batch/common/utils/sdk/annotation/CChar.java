package com.jxtb.batch.common.utils.sdk.annotation;

import java.lang.annotation.*;
import java.math.RoundingMode;

/**
 * Created by jxtb on 2019/6/28.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CChar {

    int value() default 1;

    /**
     * 在指定到证书数字段的情况下是否要填充0
     */
    boolean zeroPadding() default true;

    /**
     * 在指定到数字字段的情况下的格式化字符串，使用MessageFormat的格式，比zeroPadding优先级高
     */
    String formatPattern() default "";

    /**
     * 向左填充空格。默认情况下为false，即尾随追加空格
     */
    boolean leftPadding() default false;

    /**
     * 对于字符串的类型，在解析时自动执行trim()
     */
    boolean autoTrim() default  true;

    /**
     * 对于BigDecimal类型，解析时的小数位
     */
    int precision() default 0;

    /**
     * 在输出时如果有小数并且指定精度时的截取方式，默认为四舍五入
     */
    RoundingMode rounding() default RoundingMode.HALF_UP;

    /**
     * 对于日期类型的字段输入和解析的模板，如yyyyMMdd等，见SimpleDateFormat
     */
    String datePattern() default "";

    /**
     * 必填字段
     */
    boolean required() default false;

    /**
     * 由于JDK规范没有约定 class 的 getFieIds方法返回的顺序，而且不保证这个顺序，所以这里必须显示指定字段的顺序。
     * 同一个类带有CChar 或 CBinaryInt字段以order 属性从小到大进行排序，以保证接口的稳定性
     */
    int order();

}
