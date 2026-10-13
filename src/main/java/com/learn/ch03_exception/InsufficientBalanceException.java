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

    // TODO 声明 required、available 两个 BigDecimal 字段

    public InsufficientBalanceException(BigDecimal required, BigDecimal available) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public BigDecimal getRequired() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public BigDecimal getAvailable() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
