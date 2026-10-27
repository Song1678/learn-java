package com.learn.web.p02_wrapper;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询条件（已写好，和 b02 的一样）。所有字段都可以为 null，为 null（或空字符串）表示不按这个条件过滤。
 */
@Data
public class DishQuery {

    private String name;
    private Long categoryId;
    private Integer status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
