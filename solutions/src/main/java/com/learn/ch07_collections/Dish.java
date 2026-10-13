package com.learn.ch07_collections;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 练习 3：equals 和 hashCode（对应「集合 - 编写equals方法 / 编写equals和hashCode」）
 *
 * <p>HashSet / HashMap 判断“两个对象是不是同一个 key”时：先比 hashCode，再比 equals。
 * 不重写的话，默认比较的是内存地址，两个内容一样的 new Dish(...) 会被当成不同的元素。
 *
 * <p>要求：name 相同并且 price 数值相等（28 和 28.00 也算相等）就认为是同一道菜。
 */
public class Dish {

    private final String name;
    private final BigDecimal price;

    public Dish(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        // @answer 提示：BigDecimal 的 equals 会比较精度（28 不等于 28.00），数值比较要用 compareTo
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dish other)) {
            return false;
        }
        return Objects.equals(name, other.name) && price.compareTo(other.price) == 0;
        // @end
    }

    /**
     * 规则：equals 相等的两个对象，hashCode 必须相等。
     * 提示：price.stripTrailingZeros() 可以把 28.00 变成 28，再参与 Objects.hash(...)
     */
    @Override
    public int hashCode() {
        // @answer
        return Objects.hash(name, price.stripTrailingZeros());
        // @end
    }
}
