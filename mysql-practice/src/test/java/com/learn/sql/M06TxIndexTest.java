package com.learn.sql;

import com.learn.sql.support.SqlTestSupport;
import com.learn.sql.support.SqlTestSupport.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.learn.sql.support.SqlTestSupport.query;
import static com.learn.sql.support.SqlTestSupport.row;
import static org.junit.jupiter.api.Assertions.*;

/**
 * m06 事务与索引
 */
@DisplayName("m06 事务与索引")
class M06TxIndexTest {

    Connection conn;

    @BeforeEach
    void setUp() {
        SqlTestSupport.resetDatabase();
        conn = SqlTestSupport.connection();
    }

    @AfterEach
    void tearDown() throws SQLException {
        conn.close();
    }

    private List<List<String>> rows(String sql) {
        return query(conn, sql).rows();
    }

    /** 对一条查询执行 EXPLAIN，返回 possible_keys 和 key 两列 */
    private Result explain(String sql) {
        return query(conn, "EXPLAIN " + sql);
    }

    private static String column(Result r, String name) {
        int i = r.columns().indexOf(name);
        return r.rows().get(0).get(i);
    }

    @Test
    @DisplayName("q01 事务下单")
    void q01() {
        SqlTestSupport.runAnswer(conn, "m06_tx_index/q01_transaction.sql");
        // 用另一个连接检查：没提交的数据别的连接看不到
        try (Connection other = SqlTestSupport.connection()) {
            List<List<String>> order = query(other,
                    "SELECT id, status, user_id, address_book_id, order_time, amount FROM orders WHERE number = '2024050820000099'").rows();
            assertEquals(List.of(row("29", "1", "3", "4", "2024-05-08 20:00:00", "32.00")), order,
                    "订单不存在或字段不对（如果字段都对，检查一下有没有 COMMIT）");
            assertEquals(List.of(row("29", "NULL", "1", "单人工作餐", "1", "32.00")),
                    query(other, "SELECT order_id, IFNULL(dish_id, 'NULL'), setmeal_id, name, number, amount FROM order_detail WHERE id > 54").rows(),
                    "明细不对：order_id 应该是新订单的 id（LAST_INSERT_ID()）");
            assertEquals(List.of(row("0")), query(other, "SELECT COUNT(*) FROM shopping_cart WHERE user_id = 3").rows(),
                    "用户 3 的购物车应该被清空");
            assertEquals(List.of(row("3")), query(other, "SELECT COUNT(*) FROM shopping_cart").rows(),
                    "其他用户的购物车不能删");
        } catch (SQLException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("q02 创建索引")
    void q02() {
        SqlTestSupport.runAnswer(conn, "m06_tx_index/q02_create_index.sql");
        List<List<String>> idx = rows("SELECT INDEX_NAME, NON_UNIQUE, SEQ_IN_INDEX, COLUMN_NAME FROM information_schema.STATISTICS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'orders' AND INDEX_NAME <> 'PRIMARY' "
                + "ORDER BY INDEX_NAME, SEQ_IN_INDEX");
        assertEquals(List.of(row("idx_user_time", "1", "1", "user_id"), row("idx_user_time", "1", "2", "order_time"),
                row("uk_number", "0", "1", "number")), idx, "索引名、列的顺序或是否唯一不对");
        assertEquals("uk_number", column(explain("SELECT * FROM orders WHERE number = '2024050111300003'"), "key"));
        assertThrows(SQLException.class, () -> SqlTestSupport.execute(conn,
                "INSERT INTO orders (number, user_id, order_time, amount) VALUES ('2024050111300003', 1, NOW(), 1)"),
                "uk_number 应该是唯一索引，重复的订单号不能插入");
    }

    @Test
    @DisplayName("q03 最左前缀原则")
    void q03() throws SQLException {
        SqlTestSupport.execute(conn, "CREATE INDEX idx_user_time ON orders (user_id, order_time)");
        List<String> statements = SqlTestSupport.answerStatements("m06_tx_index/q03_leftmost_prefix.sql");
        String sql = statements.get(statements.size() - 1);
        Result result = SqlTestSupport.runAnswer(conn, "m06_tx_index/q03_leftmost_prefix.sql");
        SqlTestSupport.assertResult(new Result(List.of("id", "number"), List.of(
                row("13", "2024050318300013"), row("18", "2024050612050018"),
                row("22", "2024050712000022"), row("25", "2024050712300025"))), result, true);
        String keys = column(explain(sql), "possible_keys");
        assertTrue(keys != null && keys.contains("idx_user_time"),
                "这个查询用不上 idx_user_time（possible_keys = " + keys + "），想想最左前缀原则");
    }

    @Test
    @DisplayName("q04 避免对索引列使用函数")
    void q04() throws SQLException {
        SqlTestSupport.execute(conn, "CREATE INDEX idx_order_time ON orders (order_time)");
        List<String> statements = SqlTestSupport.answerStatements("m06_tx_index/q04_avoid_function.sql");
        String sql = statements.get(statements.size() - 1);
        Result result = SqlTestSupport.runAnswer(conn, "m06_tx_index/q04_avoid_function.sql");
        SqlTestSupport.assertResult(new Result(List.of("id"),
                List.of(row("14"), row("15"), row("16"), row("17"))), result, true);
        String keys = column(explain(sql), "possible_keys");
        assertTrue(keys != null && keys.contains("idx_order_time"),
                "这个查询用不上 idx_order_time（possible_keys = " + keys + "），是不是对 order_time 用了函数？");
    }
}
