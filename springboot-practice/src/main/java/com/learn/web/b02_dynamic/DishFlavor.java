package com.learn.web.b02_dynamic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜品口味（已写好），对应表 dish_flavor。例如 name=辣度，value=不辣,微辣,中辣
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor {

    private Long id;
    private Long dishId;
    private String name;
    private String value;
}
