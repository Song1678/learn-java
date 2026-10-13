package com.learn.ch09_takeout;

import com.learn.ch09_takeout.common.ServiceException;
import com.learn.ch09_takeout.common.StatusConstant;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 测试公共部分：每个测试方法执行前都重新组装一套全新的对象。
 */
abstract class TakeoutTestBase {

    protected CategoryMapper categoryMapper;
    protected DishMapper dishMapper;
    protected ShoppingCartMapper cartMapper;
    protected OrdersMapper ordersMapper;
    protected CategoryService categoryService;
    protected DishService dishService;
    protected ShoppingCartService cartService;
    protected OrderService orderService;
    protected ReportService reportService;

    /** 可以手动拨动的“当前时间” */
    protected LocalDateTime now = LocalDateTime.of(2024, 5, 1, 12, 0, 0);

    @BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapper();
        dishMapper = new DishMapper();
        cartMapper = new ShoppingCartMapper();
        ordersMapper = new OrdersMapper();
        categoryService = new CategoryService(categoryMapper, dishMapper);
        dishService = new DishService(dishMapper, categoryMapper);
        cartService = new ShoppingCartService(cartMapper, dishMapper);
        orderService = new OrderService(ordersMapper, cartService, () -> now);
        reportService = new ReportService(ordersMapper);
    }

    protected static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    /** 新建一个起售中的菜，返回 id */
    protected Long enabledDish(Long categoryId, String name, String price) {
        Long id = dishService.add(new DishDTO(name, categoryId, bd(price), null));
        dishService.startOrStop(id, StatusConstant.ENABLE);
        return id;
    }

    protected static void assertServiceError(String expectedMsg, Executable action) {
        ServiceException e = assertThrows(ServiceException.class, action);
        assertEquals(expectedMsg, e.getMessage());
    }
}
