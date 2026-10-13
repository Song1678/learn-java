package com.learn.ch05_annotation.validation;

import java.math.BigDecimal;

/**
 * 新增菜品的表单（已写好）。看看注解是怎么用的。
 */
public class DishForm {

    @NotBlank(message = "不能为空")
    @Length(max = 20, message = "长度不能超过20")
    private String name;

    @NotNull(message = "不能为空")
    @Range(min = 0, max = 9999, message = "必须在0到9999之间")
    private BigDecimal price;

    @NotNull
    private Long categoryId;

    @Range(min = 0, max = 1, message = "只能是0或1")
    private Integer status;

    /** 没有注解，不校验 */
    private String description;

    public DishForm(String name, BigDecimal price, Long categoryId, Integer status, String description) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.status = status;
        this.description = description;
    }
}
