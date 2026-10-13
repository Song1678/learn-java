package com.learn.ch02_oop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch02 练习4：枚举")
class OrderStatusTest {

    @Test
    @DisplayName("4.1 fromCode")
    void fromCode() {
        assertEquals(OrderStatus.PENDING_PAYMENT, OrderStatus.fromCode(1));
        assertEquals(OrderStatus.CANCELLED, OrderStatus.fromCode(6));
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.fromCode(99));
    }

    @Test
    @DisplayName("4.2 canCancel")
    void canCancel() {
        assertTrue(OrderStatus.PENDING_PAYMENT.canCancel());
        assertTrue(OrderStatus.TO_BE_CONFIRMED.canCancel());
        assertFalse(OrderStatus.CONFIRMED.canCancel());
        assertFalse(OrderStatus.COMPLETED.canCancel());
        assertFalse(OrderStatus.CANCELLED.canCancel());
    }

    @Test
    @DisplayName("4.3 next 状态流转")
    void next() {
        OrderStatus s = OrderStatus.PENDING_PAYMENT;
        s = s.next();
        assertEquals(OrderStatus.TO_BE_CONFIRMED, s);
        s = s.next().next().next();
        assertEquals(OrderStatus.COMPLETED, s);
        assertThrows(IllegalStateException.class, OrderStatus.COMPLETED::next);
        assertThrows(IllegalStateException.class, OrderStatus.CANCELLED::next);
    }
}
