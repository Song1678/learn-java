package com.learn.web.w02_rest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品（已写好，除了 2.8）。
 *
 * <p>{@code @Data}：Lombok 在编译时自动生成 getter / setter / equals / hashCode / toString。
 * 在 IDEA 里按 Ctrl+F12（Mac：Cmd+F12）可以看到生成出来的方法。
 */
@Data
public class Dish {

    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    /** 1 起售 0 停售 */
    private Integer status;
    private String description;

    /**
     * 2.8 默认情况下 LocalDateTime 转成 JSON 是 "2024-04-05T10:10:00"（ISO 格式，中间有个 T）。
     * 加一个注解，让它变成 "2024-04-05 10:10:00"。
     */
    // TODO 2.8 加上 @JsonFormat 注解
    private LocalDateTime createTime;
}
