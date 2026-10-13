package com.learn.ch09_takeout.domain;

import java.math.BigDecimal;

/**
 * 购物车中的一项（已写好）。对应数据库表 shopping_cart：每个用户的每道菜一行。
 */
public class ShoppingCart extends BaseEntity {

    private Long userId;
    private Long dishId;
    /** 冗余菜名，避免每次都查 dish 表 */
    private String name;
    /** 单价 */
    private BigDecimal amount;
    /** 数量 */
    private Integer number;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return name + " x" + number;
    }
}
