package com.learn.ch02_oop;

import java.math.BigDecimal;

/**
 * 练习 5：record（对应「核心类 - 记录类」）
 *
 * <p>record 是 Java 16+ 的不可变数据类，自动生成构造方法、getter（name() 而不是 getName()）、
 * equals、hashCode、toString。相当于 JS 里一个被 Object.freeze 的对象。
 *
 * @param name     菜品名
 * @param price    单价
 * @param quantity 数量
 */
public record CartItem(String name, BigDecimal price, int quantity) {

    /**
     * 5.1 紧凑构造方法（compact constructor）：用来做参数校验。
     * 要求：name 不能为 null 或空白；price 不能为 null 且不能小于 0；quantity 必须 >= 1。
     * 不满足时抛出 IllegalArgumentException。
     */
    public CartItem {
        // @answer 提示：String.isBlank()
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("菜品名不能为空");
        }
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("价格不合法");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("数量至少为 1");
        }
        // @end
    }

    /**
     * 5.2 小计 = 单价 * 数量
     */
    public BigDecimal subtotal() {
        // @answer
        return price.multiply(BigDecimal.valueOf(quantity));
        // @end
    }
}
