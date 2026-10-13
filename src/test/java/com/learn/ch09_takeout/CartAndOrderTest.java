package com.learn.ch09_takeout;

import com.learn.ch09_takeout.common.StatusConstant;
import com.learn.ch09_takeout.domain.OrderDetail;
import com.learn.ch09_takeout.domain.OrderStatus;
import com.learn.ch09_takeout.domain.Orders;
import com.learn.ch09_takeout.domain.ShoppingCart;
import com.learn.ch09_takeout.dto.DishDTO;
import com.learn.ch09_takeout.dto.SalesTop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch09 练习9.4~9.6：购物车、订单、统计")
class CartAndOrderTest extends TakeoutTestBase {

    static final Long TOM = 1L;
    static final Long AMY = 2L;

    Long kungPao;
    Long rice;
    Long fish;

    @BeforeEach
    void data() {
        Long cat = categoryService.add("川菜", 1);
        kungPao = enabledDish(cat, "宫保鸡丁", "28");
        rice = enabledDish(cat, "米饭", "2");
        fish = enabledDish(cat, "水煮鱼", "58.5");
    }

    @Test
    @DisplayName("9.4 购物车")
    void cart() {
        cartService.add(TOM, kungPao);
        cartService.add(TOM, rice);
        cartService.add(TOM, kungPao);
        cartService.add(AMY, rice);

        List<ShoppingCart> list = cartService.list(TOM);
        assertEquals(2, list.size());
        assertEquals("宫保鸡丁", list.get(0).getName());
        assertEquals(2, list.get(0).getNumber());
        assertEquals(0, bd("28").compareTo(list.get(0).getAmount()));
        assertEquals(0, bd("58").compareTo(cartService.total(TOM)));

        cartService.sub(TOM, kungPao);
        assertEquals(1, cartService.list(TOM).get(0).getNumber());
        cartService.sub(TOM, kungPao);
        assertEquals(1, cartService.list(TOM).size());
        assertServiceError("购物车中没有该菜品", () -> cartService.sub(TOM, kungPao));

        Long off = dishService.add(new DishDTO("停售菜", 1L, bd("1"), null));
        assertServiceError("菜品不存在或已停售", () -> cartService.add(TOM, off));
        assertServiceError("菜品不存在或已停售", () -> cartService.add(TOM, 99L));

        cartService.clean(TOM);
        assertTrue(cartService.list(TOM).isEmpty());
        assertEquals(1, cartService.list(AMY).size(), "不能清掉别人的购物车");
        assertEquals(0, BigDecimal.ZERO.compareTo(cartService.total(TOM)));
    }

    @Test
    @DisplayName("9.5 下单")
    void submit() {
        assertServiceError("购物车数据为空，不能下单", () -> orderService.submit(TOM));

        cartService.add(TOM, kungPao);
        cartService.add(TOM, kungPao);
        cartService.add(TOM, fish);
        Orders o = orderService.submit(TOM);

        assertNotNull(o.getId());
        assertEquals("202405011200000001", o.getNumber());
        assertEquals(OrderStatus.PENDING_PAYMENT, o.getStatus());
        assertEquals(now, o.getOrderTime());
        assertEquals(0, bd("114.5").compareTo(o.getAmount()));
        assertEquals(List.of(
                new OrderDetail(kungPao, "宫保鸡丁", bd("28"), 2),
                new OrderDetail(fish, "水煮鱼", bd("58.5"), 1)), o.getDetails());
        assertTrue(cartService.list(TOM).isEmpty(), "下单后清空购物车");
        assertSame(o, orderService.getById(o.getId()));

        cartService.add(TOM, rice);
        assertEquals("202405011200000002", orderService.submit(TOM).getNumber());
        assertServiceError("订单不存在", () -> orderService.getById(99L));
    }

    private Orders newOrder(Long user) {
        cartService.add(user, kungPao);
        return orderService.submit(user);
    }

    @Test
    @DisplayName("9.5 支付与状态流转")
    void flow() {
        Orders o = newOrder(TOM);
        Long id = o.getId();
        assertServiceError("订单状态错误", () -> orderService.confirm(id));
        assertServiceError("订单不存在", () -> orderService.pay(AMY, id));

        now = now.plusMinutes(3);
        orderService.pay(TOM, id);
        assertEquals(OrderStatus.TO_BE_CONFIRMED, orderService.getById(id).getStatus());
        assertEquals(now, orderService.getById(id).getCheckoutTime());
        assertServiceError("订单状态错误", () -> orderService.pay(TOM, id));

        assertServiceError("订单状态错误", () -> orderService.deliver(id));
        orderService.confirm(id);
        assertServiceError("订单状态错误", () -> orderService.complete(id));
        orderService.deliver(id);
        orderService.complete(id);
        assertEquals(OrderStatus.COMPLETED, orderService.getById(id).getStatus());
        assertServiceError("订单状态错误，不能取消", () -> orderService.cancel(id, "不想要了"));
    }

