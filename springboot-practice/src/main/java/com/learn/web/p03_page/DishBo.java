package com.learn.web.p03_page;

import lombok.Data;

/**
 * 分页查询条件（已写好）
 */
@Data
public class DishBo {

    /** 名称模糊查询 */
    private String name;
    private Long categoryId;
    private Integer status;
}
