package com.learn.ch09_takeout.domain;

/**
 * 订单状态（已写好，和第 2 章的一样）。
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

    public boolean canCancel() {
        return this == PENDING_PAYMENT || this == TO_BE_CONFIRMED;
    }
}
