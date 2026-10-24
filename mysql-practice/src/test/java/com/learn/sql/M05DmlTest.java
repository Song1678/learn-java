package com.learn.sql;

import com.learn.sql.support.SqlTestSupport;
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
 * m05 增删改：在示例数据上执行你的 SQL，再查表检查结果。
 */
@DisplayName("m05 增删改")
class M05DmlTest {

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

    private void run(String file) {
        SqlTestSupport.runAnswer(conn, "m05_dml/" + file + ".sql");
    }

    @Test
    @DisplayName("q01 INSERT")
    void q01() {
        run("q01_insert");
        assertEquals(List.of(row("8", "1", "小吃", "8", "1")),
                rows("SELECT id, type, name, sort, status FROM category WHERE name = '小吃'"));
        assertEquals(List.of(row("0")), rows("SELECT COUNT(*) FROM category WHERE name = '小吃' AND create_time IS NULL"),
                "create_time 要设为当前时间");
        assertEquals(List.of(row("8")), rows("SELECT COUNT(*) FROM category"));
    }

    @Test
    @DisplayName("q02 一条语句插入多行")
    void q02() {
        assertEquals(1, SqlTestSupport.answerStatements("m05_dml/q02_insert_multi.sql").size(),
                "题目要求只用一条 INSERT 语句");
        run("q02_insert_multi");
        assertEquals(List.of(row("小笼包", "3", "18.00", "1"), row("煎饺", "3", "12.00", "1"), row("馄饨", "3", "14.00", "1")),
                rows("SELECT name, category_id, price, status FROM dish WHERE id > 20 ORDER BY id"));
        assertEquals(List.of(row("0")), rows("SELECT COUNT(*) FROM dish WHERE id > 20 AND create_time IS NULL"));
    }

    @Test
    @DisplayName("q03 UPDATE")
    void q03() {
        run("q03_update");
        assertEquals(List.of(row("1", "30.80"), row("2", "28.60"), row("3", "63.80"), row("4", "19.80"),
                        row("5", "35.20"), row("6", "46.20"), row("19", "41.80")),
                rows("SELECT id, price FROM dish WHERE category_id = 1 ORDER BY id"));
        assertEquals(List.of(row("12.00"), row("2.00"), row("5.00")),
                rows("SELECT price FROM dish WHERE id IN (7, 11, 15) ORDER BY id"), "其他分类的价格不能变");
    }

    @Test
    @DisplayName("q04 NOT IN 与 NULL")
    void q04() {
        run("q04_not_in_null");
        assertEquals(List.of(row("5", "回锅肉"), row("8", "夫妻肺片"), row("10", "凉拌木耳"), row("18", "柠檬茶")),
                rows("SELECT id, name FROM dish WHERE status = 0 ORDER BY id"),
                "只有夫妻肺片应该被停售（如果一个都没停售，想想 NOT IN 遇到 NULL 会怎样）");
    }

    @Test
    @DisplayName("q05 DELETE")
    void q05() {
        run("q05_delete");
        assertEquals(List.of(row("3"), row("8")), rows("SELECT user_id FROM shopping_cart ORDER BY id"));
    }

    @Test
    @DisplayName("q06 超时订单批量取消")
    void q06() {
        run("q06_timeout_cancel");
        assertEquals(List.of(row("25", "6", "订单超时，自动取消"), row("26", "1", "NULL")),
                rows("SELECT id, status, IFNULL(cancel_reason, 'NULL') FROM orders WHERE id IN (25, 26) ORDER BY id"),
                "12:30 下的单（25）超时，12:35 下的单（26）刚好 15 分钟，不算超时");
        assertEquals(List.of(row("5")), rows("SELECT COUNT(*) FROM orders WHERE status = 6"),
                "只应该多取消 1 个订单");
        assertEquals(List.of(row("2024-05-07 12:30:00", "47.00")),
                rows("SELECT order_time, amount FROM orders WHERE id = 25"), "其他列不要动");
    }

    @Test
    @DisplayName("q07 INSERT ... SELECT 备份")
    void q07() {
        run("q07_insert_select");
        assertEquals(rows("SELECT * FROM dish WHERE status = 0 ORDER BY id"),
                rows("SELECT * FROM dish_backup ORDER BY id"));
        assertEquals(List.of(row("3")), rows("SELECT COUNT(*) FROM dish_backup"));
    }

    @Test
    @DisplayName("q08 多表关联删除")
    void q08() {
        run("q08_delete_join");
        assertEquals(List.of(row("1"), row("2"), row("3"), row("3"), row("6"), row("12"), row("15"), row("16")),
                rows("SELECT dish_id FROM dish_flavor ORDER BY dish_id"));
        assertEquals(List.of(row("20")), rows("SELECT COUNT(*) FROM dish"), "dish 表的数据不能删");
    }
}
