package com.learn.web.w04_exception.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义校验注解：状态只能是 0 或 1，null 算通过（已写好）。
 *
 * <p>对照 ch05 你写的 @Range + Validator：注解只负责“声明规则”，真正的检查逻辑在 {@link StatusValidator} 里。
 * {@code @Constraint(validatedBy = ...)} 就是告诉校验框架“这个注解用哪个类来检查”。
 * 下面三个属性 message / groups / payload 是 Bean Validation 规定必须有的。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusValidator.class)
public @interface Status {

    String message() default "状态只能是0或1";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
