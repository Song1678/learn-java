package com.learn.web.x01_takeout.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 菜品口味（已写好）
 */
@Data
@TableName("dish_flavor")
public class DishFlavor {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dishId;
    private String name;
    private String value;
}
