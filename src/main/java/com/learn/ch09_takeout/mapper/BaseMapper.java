package com.learn.ch09_takeout.mapper;

import com.learn.ch09_takeout.domain.BaseEntity;

import java.util.List;
import java.util.function.Predicate;

/**
 * 通用 Mapper 接口（已写好）—— 模仿 MyBatis-Plus 的 BaseMapper&lt;T&gt;。
 *
 * <p>在 RuoYi-Vue-Plus 里你只要写 interface DishMapper extends BaseMapperPlus&lt;Dish, DishVo&gt; {}，
 * 就自动拥有了增删改查方法，背后是 MyBatis-Plus 生成 SQL。这里用内存 Map 模拟数据库。
 *
 * @param <T> 实体类型
 */
public interface BaseMapper<T extends BaseEntity> {

    /** 插入，自动生成 id、createTime、updateTime，返回影响行数 1 */
    int insert(T entity);

    /** 根据 id 查询，不存在返回 null */
    T selectById(Long id);

    /** 查询全部，按 id 升序 */
    List<T> selectList();

    /** 条件查询（相当于 SQL 的 WHERE），按 id 升序 */
    List<T> selectList(Predicate<T> where);

    /** 条件计数 */
    long selectCount(Predicate<T> where);

    /** 根据 id 更新，自动刷新 updateTime。id 为 null 或不存在返回 0，成功返回 1 */
    int updateById(T entity);

    /** 根据 id 删除。不存在返回 0，成功返回 1 */
    int deleteById(Long id);
}
