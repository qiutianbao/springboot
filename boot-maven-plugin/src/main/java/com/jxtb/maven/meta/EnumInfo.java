package com.jxtb.maven.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by jxtb on 2019/6/26.
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface EnumInfo {
    String[] values();
}
