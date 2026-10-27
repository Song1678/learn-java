package com.learn.web.b01_mybatis;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类（已写好），对应表 category。
 *
 * <p>字段名是驼峰（createTime），列名是下划线（create_time）。application.yml 里开了
 * map-underscore-to-camel-case，MyBatis 会自动对应，SELECT * 就能把每一列装进同名字段。
 */
@Data
public class Category {

    private Long id;
    /** 1 菜品分类 2 套餐分类 */
    private Integer type;
    private String name;
    private Integer sort;
    /** 1 启用 0 禁用 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
