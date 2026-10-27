package com.learn.web.b04_tx;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 本章用到的所有 SQL（已写好），本章专心练事务。
 */
@Mapper
public interface TxMapper {

    // ------------------------------------------------------------------ 菜品、口味

    @Insert("INSERT INTO dish (name, category_id, price, status, create_time, update_time) "
            + "VALUES (#{name}, #{categoryId}, #{price}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertDish(TxDish dish);

    @Insert("INSERT INTO dish_flavor (dish_id, name, value) VALUES (#{dishId}, #{name}, #{value})")
    int insertFlavor(@Param("dishId") Long dishId, @Param("name") String name, @Param("value") String value);

    /** 返回 null 表示菜品不存在 */
    @Select("SELECT status FROM dish WHERE id = #{id}")
    Integer selectDishStatus(Long id);

    @Select("SELECT COUNT(*) FROM setmeal_dish WHERE dish_id = #{dishId}")
    long countSetmealByDish(Long dishId);

    @Delete("DELETE FROM dish WHERE id = #{id}")
    int deleteDish(Long id);

    @Delete("DELETE FROM dish_flavor WHERE dish_id = #{dishId}")
    int deleteFlavorsByDish(Long dishId);

    // ------------------------------------------------------------------ 下单

    /** 查询某个用户的某个地址，不存在（或不是这个用户的）返回 null */
    @Select("SELECT id FROM address_book WHERE id = #{id} AND user_id = #{userId}")
    Long selectAddressId(@Param("id") Long id, @Param("userId") Long userId);

    /** 某个用户的购物车，每行是一个 Map：dish_id、setmeal_id、name、amount、number */
    @Select("SELECT dish_id, setmeal_id, name, amount, number FROM shopping_cart WHERE user_id = #{userId} ORDER BY id")
    List<Map<String, Object>> selectCart(Long userId);

    @Insert("INSERT INTO orders (number, status, user_id, address_book_id, order_time, amount, remark) "
            + "VALUES (#{number}, #{status}, #{userId}, #{addressBookId}, #{orderTime}, #{amount}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertOrder(TxOrder order);

    @Insert("INSERT INTO order_detail (order_id, dish_id, setmeal_id, name, number, amount) "
            + "VALUES (#{orderId}, #{dishId}, #{setmealId}, #{name}, #{number}, #{amount})")
    int insertOrderDetail(@Param("orderId") Long orderId, @Param("dishId") Long dishId,
                          @Param("setmealId") Long setmealId, @Param("name") String name,
                          @Param("number") Integer number, @Param("amount") BigDecimal amount);

    @Delete("DELETE FROM shopping_cart WHERE user_id = #{userId}")
    int clearCart(Long userId);

    // ------------------------------------------------------------------ 分类（TxPitfalls 用）

    @Insert("INSERT INTO category (type, name, sort, status, create_time, update_time) VALUES (1, #{name}, 99, 1, NOW(), NOW())")
    int insertCategory(String name);
}
