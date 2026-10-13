package com.learn.ch09_takeout;

import com.learn.ch09_takeout.common.R;
import com.learn.ch09_takeout.common.StatusConstant;
import com.learn.ch09_takeout.controller.DishController;
import com.learn.ch09_takeout.controller.OrderController;
import com.learn.ch09_takeout.domain.Orders;
import com.learn.ch09_takeout.dto.DishDTO;
import com.learn.ch09_takeout.mapper.CategoryMapper;
import com.learn.ch09_takeout.mapper.DishMapper;
import com.learn.ch09_takeout.mapper.OrdersMapper;
import com.learn.ch09_takeout.mapper.ShoppingCartMapper;
import com.learn.ch09_takeout.service.CategoryService;
import com.learn.ch09_takeout.service.DishService;
import com.learn.ch09_takeout.service.OrderService;
import com.learn.ch09_takeout.service.ReportService;
import com.learn.ch09_takeout.service.ShoppingCartService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 迷你外卖控制台演示（已写好）。全部练习完成后运行：
 * <pre>
 * mvn -q compile exec:java -Dexec.mainClass=com.learn.ch09_takeout.TakeoutApp
 * </pre>
 * 想直接看参考答案的运行效果，加上 -Psolutions。
 */
public class TakeoutApp {

    public static void main(String[] args) {
        // ========== 1. 手动“组装”所有对象（Spring 会自动帮你做这一步） ==========
        CategoryMapper categoryMapper = new CategoryMapper();
        DishMapper dishMapper = new DishMapper();
        ShoppingCartMapper cartMapper = new ShoppingCartMapper();
        OrdersMapper ordersMapper = new OrdersMapper();

        CategoryService categoryService = new CategoryService(categoryMapper, dishMapper);
        DishService dishService = new DishService(dishMapper, categoryMapper);
        ShoppingCartService cartService = new ShoppingCartService(cartMapper, dishMapper);
        OrderService orderService = new OrderService(ordersMapper, cartService, LocalDateTime::now);
        ReportService reportService = new ReportService(ordersMapper);

        DishController dishController = new DishController(dishService);
        OrderController orderController = new OrderController(cartService, orderService);

        // ========== 2. 管理端：建分类、建菜品、起售 ==========
        Long sichuan = categoryService.add("川菜", 1);
        Long staple = categoryService.add("主食", 2);
        System.out.println("分类: " + categoryService.listEnabled());

        R<Long> r1 = dishController.add(new DishDTO("宫保鸡丁", sichuan, new BigDecimal("28.00"), "经典川菜"));
        R<Long> r2 = dishController.add(new DishDTO("水煮鱼", sichuan, new BigDecimal("58.00"), null));
        R<Long> r3 = dishController.add(new DishDTO("米饭", staple, new BigDecimal("2.00"), null));
        System.out.println("新增菜品: " + r1 + " " + r2 + " " + r3);
        System.out.println("重复新增: " + dishController.add(new DishDTO("米饭", staple, new BigDecimal("2"), null)));
        System.out.println("价格非法: " + dishController.add(new DishDTO("凉面", staple, BigDecimal.ZERO, null)));

        for (Long id : new Long[]{r1.getData(), r2.getData(), r3.getData()}) {
            dishController.startOrStop(StatusConstant.ENABLE, id);
        }
        System.out.println("川菜列表: " + dishController.list(sichuan));

        // ========== 3. 用户端：加购物车、下单、支付 ==========
        Long userId = 10086L;
        orderController.addToCart(userId, r1.getData());
        orderController.addToCart(userId, r1.getData());
        orderController.addToCart(userId, r3.getData());
        orderController.addToCart(userId, r3.getData());
        System.out.println("购物车: " + cartService.list(userId) + " 合计 " + cartService.total(userId));

        R<Orders> submitted = orderController.submit(userId);
        System.out.println("下单: " + submitted);
        System.out.println("再次下单(购物车已空): " + orderController.submit(userId));
        System.out.println("别人来支付: " + orderController.pay(999L, submitted.getData().getId()));
        System.out.println("本人支付: " + orderController.pay(userId, submitted.getData().getId()));

        // ========== 4. 商家端：接单、派送、完成 ==========
        Long orderId = submitted.getData().getId();
        orderService.confirm(orderId);
        orderService.deliver(orderId);
        orderService.complete(orderId);
        System.out.println("订单最终状态: " + orderService.getById(orderId));

        // ========== 5. 统计 ==========
        LocalDate today = LocalDate.now();
        System.out.println("近3天营业额: " + reportService.turnover(today.minusDays(2), today));
        System.out.println("销量排行: " + reportService.top(today, today, 10));
    }
}
