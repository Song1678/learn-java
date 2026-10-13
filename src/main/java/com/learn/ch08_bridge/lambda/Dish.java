package com.learn.ch08_bridge.lambda;

import java.math.BigDecimal;

/**
 * 菜品（已写好）。
 *
 * @param name     名称
 * @param category 分类
 * @param price    价格
 * @param sales    月销量
 * @param onSale   是否起售
 */
public record Dish(String name, String category, BigDecimal price, int sales, boolean onSale) {
}
