package com.learn.web;

import com.learn.web.b04_tx.DishTxService;
import com.learn.web.b04_tx.OrderTxService;
import com.learn.web.b04_tx.TxDish;
import com.learn.web.b04_tx.TxOrder;
import com.learn.web.b04_tx.TxPitfalls;
import com.learn.web.common.ServiceException;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("b04 事务")
class B04TransactionTest extends DbTestBase {

    @Autowired
    DishTxService dishTxService;

    @Autowired
    OrderTxService orderTxService;

    @Autowired
    TxPitfalls pitfalls;

    private static TxDish dish(String name, TxDish.Flavor... flavors) {
        TxDish d = new TxDish();
        d.setName(name);
        d.setCategoryId(3L);
        d.setPrice(new BigDecimal("18.00"));
        d.setFlavors(List.of(flavors));
        return d;
    }

    @Test
    @DisplayName("4.1 新增菜品和口味：成功")
    void saveOk() {
        Long id = dishTxService.saveWithFlavors(dish("小笼包",
                new TxDish.Flavor("辣度", "不辣,微辣"), new TxDish.Flavor("忌口", "不要葱")));
        assertEquals(21L, id);
        assertEquals(0, ((Number) row("SELECT status FROM dish WHERE id = 21").get("status")).intValue(), "新菜品默认停售");
        assertEquals(2, count("SELECT COUNT(*) FROM dish_flavor WHERE dish_id = 21"));
    }

    @Test
    @DisplayName("4.1 新增菜品和口味：失败要回滚")
    void saveRollback() {
        ServiceException e = assertThrows(ServiceException.class, () -> dishTxService.saveWithFlavors(dish("小笼包",
                new TxDish.Flavor("辣度", "不辣,微辣"), new TxDish.Flavor(" ", "xx"))));
        assertEquals("口味名称不能为空", e.getMessage());
        assertEquals(0, count("SELECT COUNT(*) FROM dish WHERE name = '小笼包'"), "菜品已经插入了，但事务回滚后应该被撤销");
        assertEquals(10, count("SELECT COUNT(*) FROM dish_flavor"), "第一个口味也要被撤销");
    }

    @Test
    @DisplayName("4.2 批量删除：成功")
    void deleteOk() {
        dishTxService.deleteBatch(List.of(10L, 18L));
        assertEquals(18, count("SELECT COUNT(*) FROM dish"));
        assertEquals(0, count("SELECT COUNT(*) FROM dish_flavor WHERE dish_id = 18"), "口味也要一起删");
    }

    @Test
    @DisplayName("4.2 批量删除：失败要回滚")
    void deleteRollback() {
        assertEquals("当前菜品关联了套餐，不能删除",
                assertThrows(ServiceException.class, () -> dishTxService.deleteBatch(List.of(18L, 5L))).getMessage());
        assertEquals(20, count("SELECT COUNT(*) FROM dish"), "18 号在 5 号之前已经删掉了，要回滚恢复");
        assertEquals(1, count("SELECT COUNT(*) FROM dish_flavor WHERE dish_id = 18"));

        assertEquals("起售中的菜品不能删除",
                assertThrows(ServiceException.class, () -> dishTxService.deleteBatch(List.of(10L, 1L))).getMessage());
        assertEquals(20, count("SELECT COUNT(*) FROM dish"));
        assertEquals("菜品不存在",
                assertThrows(ServiceException.class, () -> dishTxService.deleteBatch(List.of(10L, 99L))).getMessage());
        assertEquals(20, count("SELECT COUNT(*) FROM dish"));
    }

