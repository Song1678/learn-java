package com.learn.web.b02_dynamic;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 菜品查询条件（已写好）。所有字段都可以为 null，为 null 表示不按这个条件过滤。
 */
@Data
public class DishQuery {

    /** 名称包含（模糊查询），null 或空字符串都表示不过滤 */
    private String name;
    private Long categoryId;
    private Integer status;
    /** 价格 &gt;= minPrice */
    private BigDecimal minPrice;
    /** 价格 &lt;= maxPrice */
    private BigDecimal maxPrice;
}
