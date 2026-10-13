package com.learn.ch05_annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数字（int / Integer / long / BigDecimal ... 任意 Number）必须在 [min, max] 之间。null 不检查。（已写好）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Range {

    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;

    String message() default "超出范围";
}
