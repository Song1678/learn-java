package com.learn.ch09_takeout.domain;

import java.math.BigDecimal;

/**
 * 菜品（已写好）。对应数据库表 dish。
 */
public class Dish extends BaseEntity {

    private String name;
    private Long categoryId;
    private BigDecimal price;
    /** 1 起售 0 停售 */
    private Integer status;
    private String description;

    public Dish() {
    }

    public Dish(String name, Long categoryId, BigDecimal price, Integer status) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Dish{id=" + getId() + ", name=" + name + ", price=" + price + ", status=" + status + "}";
    }
}
