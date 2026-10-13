package com.learn.ch03_exception;

import java.math.BigDecimal;

/**
 * 练习 3（续）：异常转换。
 *
 * <p>上层（比如 Controller）不想处理一堆不同的受检异常，
 * 所以业务层经常把受检异常 catch 住，转换成统一的 ServiceException 再抛出去。
 */
public class TransferService {

    /** 余额不足时使用的错误码 */
    public static final int CODE_INSUFFICIENT = 400;

    /**
     * 从 from 转账 amount 到 to。
     * 余额不足时：catch 住 InsufficientBalanceException，抛出
     * new ServiceException(原异常的 message, CODE_INSUFFICIENT, 原异常)。
     * 转账失败时两个账户余额都不能变。
     */
    public void transfer(Account from, Account to, BigDecimal amount) {
        // TODO 提示：先取款成功，再存款
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
