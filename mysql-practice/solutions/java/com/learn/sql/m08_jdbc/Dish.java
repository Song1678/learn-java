package com.learn.sql.m08_jdbc;

import java.math.BigDecimal;

/**
 * 菜品实体（已写好）。数据库列 category_id 对应 Java 字段 categoryId。
 */
public record Dish(Long id, String name, Long categoryId, BigDecimal price, Integer status, String description) {
}
