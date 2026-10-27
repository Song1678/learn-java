package com.learn.web.p03_page;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * p03 练习 3.3 ~ 3.4：给字段加注解
 */
@Data
@TableName("dish")
public class Dish {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private Integer status;
    private String description;

    /** 3.3 插入时自动填充 */
    // TODO 3.3 加上 @TableField(fill = ...)
    private LocalDateTime createTime;

    /** 3.3 插入和更新时都自动填充 */
    // TODO 3.3 加上 @TableField(fill = ...)
    private LocalDateTime updateTime;

    /**
     * 3.4 逻辑删除标志：0 正常，1 已删除。加上 {@code @TableLogic} 之后：
     * <ul>
     *   <li>deleteById 不再执行 DELETE，而是 UPDATE dish SET del_flag = 1 WHERE id = ? AND del_flag = 0</li>
     *   <li>所有查询自动加上 WHERE del_flag = 0</li>
     * </ul>
     * 数据删了还能找回来，企业项目里很常用（RuoYi-Vue-Plus 的用户、部门等表都有 del_flag）。
     */
    // TODO 3.4 加上 @TableLogic
    private Integer delFlag;
}
