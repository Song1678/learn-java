package com.learn.ch05_annotation.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导出列（已写好）。RuoYi-Vue-Plus 导出 Excel 用的 @ExcelProperty 就是这种注解。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {

    /** 表头名称 */
    String name();

    /** 列顺序，越小越靠前 */
    int order() default 0;
}
