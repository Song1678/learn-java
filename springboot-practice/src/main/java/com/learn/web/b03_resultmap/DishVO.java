package com.learn.web.b03_resultmap;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 返回给前端的菜品信息（已写好）：除了 dish 表的字段，还有分类名称（来自 category 表）和口味列表（来自 dish_flavor 表）。
 *
 * <p>VO = View Object，专门用来“展示”的对象，字段可以来自多张表。RuoYi-Vue-Plus 里叫 XxxVo。
 */
@Data
public class DishVO {

    private Long id;
    private String name;
    private Long categoryId;
    /** 来自 category.name */
    private String categoryName;
    private BigDecimal price;
    private Integer status;
    /** 来自 dish_flavor，一个菜品有多个口味（一对多） */
    private List<FlavorVO> flavors = new ArrayList<>();
}
