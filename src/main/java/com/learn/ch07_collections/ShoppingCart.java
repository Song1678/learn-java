package com.learn.ch07_collections;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 练习 4：购物车（苍穹外卖里“购物车”功能的内存版）
 */
public class ShoppingCart {

    /**
     * 购物车里的一项（已写好）。
     */
    public record Item(String name, BigDecimal price, int quantity) {

        public BigDecimal subtotal() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    // TODO 选一个合适的 Map 存放购物车项（提示：要保持添加顺序）

    /**
     * 加入一份菜。已经在购物车里的菜数量 +1（价格以第一次加入时为准），否则新增一项，数量为 1。
     */
    public void add(String name, BigDecimal price) {
        // TODO 提示：record 是不可变的，数量变了就 new 一个新的 Item 放回去
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 减少一份。数量减到 0 时从购物车移除；不存在的菜什么都不做。
     */
    public void sub(String name) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 某道菜的数量，不存在返回 0 */
    public int quantityOf(String name) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 按加入顺序返回所有项（返回拷贝） */
    public List<Item> list() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 总价 */
    public BigDecimal total() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public void clear() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
