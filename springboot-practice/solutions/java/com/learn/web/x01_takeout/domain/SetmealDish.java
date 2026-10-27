package com.learn.web.x01_takeout.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 套餐包含的菜品（已写好）
 */
@Data
@TableName("setmeal_dish")
public class SetmealDish {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long setmealId;
    private Long dishId;
    private Integer copies;
}
