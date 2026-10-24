package com.learn.sql.m08_jdbc;

import com.learn.sql.DbConfig;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 练习 8.4：用 JDBC 手动控制事务 —— 实现“用户下单”（m06-01 的 Java 版）
 *
 * <p>JDBC 默认每条 SQL 自动提交（autoCommit = true）。手动控制事务的固定写法：
 * <pre>
 * conn.setAutoCommit(false);   // 开启事务
 * try {
 *     ... 执行多条 SQL ...
 *     conn.commit();           // 全部成功：提交
 * } catch (Exception e) {
 *     conn.rollback();         // 任何一步失败：回滚，前面执行过的也全部撤销
 *     throw e;
 * }
 * </pre>
 * 在 Spring 里，这一整套被一个 @Transactional 注解代替了。
 */
public class OrderSubmitService {

    /** 购物车里的一项（已写好） */
    public record CartItem(Long dishId, Long setmealId, String name, BigDecimal amount, int number) {
    }

    /**
     * 下单：
     * <ol>
     *   <li>开启事务</li>
     *   <li>查出该用户购物车的所有项（按 id 升序）；购物车为空时抛 IllegalStateException("购物车为空，不能下单")</li>
     *   <li>插入订单：number = orderNo，status = 1（待付款），user_id，address_book_id，order_time，
     *       amount = Σ 单价 × 数量；拿到自增的订单 id</li>
     *   <li>调用 afterOrderInserted(conn, orderId)（测试会用它模拟“中途出错”）</li>
     *   <li>为每个购物车项插入一条 order_detail（dish_id / setmeal_id / name / number / amount）</li>
     *   <li>删除该用户的购物车，提交事务，返回订单 id</li>
     * </ol>
     * 任何一步出错（SQLException 或 RuntimeException）都要回滚，然后把异常继续抛出去
     * （SQLException 包装成 RuntimeException 抛出）。
     *
     * <p>提示：
     * <ul>
     *   <li>LocalDateTime 存入数据库：用 ps.setObject(i, orderTime)。
     *       不要用 ps.setTimestamp(i, Timestamp.valueOf(orderTime))：Timestamp 带时区语义，
     *       电脑时区和 URL 里的 serverTimezone 不一致时，存进去的时间会差几个小时</li>
     *   <li>dish_id / setmeal_id 可能为 null：用 ps.setObject(i, 值) 可以自动处理 null；
     *       读的时候 rs.getObject("dish_id", Long.class) 能读出 null（rs.getLong 会把 NULL 变成 0）</li>
     *   <li>多条明细可以用 ps.addBatch() + ps.executeBatch() 批量插入</li>
     * </ul>
     */
    public long submit(long userId, long addressBookId, String orderNo, LocalDateTime orderTime) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
    // TODO 建议把事务里的业务步骤抽成一个 private 方法，submit 里只负责开启/提交/回滚

    /**
     * 插入订单之后的“扩展点”（已写好，什么都不做）。测试会重写这个方法，故意抛出异常，检查你有没有正确回滚。
     */
    protected void afterOrderInserted(Connection conn, long orderId) throws SQLException {
    }
}
