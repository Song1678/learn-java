package com.learn.ch05_annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段不能为 null。（已写好，作为参考）
 *
 * <p>@Retention(RUNTIME)：注解保留到运行时，反射才能读到。
 * <p>@Target(FIELD)：只能标在字段上。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {

    String message() default "不能为空";
}
