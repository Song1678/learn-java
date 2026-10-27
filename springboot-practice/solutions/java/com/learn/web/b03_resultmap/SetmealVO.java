package com.learn.web.b03_resultmap;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 套餐和它包含的菜品（已写好）
 */
@Data
public class SetmealVO {

    private Long id;
    private String name;
    private BigDecimal price;
    private List<SetmealDishVO> dishes = new ArrayList<>();

    @Data
    public static class SetmealDishVO {
        /** 来自 dish.name */
        private String dishName;
        /** 来自 setmeal_dish.copies */
        private Integer copies;
    }
}
