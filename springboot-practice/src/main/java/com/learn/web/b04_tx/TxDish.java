package com.learn.web.b04_tx;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 新增菜品的参数 + 插入时回填的 id（已写好）
 */
@Data
public class TxDish {

    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private Integer status;
    private List<Flavor> flavors = new ArrayList<>();

    public record Flavor(String name, String value) {
    }
}
