package com.learn.web.common;

/**
 * 状态常量（已写好）。菜品、分类、套餐都用 1 表示起售/启用，0 表示停售/禁用。
 */
public final class StatusConstant {

    public static final int ENABLE = 1;
    public static final int DISABLE = 0;

    private StatusConstant() {
    }
}
