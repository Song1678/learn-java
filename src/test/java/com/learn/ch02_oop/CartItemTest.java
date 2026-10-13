package com.learn.ch02_oop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch02 练习5：record 与 StringBuilder")
class CartItemTest {

    @Test
    @DisplayName("5.1 紧凑构造方法校验")
    void validate() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(" ", BigDecimal.ONE, 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem(null, BigDecimal.ONE, 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem("米饭", new BigDecimal("-1"), 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem("米饭", null, 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem("米饭", BigDecimal.ONE, 0));
    }

    @Test
    @DisplayName("5.2 小计 & record 自动生成 equals")
    void subtotal() {
        CartItem item = new CartItem("宫保鸡丁", new BigDecimal("28.00"), 2);
        assertEquals(0, new BigDecimal("56").compareTo(item.subtotal()));
        assertEquals("宫保鸡丁", item.name());
        assertEquals(new CartItem("宫保鸡丁", new BigDecimal("28.00"), 2), item);
    }

    @Test
    @DisplayName("5.3 生成小票")
    void receipt() {
        String text = Receipt.build(
                new CartItem("宫保鸡丁", new BigDecimal("28"), 2),
                new CartItem("米饭", new BigDecimal("2.00"), 3));
        assertEquals("宫保鸡丁 x2 = 56.00\n米饭 x3 = 6.00\n合计: 62.00", text);
        assertEquals("合计: 0.00", Receipt.build());
    }
}
