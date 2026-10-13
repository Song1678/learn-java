package com.learn.ch02_oop.pay;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 原价支付，名称为 "原价"。
 */
public class NormalPay implements PayStrategy {

    @Override
    public BigDecimal pay(BigDecimal amount) {
        // @answer 提示：amount.setScale(2, RoundingMode.HALF_UP)
        return amount.setScale(2, RoundingMode.HALF_UP);
        // @end
    }

    @Override
    public String name() {
        // @answer
        return "原价";
        // @end
    }
}
