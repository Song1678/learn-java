package com.learn.web.w02_rest.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 新增 / 修改菜品时，前端在请求体里传来的 JSON（已写好）：
 * <pre>{"id": 1, "name": "宫保鸡丁", "categoryId": 1, "price": 30.00, "description": "..."}</pre>
 *
 * <p>Bo = Business Object，RuoYi-Vue-Plus 里接收参数的对象都叫 XxxBo（ch09 里叫 DishDTO）。
 */
@Data
public class DishBo {

    /** 修改时必传，新增时不传 */
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private String description;
}
