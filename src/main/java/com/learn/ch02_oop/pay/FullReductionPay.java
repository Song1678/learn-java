package com.learn.ch02_oop.pay;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 满减支付：满 threshold 减 reduction，例如 满 50 减 10。名称为 "满减"。
 * <pre>
 * 满50减10： 49.90 -> 49.90， 50.00 -> 40.00， 88.88 -> 78.88
 * </pre>
 */
public class FullReductionPay implements PayStrategy {

    // TODO 声明 threshold、reduction 字段

    public FullReductionPay(BigDecimal threshold, BigDecimal reduction) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public BigDecimal pay(BigDecimal amount) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public String name() {
        return "满减";
    }
}
