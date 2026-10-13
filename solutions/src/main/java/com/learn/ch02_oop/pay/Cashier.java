package com.learn.ch02_oop.pay;

import java.math.BigDecimal;

/**
 * 收银台：面向接口编程 —— 只依赖 PayStrategy 接口，不关心具体是哪种实现。
 * （Spring 里的依赖注入就是建立在这种思想上的）
 */
public class Cashier {

    private Cashier() {
    }

    /**
     * 3.x 从多种支付方式里选出最省钱的那个，返回它的实际支付金额。
     * strategies 至少有一个元素。
     */
    public static BigDecimal bestPrice(BigDecimal amount, PayStrategy... strategies) {
        // @answer 提示：可变参数 strategies 在方法内部就是一个数组
        BigDecimal best = null;
        for (PayStrategy s : strategies) {
            BigDecimal price = s.pay(amount);
            if (best == null || price.compareTo(best) < 0) {
                best = price;
            }
        }
        return best;
        // @end
    }
}
