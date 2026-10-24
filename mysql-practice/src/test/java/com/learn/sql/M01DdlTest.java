package com.learn.sql;

import com.learn.sql.support.SqlTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.learn.sql.support.SqlTestSupport.query;
import static com.learn.sql.support.SqlTestSupport.row;
import static org.junit.jupiter.api.Assertions.*;

/**
 * m01 建表练习：在空库 learn_sql_ddl 里执行你的 SQL，再通过插入数据检查约束是否生效。
 */
@DisplayName("m01 建库建表")
class M01DdlTest {

    static final String DB = "learn_sql_ddl";

    Connection conn;

    @BeforeEach
    void setUp() {
        SqlTestSupport.recreate(DB);
        conn = SqlTestSupport.connection(DB);
    }

    @AfterEach
    void tearDown() throws SQLException {
        conn.close();
    }

    private void ok(String sql) {
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            fail("这条 SQL 应该能执行成功，但是报错了：" + e.getMessage() + "\nSQL：" + sql);
        }
    }

    private void fails(String sql, String why) {
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            return;
        }
        fail("这条 SQL 应该报错（" + why + "），但是执行成功了，检查一下约束有没有加上：\n" + sql);
    }

    private List<List<String>> rows(String sql) {
        return query(conn, sql).rows();
    }

    @Test
    @DisplayName("q01 创建分类表")
    void q01() {
        SqlTestSupport.runAnswer(conn, "m01_ddl/q01_create_category.sql");
        ok("INSERT INTO category (type, name) VALUES (1, '热菜')");
        ok("INSERT INTO category (type, name, sort, status, create_time) VALUES (2, '套餐', 5, 0, '2024-05-01 10:00:00')");
        assertEquals(List.of(row("1", "1", "热菜", "0", "1", "NULL"), row("2", "2", "套餐", "5", "0", "2024-05-01 10:00:00")),
                rows("SELECT id, type, name, sort, status, create_time FROM category ORDER BY id"),
                "id 应该自增，sort 默认 0，status 默认 1");
        fails("INSERT INTO category (type, name) VALUES (1, '热菜')", "name 不能重复");
        fails("INSERT INTO category (type, name) VALUES (1, NULL)", "name 不能为空");
        fails("INSERT INTO category (name) VALUES ('凉菜')", "type 不能为空且没有默认值");
        fails("INSERT INTO category (type, name) VALUES (1, '" + "名".repeat(33) + "')", "name 最长 32 个字符");
        ok("INSERT INTO category (type, name) VALUES (1, '" + "名".repeat(32) + "')");
    }

    @Test
    @DisplayName("q02 创建菜品表：DECIMAL 与默认时间")
    void q02() {
        SqlTestSupport.runAnswer(conn, "m01_ddl/q02_create_dish.sql");
        ok("INSERT INTO dish (name, category_id, price) VALUES ('宫保鸡丁', 1, 28.5)");
        ok("INSERT INTO dish (name, category_id, price, status, description) VALUES ('米饭', 3, 12.345, 0, '东北大米')");
        assertEquals(List.of(row("宫保鸡丁", "1", "28.50", "1", "NULL"), row("米饭", "3", "12.35", "0", "东北大米")),
                rows("SELECT name, category_id, price, status, description FROM dish ORDER BY id"),
                "price 应该是 DECIMAL(10,2)，status 默认 1");
        assertEquals(List.of(row("0")), rows("SELECT COUNT(*) FROM dish WHERE create_time IS NULL"),
                "create_time 应该默认为当前时间");
        fails("INSERT INTO dish (name, category_id, price) VALUES ('佛跳墙', 1, 123456789)", "DECIMAL(10,2) 整数部分最多 8 位");
        ok("INSERT INTO dish (name, category_id, price) VALUES ('满汉全席', 1, 99999999.99)");
        fails("INSERT INTO dish (name, category_id, price) VALUES ('宫保鸡丁', 1, 1)", "name 不能重复");
        fails("INSERT INTO dish (name, price) VALUES ('可乐', 5)", "category_id 不能为空");
        fails("INSERT INTO dish (name, category_id) VALUES ('雪碧', 4)", "price 不能为空");
        fails("INSERT INTO dish (name, category_id, price, create_time) VALUES ('橙汁', 4, 5, NULL)", "create_time 不能为空");
    }

    @Test
    @DisplayName("q03 外键")
    void q03() {
        SqlTestSupport.runAnswer(conn, "m01_ddl/q03_foreign_key.sql");
        ok("INSERT INTO orders (number) VALUES ('A001')");
        ok("INSERT INTO order_detail (order_id, name) VALUES (1, '米饭')");
        assertEquals(List.of(row("1", "米饭", "1")), rows("SELECT order_id, name, number FROM order_detail"),
                "number 默认值应该是 1");
        fails("INSERT INTO order_detail (order_id, name) VALUES (99, '可乐')", "订单 99 不存在，外键应该阻止插入");
        fails("DELETE FROM orders WHERE id = 1", "订单下还有明细，外键应该阻止删除");
        fails("INSERT INTO order_detail (name) VALUES ('可乐')", "order_id 不能为空");
        fails("INSERT INTO orders (number) VALUES (NULL)", "number 不能为空");
    }

    @Test
    @DisplayName("q04 ALTER TABLE")
    void q04() {
        ok("CREATE TABLE employee (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(16), username VARCHAR(32) NOT NULL, age INT)");
        ok("INSERT INTO employee (name, username, age) VALUES ('管理员', 'admin', 30)");
        SqlTestSupport.runAnswer(conn, "m01_ddl/q04_alter_table.sql");
        assertEquals(List.of(row("1", "管理员", "admin")), rows("SELECT id, name, username FROM employee"), "原来的数据不能丢");
        ok("INSERT INTO employee (name, username, phone) VALUES ('" + "长".repeat(32) + "', 'zhangsan', '13800000001')");
        fails("INSERT INTO employee (name, username) VALUES (NULL, 'lisi')", "name 改成了 NOT NULL");
        fails("INSERT INTO employee (name, username) VALUES ('张三', 'admin')", "username 加了唯一约束");
        fails("SELECT age FROM employee", "age 列应该已经删除");
    }

    @Test
    @DisplayName("q05 联合唯一约束")
    void q05() {
        SqlTestSupport.runAnswer(conn, "m01_ddl/q05_composite_unique.sql");
        ok("INSERT INTO shopping_cart (user_id, dish_id) VALUES (1, 1)");
        ok("INSERT INTO shopping_cart (user_id, dish_id) VALUES (1, 2)");
        ok("INSERT INTO shopping_cart (user_id, dish_id, number) VALUES (2, 1, 3)");
        assertEquals(List.of(row("1"), row("1"), row("3")), rows("SELECT number FROM shopping_cart ORDER BY id"),
                "number 默认值应该是 1");
        fails("INSERT INTO shopping_cart (user_id, dish_id) VALUES (1, 1)", "(user_id, dish_id) 组合不能重复");
        fails("INSERT INTO shopping_cart (user_id) VALUES (3)", "dish_id 不能为空");
    }
}
