package com.learn.ch03_exception;

import java.math.BigDecimal;

/**
 * 练习 3（续）：账户
 */
public class Account {

    private final String owner;
    private BigDecimal balance;

    public Account(String owner, BigDecimal balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public String getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 存款。amount 必须大于 0，否则抛 IllegalArgumentException("金额必须大于0")。
     */
    public void deposit(BigDecimal amount) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 取款。amount 必须大于 0，否则抛 IllegalArgumentException("金额必须大于0")；
     * 余额不足时抛出受检异常 InsufficientBalanceException，并且余额保持不变。
     */
    public void withdraw(BigDecimal amount) throws InsufficientBalanceException {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
    // TODO 可以自己抽一个 private 方法来校验金额，避免重复代码
}
