package com.learn.ch09_takeout.dto;

import java.math.BigDecimal;

/**
 * 新增菜品时前端传来的数据（已写好）。
 *
 * <p>DTO（Data Transfer Object）：接收前端参数用；Entity：对应数据库表；VO：返回给前端用。
 * 三者分开是企业项目的惯例，RuoYi-Vue-Plus 里叫 Bo / Entity / Vo。
 */
public record DishDTO(String name, Long categoryId, BigDecimal price, String description) {
}
