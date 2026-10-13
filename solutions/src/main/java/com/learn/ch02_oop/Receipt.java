package com.learn.ch02_oop;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 练习 5（续）：StringBuilder 拼接小票。
 */
public class Receipt {

    private Receipt() {
    }

    /**
     * 5.3 生成小票文本。每个菜一行，格式为 "菜名 x数量 = 小计"，最后一行是 "合计: 总价"。
     * 行之间用 '\n' 分隔，末尾没有换行。
     * <pre>
     * 宫保鸡丁 x2 = 56.00
     * 米饭 x3 = 6.00
     * 合计: 62.00
     * </pre>
     * 提示：BigDecimal 的 toString() 会保留原来的精度（new BigDecimal("28.00") 打印 28.00）。
     * 小计和合计都用 setScale(2, RoundingMode.HALF_UP) 统一成 2 位小数再输出。
     */
    public static String build(CartItem... items) {
        // @answer
        StringBuilder sb = new StringBuilder();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            BigDecimal subtotal = item.subtotal().setScale(2, RoundingMode.HALF_UP);
            sb.append(item.name()).append(" x").append(item.quantity())
                    .append(" = ").append(subtotal).append('\n');
            total = total.add(subtotal);
        }
        sb.append("合计: ").append(total.setScale(2, RoundingMode.HALF_UP));
        return sb.toString();
        // @end
    }
}
