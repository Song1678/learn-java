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
        // @answer
        Dish dish = dishMapper.selectById(dishId);
        if (dish == null || dish.getStatus() != StatusConstant.ENABLE) {
            throw new ServiceException("菜品不存在或已停售");
        }
        ShoppingCart existing = find(userId, dishId);
        if (existing != null) {
            existing.setNumber(existing.getNumber() + 1);
            cartMapper.updateById(existing);
            return;
        }
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userId);
        cart.setDishId(dishId);
        cart.setName(dish.getName());
        cart.setAmount(dish.getPrice());
        cart.setNumber(1);
        cartMapper.insert(cart);
        // @end
    }

    /**
     * 减一份。购物车里没有这道菜：ServiceException("购物车中没有该菜品")；数量减到 0 就删除这一条。
     */
    public void sub(Long userId, Long dishId) {
        // @answer
        ShoppingCart existing = find(userId, dishId);
        if (existing == null) {
            throw new ServiceException("购物车中没有该菜品");
        }
        if (existing.getNumber() <= 1) {
            cartMapper.deleteById(existing.getId());
        } else {
            existing.setNumber(existing.getNumber() - 1);
            cartMapper.updateById(existing);
        }
        // @end
    }
    // @hide 可以抽一个 private 方法：查找某个用户购物车里的某道菜

    private ShoppingCart find(Long userId, Long dishId) {
        List<ShoppingCart> list = cartMapper.selectList(
                c -> c.getUserId().equals(userId) && c.getDishId().equals(dishId));
        return list.isEmpty() ? null : list.get(0);
    }
    // @end

    /** 某个用户的购物车，按加入顺序（id 升序） */
    public List<ShoppingCart> list(Long userId) {
        // @answer
        return cartMapper.selectList(c -> c.getUserId().equals(userId));
        // @end
    }

    /** 购物车总价 = Σ 单价 × 数量 */
    public BigDecimal total(Long userId) {
        // @answer
        return list(userId).stream()
                .map(c -> c.getAmount().multiply(BigDecimal.valueOf(c.getNumber())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // @end
    }

    /** 清空某个用户的购物车（不能影响其他用户） */
    public void clean(Long userId) {
        // @answer
        for (ShoppingCart c : list(userId)) {
            cartMapper.deleteById(c.getId());
        }
        // @end
    }
}
