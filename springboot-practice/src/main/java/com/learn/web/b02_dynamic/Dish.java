package com.learn.web.b02_dynamic;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品（已写好），对应表 dish。
 */
@Data
public class Dish {

    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    /** 1 起售 0 停售 */
    private Integer status;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
