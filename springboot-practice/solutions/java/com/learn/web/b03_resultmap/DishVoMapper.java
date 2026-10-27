package com.learn.web.b03_resultmap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * b03 练习 3.1 ~ 3.3：多表查询与结果映射，SQL 写在 mapper/b03/DishVoMapper.xml
 */
@Mapper
public interface DishVoMapper {

    /** 3.1 菜品 + 分类名称；categoryId 为 null 时查全部 */
    List<DishVO> selectWithCategory(@Param("categoryId") Long categoryId);

    /** 3.2 菜品详情 + 口味列表（一对多），不存在返回 null */
    DishVO selectDetail(@Param("id") Long id);

    /** 3.3 所有套餐 + 包含的菜品（多对多） */
    List<SetmealVO> selectSetmeals();
}
