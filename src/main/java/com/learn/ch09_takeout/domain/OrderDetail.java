package com.learn.ch09_takeout.domain;

import java.math.BigDecimal;

/**
 * 订单明细（已写好）。真实项目里是单独一张 order_detail 表，这里简化成订单里的一个 List。
 *
 * @param dishId 菜品 id
 * @param name   菜名
 * @param amount 单价
 * @param number 数量
 */
public record OrderDetail(Long dishId, String name, BigDecimal amount, int number) {
}
