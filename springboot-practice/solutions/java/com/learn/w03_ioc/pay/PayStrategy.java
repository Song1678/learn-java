package com.learn.w03_ioc.pay;

import java.math.BigDecimal;

/**
 * 支付策略（已写好，和 ch02 的 PayStrategy 一样）。
 */
public interface PayStrategy {

    /** 计算实际要付的钱 */
    BigDecimal pay(BigDecimal amount);
}
