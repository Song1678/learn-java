package com.learn.w03_ioc.pay;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 原价支付（已写好）。
 *
 * <p>{@code @Component("normal")}：注册成 Spring Bean，bean 的名字叫 normal。
 * 不写名字的话，默认名字是类名首字母小写：normalPay。
 */
@Component("normal")
public class NormalPay implements PayStrategy {

    @Override
    public BigDecimal pay(BigDecimal amount) {
        return amount;
    }
}
