package com.learn.web.b02_dynamic;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * b02 练习：XML 映射文件 + 动态 SQL
 *
 * <p>接口已经写好了，SQL 写在 src/main/resources/mapper/b02/DishMapper.xml 里：
 * XML 的 namespace = 本接口的全类名，每个语句的 id = 方法名，MyBatis 靠这两个把它们对应起来。
 *
 * <p>为什么要用 XML？注解里写长 SQL、写 if 判断都很难看，复杂 SQL 一般写在 XML 里。
 * RuoYi-Vue-Plus 简单的 CRUD 用 MyBatis-Plus，复杂查询就写在 resources/mapper/*.xml 里。
 */
@Mapper
public interface DishMapper {

    /** 2.1 条件查询，按 id 升序（见 XML 里的说明） */
    List<Dish> selectList(DishQuery query);

    /** 2.2 条件计数，条件和 selectList 完全一样 */
    long selectCount(DishQuery query);

    /** 2.3 只更新不为 null 的字段 */
    int updateSelective(Dish dish);

    /** 2.4 根据多个 id 批量查询，按 id 升序 */
    List<Dish> selectByIds(@Param("ids") List<Long> ids);

    /** 2.5 批量删除，返回删除的行数 */
    int deleteByIds(@Param("ids") List<Long> ids);

    /** 2.6 批量插入口味（一条 INSERT 语句插入多行） */
    int insertFlavors(@Param("flavors") List<DishFlavor> flavors);

    /** 2.7 按指定列排序（column 只能由 Service 传入白名单里的列名） */
    List<Dish> selectOrderBy(@Param("column") String column, @Param("asc") boolean asc);
}
