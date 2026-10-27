package com.learn.web.b01_mybatis;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * b01 练习 1.1 ~ 1.9：用注解写 SQL
 *
 * <p>MyBatis 的用法：只写接口，不写实现类。在方法上用 @Select / @Insert / @Update / @Delete 写 SQL，
 * 启动时 MyBatis 自动生成实现类（动态代理）并放进 Spring 容器，Service 里直接注入使用。
 *
 * <p>对照 mysql-practice 的 m08 DishDao：以前要自己写 getConnection、prepareStatement、setLong、
 * while (rs.next()) ...，现在只写一行 SQL，参数用 #{参数名} 占位（相当于 ?），结果自动装进对象。
 *
 * <p>还没写 SQL 的方法被调用时会报：{@code Invalid bound statement (not found)}。
 */
@Mapper
public interface CategoryMapper {

    /**
     * 1.1 根据 id 查询，不存在返回 null
     */
    // TODO 1.1 加上 @Select 注解
    Category selectById(Long id);

    /**
     * 1.2 查询全部，按 sort 升序，sort 相同按 id 升序
     */
    // TODO 1.2 写 SQL 注解
    List<Category> selectAll();

    /**
     * 1.3 按类型和状态查询，按 sort 升序。
     * <p>有多个参数时，用 @Param("名字") 给参数起名，SQL 里写 #{名字}。
     */
    // TODO 1.3 写 SQL 注解
    List<Category> selectByTypeAndStatus(@Param("type") Integer type, @Param("status") Integer status);

    /**
     * 1.4 统计某个状态的分类有多少个（返回值是简单类型也可以）
     */
    // TODO 1.4 写 SQL 注解
    long countByStatus(Integer status);

    /**
     * 1.5 只查所有分类的名称，按 id 升序
     */
    // TODO 1.5 写 SQL 注解
    List<String> selectNames();

    /**
     * 1.6 名称模糊查询（包含 keyword），按 id 升序。
     * <p>提示：LIKE '%#{keyword}%' 是错的（#{} 在引号里不会被替换），要用 CONCAT('%', #{keyword}, '%')
     */
    // TODO 1.6 写 SQL 注解
    List<Category> searchByName(String keyword);

    /**
     * 1.7 插入一条分类：type、name、sort、status 用对象里的值，create_time 和 update_time 都用 NOW()。
     * 返回影响的行数；插入后要把数据库生成的自增 id 回填到 category.id 里。
     * <p>提示：参数是对象时，#{name} 取的是 category.getName()；
     * 回填 id 用 {@code @Options(useGeneratedKeys = true, keyProperty = "id")}（对照 JDBC 的 RETURN_GENERATED_KEYS）
     */
    // TODO 1.7 加上 @Insert 和 @Options
    int insert(Category category);

    /**
     * 1.8 修改排序，同时把 update_time 设为 NOW()，返回影响的行数
     */
    // TODO 1.8 写 SQL 注解
    int updateSort(@Param("id") Long id, @Param("sort") Integer sort);

    /**
     * 1.9 根据 id 删除，返回影响的行数
     */
    // TODO 1.9 写 SQL 注解
    int deleteById(Long id);
}