    @Test
    @DisplayName("4.3 下单：成功")
    void submitOk() {
        TxOrder order = orderTxService.submit(1L, 2L, "多放辣");
        assertEquals(29L, order.getId(), "要回填订单 id");
        Map<String, Object> row = row("SELECT * FROM orders WHERE id = 29");
        assertEquals(1, ((Number) row.get("status")).intValue());
        assertEquals(0, new BigDecimal("32.00").compareTo((BigDecimal) row.get("amount")), "28×1 + 2×2 = 32");
        assertEquals("多放辣", row.get("remark"));
        assertEquals(2L, ((Number) row.get("address_book_id")).longValue());
        assertTrue(((String) row.get("number")).endsWith("1"), "订单号 = yyyyMMddHHmmss + 用户id");
        assertEquals(15, ((String) row.get("number")).length());
        assertEquals(2, count("SELECT COUNT(*) FROM order_detail WHERE order_id = 29"));
        assertEquals(2, count("SELECT number FROM order_detail WHERE order_id = 29 AND name = '米饭'"));
        assertEquals(0, count("SELECT COUNT(*) FROM shopping_cart WHERE user_id = 1"), "下单后清空购物车");
        assertEquals(1, count("SELECT COUNT(*) FROM shopping_cart WHERE user_id = 3"), "别人的购物车不能动");

        TxOrder setmealOrder = orderTxService.submit(3L, 4L, null);
        assertEquals(0, new BigDecimal("32.00").compareTo(setmealOrder.getAmount()));
        assertEquals(1, count("SELECT COUNT(*) FROM order_detail WHERE order_id = ? AND setmeal_id = 1", setmealOrder.getId()));
    }

    @Test
    @DisplayName("4.3 下单：校验与回滚")
    void submitFail() {
        assertEquals("收货地址不存在",
                assertThrows(ServiceException.class, () -> orderTxService.submit(1L, 3L, null)).getMessage(),
                "3 号地址是李四的");
        assertEquals("购物车为空，不能下单",
                assertThrows(ServiceException.class, () -> orderTxService.submit(2L, 3L, null)).getMessage());

        jdbc.update("INSERT INTO shopping_cart (user_id, dish_id, name, amount, number) VALUES (8, 18, '柠檬茶', 12.00, 1)");
        jdbc.update("INSERT INTO address_book (user_id, consignee, phone, detail, is_default) VALUES (8, '郑十', '13800000008', '北门', 1)");
        assertEquals("柠檬茶已停售，请重新选择",
                assertThrows(ServiceException.class, () -> orderTxService.submit(8L, 10L, null)).getMessage());
        assertEquals(28, count("SELECT COUNT(*) FROM orders"), "订单要回滚");
        assertEquals(54, count("SELECT COUNT(*) FROM order_detail"), "明细要回滚");
        assertEquals(2, count("SELECT COUNT(*) FROM shopping_cart WHERE user_id = 8"), "购物车不能被清空");
    }

    @Test
    @DisplayName("4.4 找 bug：受检异常不回滚")
    void checkedException() {
        assertThrows(IOException.class, () -> pitfalls.importChecked(List.of("导入A", "导入B", "ERROR")));
        assertEquals(7, count("SELECT COUNT(*) FROM category"), "失败了，导入A、导入B 不应该留在数据库里");
    }

    @Test
    @DisplayName("4.5 找 bug：异常被吞掉")
    void swallowed() {
        ServiceException e = assertThrows(ServiceException.class,
                () -> pitfalls.importSwallow(List.of("导入A", "导入B", "ERROR")), "失败时要抛出 ServiceException");
        assertTrue(e.getMessage().startsWith("导入失败："), "message 要以“导入失败：”开头");
        assertEquals(7, count("SELECT COUNT(*) FROM category"), "失败了，导入A、导入B 不应该留在数据库里");
    }

    @Test
    @DisplayName("4.6 找 bug：自调用")
    void selfInvocation() {
        assertThrows(ServiceException.class, () -> pitfalls.importSelfCall(List.of("导入A", "导入B", "ERROR")));
        assertEquals(7, count("SELECT COUNT(*) FROM category"), "失败了，导入A、导入B 不应该留在数据库里");
        pitfalls.importSelfCall(List.of("导入A", "导入B"));
        assertEquals(9, count("SELECT COUNT(*) FROM category"), "全部成功时要正常提交");
    }
}
