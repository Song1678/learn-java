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

    // @hide 声明 threshold、reduction 字段
    private final BigDecimal threshold;
    private final BigDecimal reduction;
    // @end

    public FullReductionPay(BigDecimal threshold, BigDecimal reduction) {
        // @answer
        this.threshold = threshold;
        this.reduction = reduction;
        // @end
    }

    @Override
    public BigDecimal pay(BigDecimal amount) {
        // @answer
        BigDecimal result = amount.compareTo(threshold) >= 0 ? amount.subtract(reduction) : amount;
        return result.setScale(2, RoundingMode.HALF_UP);
        // @end
    }

    @Override
    public String name() {
        return "满减";
    }
}
