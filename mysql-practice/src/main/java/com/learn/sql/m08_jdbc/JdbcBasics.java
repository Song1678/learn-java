package com.learn.sql.m08_jdbc;

import com.learn.sql.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 练习 8.1：JDBC 基本流程（对应廖雪峰「JDBC编程 - JDBC简介 / JDBC查询」）
 *
 * <p>JDBC 是 Java 访问数据库的标准接口，MyBatis、MyBatis-Plus 底层都是 JDBC。固定步骤：
 * <pre>
 * 1. 获取连接 Connection    DriverManager.getConnection(url, 用户名, 密码)
 * 2. 创建语句 Statement     conn.createStatement()
 * 3. 执行 SQL 得到 ResultSet  stmt.executeQuery(sql)
 * 4. 遍历结果               while (rs.next()) { rs.getString("列名") ... }
 * 5. 关闭资源（用 try-with-resources 自动关闭，learn-java ch03 练过）
 * </pre>
 * 连接参数从 {@link DbConfig} 获取：DbConfig.url(DbConfig.database())、DbConfig.username()、DbConfig.password()。
 * 遇到 SQLException 直接往外抛（方法签名已经写了 throws SQLException）。
 */
public class JdbcBasics {

    private JdbcBasics() {
    }

    /**
     * 8.1.1 用 DriverManager 获取连接，查询 dish 表一共有多少行。
     * 提示：SELECT COUNT(*) FROM dish 只有一行一列，rs.next() 一次，然后 rs.getInt(1)（列序号从 1 开始）
     */
    public static int countDishes() throws SQLException {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 8.1.2 查询所有【启用】分类的名称，按 sort 升序。
     * 这次可以直接用 DbConfig.getConnection() 获取连接（里面就是 8.1.1 的写法）。
     */
    public static List<String> enabledCategoryNames() throws SQLException {
        // TODO 提示：while (rs.next()) { list.add(rs.getString("name")); }
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