    @Test
    @DisplayName("9.5 取消与超时自动取消")
    void cancel() {
        Orders paid = newOrder(TOM);
        orderService.pay(TOM, paid.getId());
        orderService.cancel(paid.getId(), "不想要了");
        Orders c = orderService.getById(paid.getId());
        assertEquals(OrderStatus.CANCELLED, c.getStatus());
        assertEquals("不想要了", c.getCancelReason());
        assertEquals(now, c.getCancelTime());

        Orders early = newOrder(TOM);       // 12:00 下单
        now = now.plusMinutes(10);
        Orders late = newOrder(AMY);        // 12:10 下单
        Orders paidLate = newOrder(AMY);
        orderService.pay(AMY, paidLate.getId());

        now = now.plusMinutes(5);           // 12:15，early 刚好 15 分钟，不算超时
        assertEquals(0, orderService.cancelTimeoutOrders());
        now = now.plusSeconds(1);           // 12:15:01
        assertEquals(1, orderService.cancelTimeoutOrders());
        assertEquals(OrderStatus.CANCELLED, orderService.getById(early.getId()).getStatus());
        assertEquals("订单超时，自动取消", orderService.getById(early.getId()).getCancelReason());
        assertEquals(OrderStatus.PENDING_PAYMENT, orderService.getById(late.getId()).getStatus());
        assertEquals(OrderStatus.TO_BE_CONFIRMED, orderService.getById(paidLate.getId()).getStatus());
    }

    @Test
    @DisplayName("9.5 历史订单")
    void history() {
        Orders a = newOrder(TOM);
        now = now.plusHours(1);
        Orders b = newOrder(TOM);
        Orders c = newOrder(TOM); // 和 b 同一时间
        newOrder(AMY);
        assertEquals(List.of(c.getId(), b.getId(), a.getId()),
                orderService.history(TOM).stream().map(Orders::getId).toList());
    }

    private void completedOrder(Long user, Long... dishIds) {
        for (Long d : dishIds) {
            cartService.add(user, d);
        }
        Orders o = orderService.submit(user);
        orderService.pay(user, o.getId());
        orderService.confirm(o.getId());
        orderService.deliver(o.getId());
        orderService.complete(o.getId());
    }

    @Test
    @DisplayName("9.6 营业额与销量排行")
    void report() {
        completedOrder(TOM, kungPao, rice, rice);         // 5-01: 32
        newOrder(AMY);                                    // 5-01: 未完成，不统计
        now = now.plusDays(2);
        completedOrder(AMY, fish, rice);                  // 5-03: 60.5
        completedOrder(TOM, kungPao);                     // 5-03: 28
        now = now.plusDays(10);
        completedOrder(TOM, fish, fish, fish, fish);      // 5-13: 不在区间里

        LocalDate begin = LocalDate.of(2024, 4, 30);
        LocalDate end = LocalDate.of(2024, 5, 3);
        Map<LocalDate, BigDecimal> t = reportService.turnover(begin, end);
        assertEquals(List.of(begin, begin.plusDays(1), begin.plusDays(2), end), List.copyOf(t.keySet()));
        assertEquals(0, BigDecimal.ZERO.compareTo(t.get(begin)));
        assertEquals(0, bd("32").compareTo(t.get(LocalDate.of(2024, 5, 1))));
        assertEquals(0, BigDecimal.ZERO.compareTo(t.get(LocalDate.of(2024, 5, 2))));
        assertEquals(0, bd("88.5").compareTo(t.get(end)));

        assertEquals(List.of(new SalesTop("米饭", 3), new SalesTop("宫保鸡丁", 2)), reportService.top(begin, end, 2));
        assertEquals(3, reportService.top(begin, end, 10).size());
        assertServiceError("开始日期不能晚于结束日期", () -> reportService.turnover(end, begin));
        assertServiceError("开始日期不能晚于结束日期", () -> reportService.top(end, begin, 10));
    }

    @Test
    @DisplayName("Controller + 全局异常处理")
    void controller() {
        var dishController = new com.learn.ch09_takeout.controller.DishController(dishService);
        var r = dishController.add(new DishDTO("宫保鸡丁", 1L, bd("1"), null));
        assertFalse(r.isSuccess());
        assertEquals("菜品名称已存在", r.getMsg());
        assertTrue(dishController.startOrStop(StatusConstant.DISABLE, rice).isSuccess());
        assertEquals(2, dishController.list(1L).getData().size());
    }
}
