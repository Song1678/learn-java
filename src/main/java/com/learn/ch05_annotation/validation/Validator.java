package com.learn.ch05_annotation.validation;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 练习 1.2：手写一个参数校验器（对应「注解 - 使用注解 / 定义注解 / 处理注解」）
 *
 * <p>Spring 项目里你会在 Controller 参数上写 @Validated，然后在 DTO 字段上写 @NotBlank、@Length 等，
 * 框架用的就是下面这种“反射读取注解 + 检查字段值”的方式（Hibernate Validator）。
 */
public class Validator {

    private Validator() {
    }

    /**
     * 检查对象（只看类自己声明的字段）上的 @NotNull、@NotBlank、@Length、@Range 注解，
     * 返回所有不通过的错误信息，格式为 "字段名" + 注解的 message，例如 "userName不能为空"。
     * 全部通过返回空 List。
     *
     * <p>规则：
     * <ul>
     *   <li>@NotNull：值为 null 不通过</li>
     *   <li>@NotBlank：值为 null，或者是空白字符串不通过</li>
     *   <li>@Length：值为 String 且长度不在 [min, max] 不通过；null 跳过</li>
     *   <li>@Range：值为 Number 且不在 [min, max] 不通过；null 跳过。
     *       提示：统一转成 BigDecimal 比较：new BigDecimal(value.toString())</li>
     * </ul>
     * 一个字段上可能有多个注解，每个不通过的注解都产生一条错误。
     *
     * <p>提示：field.getAnnotation(NotBlank.class) 拿到注解对象（没有则为 null），然后调用它的 message() 方法。
     */
    public static List<String> validate(Object obj) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 校验不通过时抛出 IllegalArgumentException，消息是所有错误用 "; " 连接起来。
     * 提示：String.join("; ", list)
     */
    public static void validateOrThrow(Object obj) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
