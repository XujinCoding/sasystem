package com.sa.common.converter;

import org.apache.poi.ss.formula.functions.T;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类型转换注解, 这个注解标注在字段和参数上边, 在运行时生效
 * @author starttimesxj
 */
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumFormat {
    /**
     * 传一个类对象
     * @return
     */
    Class<T> value();
}
