package com.learn.w03_ioc.pay;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 3.2 打 88 折。计算逻辑已写好，你要做的是把它注册成 Spring Bean，名字叫 discount（参考 NormalPay）。
 */
// TODO 3.2 注册成 bean，名字叫 discount
public class DiscountPay implements PayStrategy {

    @Override
    public BigDecimal pay(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.88")).setScale(2, RoundingMode.HALF_UP);
    }
}
