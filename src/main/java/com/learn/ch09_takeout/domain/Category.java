package com.learn.ch09_takeout.domain;

/**
 * 菜品分类（已写好）。对应数据库表 category。
 */
public class Category extends BaseEntity {

    private String name;
    /** 排序，越小越靠前 */
    private Integer sort;
    /** 1 启用 0 禁用 */
    private Integer status;

    public Category() {
    }

    public Category(String name, Integer sort, Integer status) {
        this.name = name;
        this.sort = sort;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Category{id=" + getId() + ", name=" + name + "}";
    }
}
