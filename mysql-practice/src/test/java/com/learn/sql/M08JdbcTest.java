package com.learn.sql;

import com.learn.sql.m08_jdbc.Dish;
import com.learn.sql.m08_jdbc.DishDao;
import com.learn.sql.m08_jdbc.EmployeeDao;
import com.learn.sql.m08_jdbc.JdbcBasics;
import com.learn.sql.m08_jdbc.OrderSubmitService;
import com.learn.sql.support.SqlTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.learn.sql.support.SqlTestSupport.query;
import static com.learn.sql.support.SqlTestSupport.row;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("m08 JDBC")
class M08JdbcTest {

    @BeforeEach
    void reset() {
        SqlTestSupport.resetDatabase();
    }

    private static List<List<String>> rows(String sql) {
        try (Connection conn = SqlTestSupport.connection()) {
            return query(conn, sql).rows();
        } catch (SQLException e) {
            return fail(e);
        }
    }

    private static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    @Test
    @DisplayName("8.1 JDBC 基本流程")
    void basics() throws SQLException {
        assertEquals(20, JdbcBasics.countDishes());
        assertEquals(List.of("热菜", "凉菜", "主食", "饮品", "超值套餐", "甜品"), JdbcBasics.enabledCategoryNames());
    }

    @Test
    @DisplayName("8.2 DishDao 查询")
    void daoQuery() throws SQLException {
        DishDao dao = new DishDao();
        assertEquals(new Dish(1L, "宫保鸡丁", 1L, bd("28.00"), 1, "经典川菜"), dao.findById(1));
        assertEquals(new Dish(3L, "水煮鱼", 1L, bd("58.00"), 1, null), dao.findById(3));
        assertNull(dao.findById(999));

        List<Dish> drinks = dao.findByCategory(4);
        assertEquals(List.of("可乐", "酸梅汤", "柠檬茶", "鲜榨橙汁"), drinks.stream().map(Dish::name).toList());
        assertTrue(dao.findByCategory(5).isEmpty());

        assertEquals(List.of(1L, 6L, 9L, 19L), dao.searchByName("鸡").stream().map(Dish::id).toList());
        assertEquals(List.of(10L, 20L), dao.searchByName("凉拌").stream().map(Dish::id).toList());
        assertTrue(dao.searchByName("'").isEmpty(), "关键字里有单引号也不能报错（用 PreparedStatement 就不会）");
    }

    @Test
    @DisplayName("8.2 DishDao 增删改")
    void daoUpdate() throws SQLException {
        DishDao dao = new DishDao();
        long id = dao.insert(new Dish(null, "小笼包", 3L, bd("18.50"), 1, null));
        assertEquals(21L, id, "要返回数据库生成的自增 id");
        assertEquals(new Dish(21L, "小笼包", 3L, bd("18.50"), 1, null), dao.findById(21));
        assertEquals(List.of(row("0")), rows("SELECT COUNT(*) FROM dish WHERE create_time IS NULL"));

        assertEquals(1, dao.updatePrice(21, bd("20")));
        assertEquals(0, bd("20").compareTo(dao.findById(21).price()));
        assertEquals(0, dao.updatePrice(999, bd("1")));

        assertEquals(1, dao.deleteById(21));
        assertEquals(0, dao.deleteById(21));
        assertNull(dao.findById(21));
    }

    @Test
    @DisplayName("8.3 SQL 注入")
    void sqlInjection() throws SQLException {
        EmployeeDao dao = new EmployeeDao();
        String attack = "' OR '1'='1";
        // 反面教材：不知道密码也登录成功了
        assertNotNull(dao.loginUnsafe("admin", attack), "演示：拼接 SQL 被注入了");

        assertEquals("管理员", dao.login("admin", "123456"));
        assertNull(dao.login("admin", "wrong"));
        assertNull(dao.login("admin", attack), "PreparedStatement 应该能防住注入");
        assertNull(dao.login(attack, attack));
        assertNull(dao.login("lixe", "123456"), "禁用的员工不能登录");
    }

    @Test
    @DisplayName("8.4 事务下单：成功")
    void submitOk() {
        long orderId = new OrderSubmitService().submit(1, 1, "2024050820000001", LocalDateTime.of(2024, 5, 8, 20, 0));
        assertEquals(29L, orderId);
        assertEquals(List.of(row("2024050820000001", "1", "1", "1", "2024-05-08 20:00:00", "32.00")),
                rows("SELECT number, status, user_id, address_book_id, order_time, amount FROM orders WHERE id = 29"));
        assertEquals(List.of(row("29", "1", "NULL", "宫保鸡丁", "1", "28.00"), row("29", "11", "NULL", "米饭", "2", "2.00")),
                rows("SELECT order_id, dish_id, IFNULL(setmeal_id, 'NULL'), name, number, amount FROM order_detail WHERE order_id = 29 ORDER BY id"));
        assertEquals(List.of(row("0")), rows("SELECT COUNT(*) FROM shopping_cart WHERE user_id = 1"));

        long setmealOrder = new OrderSubmitService().submit(3, 4, "2024050820000002", LocalDateTime.of(2024, 5, 8, 20, 5));
        assertEquals(List.of(row("NULL", "1", "单人工作餐")),
                rows("SELECT IFNULL(dish_id, 'NULL'), setmeal_id, name FROM order_detail WHERE order_id = " + setmealOrder),
                "套餐的 dish_id 应该存 NULL，而不是 0");
    }

    @Test
    @DisplayName("8.4 事务下单：购物车为空")
    void submitEmptyCart() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> new OrderSubmitService().submit(2, 3, "X", LocalDateTime.of(2024, 5, 8, 20, 0)));
        assertEquals("购物车为空，不能下单", e.getMessage());
        assertEquals(List.of(row("28")), rows("SELECT COUNT(*) FROM orders"));
    }

    @Test
    @DisplayName("8.4 事务下单：中途出错要回滚")
    void submitRollback() {
        OrderSubmitService failing = new OrderSubmitService() {
            @Override
            protected void afterOrderInserted(Connection conn, long orderId) throws SQLException {
                throw new SQLException("模拟：插入订单后数据库出错了");
            }
        };
        assertThrows(RuntimeException.class,
                () -> failing.submit(1, 1, "2024050820000001", LocalDateTime.of(2024, 5, 8, 20, 0)));
        assertEquals(List.of(row("28")), rows("SELECT COUNT(*) FROM orders"), "订单已经插入了但没有回滚");
        assertEquals(List.of(row("2")), rows("SELECT COUNT(*) FROM shopping_cart WHERE user_id = 1"), "购物车不应该被清空");

        OrderSubmitService runtimeFailing = new OrderSubmitService() {
            @Override
            protected void afterOrderInserted(Connection conn, long orderId) {
                throw new IllegalStateException("模拟：业务代码出错了");
            }
        };
        assertThrows(IllegalStateException.class,
                () -> runtimeFailing.submit(1, 1, "2024050820000001", LocalDateTime.of(2024, 5, 8, 20, 0)));
        assertEquals(List.of(row("28")), rows("SELECT COUNT(*) FROM orders"), "RuntimeException 也要回滚");
    }
}
