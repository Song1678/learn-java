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

    // @hide 声明 rate 字段
    private final BigDecimal rate;
    // @end

    public DiscountPay(BigDecimal rate) {
        // @answer 提示：BigDecimal 比较大小用 compareTo，不要用 equals（equals 会比较精度：1.0 和 1.00 不相等）
        if (rate.compareTo(BigDecimal.ZERO) <= 0 || rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("折扣率必须在 (0, 1] 之间");
        }
        this.rate = rate;
        // @end
    }

    @Override
    public BigDecimal pay(BigDecimal amount) {
        // @answer
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        // @end
    }

    @Override
    public String name() {
        return "折扣";
    }
}
