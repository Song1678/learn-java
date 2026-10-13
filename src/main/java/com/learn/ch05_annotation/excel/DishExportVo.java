package com.learn.ch05_annotation.excel;

import java.math.BigDecimal;

/**
 * 菜品导出对象（已写好）。
 */
public class DishExportVo {

    @ExcelColumn(name = "价格", order = 3)
    private BigDecimal price;

    @ExcelColumn(name = "菜品名称", order = 1)
    private String name;

    /** 没有 @ExcelColumn，不导出 */
    private Long id;

    @ExcelColumn(name = "分类", order = 2)
    private String categoryName;

    public DishExportVo(Long id, String name, String categoryName, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.price = price;
    }
}
