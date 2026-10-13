package com.learn.ch02_oop.pay;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch02 练习3：接口与 BigDecimal")
class PayStrategyTest {

    private static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    @Test
    @DisplayName("原价")
    void normal() {
        PayStrategy p = new NormalPay();
        assertEquals(bd("12.30"), p.pay(bd("12.3")));
        assertEquals("原价", p.name());
    }

    @Test
    @DisplayName("折扣")
    void discount() {
        PayStrategy p = new DiscountPay(bd("0.85"));
        assertEquals(bd("85.00"), p.pay(bd("100")));
        assertEquals(bd("29.33"), new DiscountPay(bd("0.88")).pay(bd("33.33")));
        assertEquals("折扣", p.name());
        assertThrows(IllegalArgumentException.class, () -> new DiscountPay(bd("0")));
        assertThrows(IllegalArgumentException.class, () -> new DiscountPay(bd("1.01")));
        assertDoesNotThrow(() -> new DiscountPay(bd("1.00")));
    }

    @Test
    @DisplayName("满减")
    void fullReduction() {
        PayStrategy p = new FullReductionPay(bd("50"), bd("10"));
        assertEquals(bd("49.90"), p.pay(bd("49.9")));
        assertEquals(bd("40.00"), p.pay(bd("50")));
        assertEquals(bd("78.88"), p.pay(bd("88.88")));
    }

    @Test
    @DisplayName("收银台选出最优价")
    void cashier() {
        BigDecimal price = Cashier.bestPrice(bd("60"),
                new NormalPay(), new DiscountPay(bd("0.9")), new FullReductionPay(bd("50"), bd("10")));
        assertEquals(bd("50.00"), price);
        assertEquals(bd("60.00"), Cashier.bestPrice(bd("60"), new NormalPay()));
    }
}
