package com.learn.sql.m08_jdbc;

import com.learn.sql.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 练习 8.3：SQL 注入
 *
 * <p>员工登录：用户名和密码都对，返回员工姓名；否则返回 null。
 * （真实项目里密码是加密存储的，这里为了演示直接存明文。）
 */
public class EmployeeDao {

    /**
     * ❌ 反面教材（已写好，不要改）：用字符串拼接 SQL。
     *
     * <p>正常输入：username = admin，password = 123456，拼出来是
     * <pre>SELECT name FROM employee WHERE username = 'admin' AND password = '123456'</pre>
     * 攻击输入：username = admin，password = ' OR '1'='1，拼出来是
     * <pre>SELECT name FROM employee WHERE username = 'admin' AND password = '' OR '1'='1'</pre>
     * 不知道密码也能登录成功！测试里会演示这一点。
     */
    public String loginUnsafe(String username, String password) throws SQLException {
        String sql = "SELECT name FROM employee WHERE username = '" + username
                + "' AND password = '" + password + "' ORDER BY id LIMIT 1";
        try (Connection conn = DbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getString("name") : null;
        }
    }

    /**
     * ✅ 8.3 用 PreparedStatement 实现同样的功能，让上面的攻击失效。另外，【禁用】的员工（status = 0）不能登录。
     *
     * <p>PreparedStatement 会把参数当成纯数据处理，' OR '1'='1 只会被当作一个奇怪的密码字符串。
     * MyBatis 里的 #{password} 就是 PreparedStatement 的 ?；而 ${password} 是字符串拼接 —— 面试必问，工作中禁用 ${} 拼接用户输入。
     */
    public String login(String username, String password) throws SQLException {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
