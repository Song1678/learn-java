package com.learn.ch02_oop.pay;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 原价支付，名称为 "原价"。
 */
public class NormalPay implements PayStrategy {

    @Override
    public BigDecimal pay(BigDecimal amount) {
        // TODO 提示：amount.setScale(2, RoundingMode.HALF_UP)
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public String name() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
