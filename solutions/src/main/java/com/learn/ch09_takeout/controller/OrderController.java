package com.learn.ch09_takeout.controller;

import com.learn.ch09_takeout.common.GlobalExceptionHandler;
import com.learn.ch09_takeout.common.R;
import com.learn.ch09_takeout.domain.Orders;
import com.learn.ch09_takeout.service.OrderService;
import com.learn.ch09_takeout.service.ShoppingCartService;

/**
 * 用户端：购物车 + 下单接口（已写好）。
 *
 * <p>真实项目里 userId 不是前端传的，而是从登录 token 里解析出来（RuoYi-Vue-Plus 用 Sa-Token：LoginHelper.getUserId()）。
 */
public class OrderController {

    private final ShoppingCartService cartService;
    private final OrderService orderService;

    public OrderController(ShoppingCartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    /** POST /user/shoppingCart/add */
    public R<Void> addToCart(Long userId, Long dishId) {
        return GlobalExceptionHandler.execute(() -> {
            cartService.add(userId, dishId);
            return R.ok();
        });
    }

    /** POST /user/order/submit */
    public R<Orders> submit(Long userId) {
        return GlobalExceptionHandler.execute(() -> R.ok(orderService.submit(userId)));
    }

    /** PUT /user/order/payment */
    public R<Void> pay(Long userId, Long orderId) {
        return GlobalExceptionHandler.execute(() -> {
            orderService.pay(userId, orderId);
            return R.ok();
        });
    }
}
