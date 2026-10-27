package com.learn.web.p01_mp.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * p01 练习 1.2：找 bug
 *
 * <p>categoryName 是为了方便返回给前端加的字段，dish 表里并没有这一列。
 * 但 MyBatis-Plus 默认认为每个字段都对应一列，生成的 SELECT 里会带上 category_name，于是报错：
 * {@code Unknown column 'category_name' in 'field list'}。
 * 用 {@code @TableField(exist = false)} 告诉它“这个字段不是表里的列”。
 */
@Data
@TableName("dish")
public class Dish {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private Integer status;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** 分类名称：不是 dish 表的列 */
    // @hide 1.2 加上注解，告诉 MyBatis-Plus 这不是表里的列
    @TableField(exist = false)
    // @end
    private String categoryName;
}
