package com.learn.w03_ioc.pay;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 3.2 满 100 减 20。计算逻辑已写好，把它注册成 Spring Bean，名字叫 fullReduction。
 */
// TODO 3.2 注册成 bean，名字叫 fullReduction
public class FullReductionPay implements PayStrategy {

    private static final BigDecimal THRESHOLD = new BigDecimal("100");
    private static final BigDecimal REDUCTION = new BigDecimal("20");

    @Override
    public BigDecimal pay(BigDecimal amount) {
        return amount.compareTo(THRESHOLD) >= 0 ? amount.subtract(REDUCTION) : amount;
    }
}
