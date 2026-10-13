package com.learn.ch02_oop.pay;

import java.math.BigDecimal;

/**
 * 练习 3：接口 + BigDecimal（对应「接口」「核心类 - BigDecimal」）
 *
 * <p>支付策略接口：输入订单原价，返回实际支付金额。
 *
 * <p><b>为什么用 BigDecimal？</b> 0.1 + 0.2 在 double 里等于 0.30000000000000004，
 * 涉及钱的计算在企业项目里一律用 BigDecimal（苍穹外卖、RuoYi 都是）。
 */
public interface PayStrategy {

    /**
     * 计算实际支付金额，结果保留 2 位小数（RoundingMode.HALF_UP 四舍五入）。
     */
    BigDecimal pay(BigDecimal amount);

    /**
     * 支付方式名称。接口可以有 default 方法 —— 实现类可以不重写。
     */
    default String name() {
        return getClass().getSimpleName();
    }
}
