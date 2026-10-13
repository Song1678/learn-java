package com.learn.ch02_oop;

/**
 * 练习 4：枚举（对应「核心类 - 枚举类」）
 *
 * <p>外卖订单状态，和苍穹外卖里的订单状态码一致：
 * 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
 *
 * <p>枚举也可以有字段、构造方法和普通方法。数据库里存 code（数字），代码里用枚举，可读性更好。
 */
public enum OrderStatus {

    PENDING_PAYMENT(1, "待付款"),
    TO_BE_CONFIRMED(2, "待接单"),
    CONFIRMED(3, "已接单"),
    DELIVERY_IN_PROGRESS(4, "派送中"),
    COMPLETED(5, "已完成"),
    CANCELLED(6, "已取消");

    private final int code;
    private final String label;

    OrderStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    /**
     * 4.1 根据 code 找到枚举；找不到抛 IllegalArgumentException。
     * 提示：values() 返回所有枚举值的数组。
     */
    public static OrderStatus fromCode(int code) {
        // @answer
        for (OrderStatus s : values()) {
            if (s.code == code) {
                return s;
            }
        }
        throw new IllegalArgumentException("未知的订单状态: " + code);
        // @end
    }

    /**
     * 4.2 是否允许取消：只有 待付款、待接单 的订单可以取消。
     */
    public boolean canCancel() {
        // @answer 提示：枚举可以直接用 == 比较，也可以用 switch
        return this == PENDING_PAYMENT || this == TO_BE_CONFIRMED;
        // @end
    }

    /**
     * 4.3 订单流转的下一个状态：
     * 待付款 -> 待接单 -> 已接单 -> 派送中 -> 已完成。
     * 已完成、已取消 没有下一个状态，抛出 IllegalStateException。
     */
    public OrderStatus next() {
        // @answer
        return switch (this) {
            case PENDING_PAYMENT -> TO_BE_CONFIRMED;
            case TO_BE_CONFIRMED -> CONFIRMED;
            case CONFIRMED -> DELIVERY_IN_PROGRESS;
            case DELIVERY_IN_PROGRESS -> COMPLETED;
            case COMPLETED, CANCELLED -> throw new IllegalStateException(label + " 状态不能再流转");
        };
        // @end
    }
}
