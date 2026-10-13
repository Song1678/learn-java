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

    // @hide 选一个合适的 Map 存放购物车项（提示：要保持添加顺序）
    private final Map<String, Item> items = new LinkedHashMap<>();
    // @end

    /**
     * 加入一份菜。已经在购物车里的菜数量 +1（价格以第一次加入时为准），否则新增一项，数量为 1。
     */
    public void add(String name, BigDecimal price) {
        // @answer 提示：record 是不可变的，数量变了就 new 一个新的 Item 放回去
        Item old = items.get(name);
        if (old == null) {
            items.put(name, new Item(name, price, 1));
        } else {
            items.put(name, new Item(name, old.price(), old.quantity() + 1));
        }
        // @end
    }

    /**
     * 减少一份。数量减到 0 时从购物车移除；不存在的菜什么都不做。
     */
    public void sub(String name) {
        // @answer
        Item old = items.get(name);
        if (old == null) {
            return;
        }
        if (old.quantity() <= 1) {
            items.remove(name);
        } else {
            items.put(name, new Item(name, old.price(), old.quantity() - 1));
        }
        // @end
    }

    /** 某道菜的数量，不存在返回 0 */
    public int quantityOf(String name) {
        // @answer
        Item item = items.get(name);
        return item == null ? 0 : item.quantity();
        // @end
    }

    /** 按加入顺序返回所有项（返回拷贝） */
    public List<Item> list() {
        // @answer
        return new ArrayList<>(items.values());
        // @end
    }

    /** 总价 */
    public BigDecimal total() {
        // @answer
        BigDecimal total = BigDecimal.ZERO;
        for (Item item : items.values()) {
            total = total.add(item.subtotal());
        }
        return total;
        // @end
    }

    public void clear() {
        // @answer
        items.clear();
        // @end
    }
}
