package com.learn.ch05_annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 练习 1.1【找 bug】：字符串长度必须在 [min, max] 之间，null 不检查。
 *
 * <p>这个注解有一个 bug，导致 Validator 用反射永远读不到它。对比一下 {@link NotNull}，把它修好。
 */
// TODO 找出这附近的 bug（提示：对比 NotNull 的写法，想想注解能“活”到什么时候）
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Length {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "长度不合法";
}
