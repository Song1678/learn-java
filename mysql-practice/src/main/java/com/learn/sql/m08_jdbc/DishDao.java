package com.learn.sql.m08_jdbc;

import com.learn.sql.DbConfig;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 练习 8.2：手写 DAO（数据访问对象）—— 就是 MyBatis 的 Mapper 帮你做的事
 *
 * <p>一律使用 PreparedStatement + ? 占位符传参，不要拼接字符串（原因见 8.3）：
 * <pre>
 * PreparedStatement ps = conn.prepareStatement("SELECT ... WHERE id = ?");
 * ps.setLong(1, id);          // 第 1 个问号
 * ResultSet rs = ps.executeQuery();
 * </pre>
 * 连接用 DbConfig.getConnection()，每个方法自己获取、自己关闭。
 */
public class DishDao {

    private static final String COLUMNS = "id, name, category_id, price, status, description";

    // TODO 建议写一个 private 方法：把 ResultSet 的当前行转换成 Dish 对象（MyBatis 的 resultMap 就是干这个的）

    /**
     * 8.2.1 根据 id 查询，不存在返回 null。
     * 提示：数据库列 category_id → Dish 的 categoryId；price 用 rs.getBigDecimal；description 可能为 NULL，getString 会返回 null
     */
    public Dish findById(long id) throws SQLException {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 8.2.2 查询某个分类下的所有菜品（所有状态），按价格升序，价格相同按 id 升序。
     */
    public List<Dish> findByCategory(long categoryId) throws SQLException {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 8.2.3 按名称模糊搜索（名称包含 keyword），按 id 升序。
     * 提示：SQL 写 name LIKE ?，参数传 "%" + keyword + "%"。不要写成 LIKE '%?%'，问号放在引号里就不是占位符了。
     */
    public List<Dish> searchByName(String keyword) throws SQLException {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 8.2.4 新增菜品（dish.id() 忽略，由数据库自增），create_time 用 NOW()，返回新生成的 id。
     * 提示：conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)，执行后 ps.getGeneratedKeys() 拿到 id。
     * description 可能为 null：ps.setString(i, null) 会写入 NULL。
     */
    public long insert(Dish dish) throws SQLException {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 8.2.5 修改价格，返回受影响的行数（id 不存在时为 0）。
     * 提示：ps.executeUpdate() 的返回值就是受影响行数。
     */
    public int updatePrice(long id, BigDecimal price) throws SQLException {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 8.2.6 删除，返回受影响的行数。
     */
    public int deleteById(long id) throws SQLException {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
