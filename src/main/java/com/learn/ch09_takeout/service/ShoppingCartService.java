package com.learn.ch09_takeout.service;

import com.learn.ch09_takeout.common.ServiceException;
import com.learn.ch09_takeout.common.StatusConstant;
import com.learn.ch09_takeout.domain.Dish;
import com.learn.ch09_takeout.domain.ShoppingCart;
import com.learn.ch09_takeout.mapper.DishMapper;
import com.learn.ch09_takeout.mapper.ShoppingCartMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 练习 9.4：购物车（每个用户一份，按 userId 区分）
 */
public class ShoppingCartService {

    private final ShoppingCartMapper cartMapper;
    private final DishMapper dishMapper;

    public ShoppingCartService(ShoppingCartMapper cartMapper, DishMapper dishMapper) {
        this.cartMapper = cartMapper;
        this.dishMapper = dishMapper;
    }

    /**
     * 加一份菜到购物车。
     * <ul>
     *   <li>菜品不存在或已停售：ServiceException("菜品不存在或已停售")</li>
     *   <li>该用户购物车里已有这道菜：数量 +1 并 updateById</li>
     *   <li>否则新增一条：userId、dishId、name、amount(=菜品当前价格)、number=1</li>
     * </ul>
     */
    public void add(Long userId, Long dishId) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 减一份。购物车里没有这道菜：ServiceException("购物车中没有该菜品")；数量减到 0 就删除这一条。
     */
    public void sub(Long userId, Long dishId) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
    // TODO 可以抽一个 private 方法：查找某个用户购物车里的某道菜

    /** 某个用户的购物车，按加入顺序（id 升序） */
    public List<ShoppingCart> list(Long userId) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 购物车总价 = Σ 单价 × 数量 */
    public BigDecimal total(Long userId) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 清空某个用户的购物车（不能影响其他用户） */
    public void clean(Long userId) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
