package com.learn.ch03_exception;

import java.math.BigDecimal;

/**
 * 练习 3：受检异常（checked exception）
 *
 * <p>继承 Exception（而不是 RuntimeException）的异常是受检异常：
 * 抛出它的方法必须写 throws 声明，调用方必须 try/catch 或者继续 throws。
 *
 * <p>消息格式："余额不足，需要 xx，可用 yy"
 */
public class InsufficientBalanceException extends Exception {

    // @hide 声明 required、available 两个 BigDecimal 字段
    private final BigDecimal required;
    private final BigDecimal available;
    // @end

    public InsufficientBalanceException(BigDecimal required, BigDecimal available) {
        // @answer
        super("余额不足，需要 " + required + "，可用 " + available);
        this.required = required;
        this.available = available;
        // @end
    }

    public BigDecimal getRequired() {
        // @answer
        return required;
        // @end
    }

    public BigDecimal getAvailable() {
        // @answer
        return available;
        // @end
    }
}
