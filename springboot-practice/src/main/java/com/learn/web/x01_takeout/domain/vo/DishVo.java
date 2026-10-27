package com.learn.web.x01_takeout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learn.web.x01_takeout.domain.DishFlavor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜品视图对象（已写好）：返回给前端的菜品信息
 */
@Data
public class DishVo {

    private Long id;
    private String name;
    private Long categoryId;
    /** 分类名称（来自 category 表） */
    private String categoryName;
    private BigDecimal price;
    private Integer status;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /** 口味（列表接口不用填，详情接口要填） */
    private List<DishFlavor> flavors = new ArrayList<>();
}
