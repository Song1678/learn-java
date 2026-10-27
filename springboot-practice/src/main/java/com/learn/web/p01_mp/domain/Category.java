package com.learn.web.p01_mp.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * p01 练习 1.1：实体类映射
 *
 * <p>MyBatis-Plus 根据实体类自动生成 SQL，所以要告诉它：
 * <ul>
 *   <li>{@code @TableName("category")}：对应哪张表（类名 Category 转下划线刚好是 category，可以省略，但建议写上，RuoYi 里都写了）</li>
 *   <li>{@code @TableId(type = IdType.AUTO)}：哪个字段是主键、主键怎么生成。AUTO 表示用数据库的自增。
 *       不写的话 MyBatis-Plus 默认用“雪花算法”生成一个 19 位的 id（RuoYi-Vue-Plus 就是用的雪花算法，表的主键不自增）</li>
 * </ul>
 * 其他字段默认按“驼峰转下划线”对应列名，不用写注解。
 */
@Data
@TableName("category")
public class Category {

    // TODO 1.1 主键用数据库自增
    private Long id;
    private Integer type;
    private String name;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
