package com.learn.ch02_oop.pay;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 折扣支付：例如 new DiscountPay(new BigDecimal("0.85")) 表示 85 折。
 * 名称为 "折扣"。折扣率必须在 (0, 1] 之间，否则抛 IllegalArgumentException。
 * <pre>
 * 100.00 打 85 折 -> 85.00
 * 33.33  打 88 折 -> 29.33   （29.3304 四舍五入）
 * </pre>
 */
public class DiscountPay implements PayStrategy {

    // TODO 声明 rate 字段

    public DiscountPay(BigDecimal rate) {
        // TODO 提示：BigDecimal 比较大小用 compareTo，不要用 equals（equals 会比较精度：1.0 和 1.00 不相等）
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public BigDecimal pay(BigDecimal amount) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public String name() {
        return "折扣";
    }
}
