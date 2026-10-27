package com.learn.web.w02_rest.domain;

import lombok.Data;

/**
 * 查询条件（已写好）。GET /w02/dishes?name=鸡&amp;categoryId=1&amp;status=1
 *
 * <p>Controller 方法参数直接写 DishQuery query（不加注解），Spring 会把同名的 URL 参数一个个 set 进来。
 * RuoYi-Vue-Plus 的列表接口 list(XxxBo bo, PageQuery pageQuery) 就是这么接收查询条件的。
 */
@Data
public class DishQuery {

    /** 名称包含（模糊查询） */
    private String name;
    private Long categoryId;
    private Integer status;
}
