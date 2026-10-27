package com.learn.web.support;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * 需要数据库的测试的基类（判题工具，不用改）。
 *
 * <p>{@code @Sql}：每个测试方法执行之前，先执行 sql/schema.sql 和 sql/data.sql，
 * 把 learn_web_test 库重置成示例数据，所以测试之间互不影响，你也可以放心地反复跑。
 */
@Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public abstract class DbTestBase extends WebTestBase {

    @Autowired
    protected JdbcTemplate jdbc;

    private static String connectError;

    /** 先用最简单的 JDBC 连一下 MySQL，连不上就给出中文提示，免得看一大堆英文报错 */
    @BeforeAll
    static void checkMysql() {
        if (connectError == null) {
            connectError = tryConnect();
        }
        if (!connectError.isEmpty()) {
            fail(connectError);
        }
    }

    private static String tryConnect() {
        Properties p = new Properties();
        Path file = Path.of("db.properties");
        if (Files.exists(file)) {
            try (InputStream in = Files.newInputStream(file)) {
                p.load(in);
            } catch (Exception ignored) {
                // 读不到就用默认值
            }
        }
        String host = value(p, "DB_HOST", "db.host", "localhost");
        String port = value(p, "DB_PORT", "db.port", "3306");
        String user = value(p, "DB_USERNAME", "db.username", "root");
        String password = value(p, "DB_PASSWORD", "db.password", "123456");
        String url = "jdbc:mysql://" + host + ":" + port + "/?allowPublicKeyRetrieval=true&useSSL=false&connectTimeout=3000";
        try (Connection ignored = DriverManager.getConnection(url, user, password)) {
            return "";
        } catch (Exception e) {
            return """
                    连接不上 MySQL（%s:%s，用户 %s）：%s
                    请检查：
                      1. MySQL 服务是否已经启动（Windows：服务里的 MySQL80；Mac：brew services list；Docker：docker compose up -d）
                      2. springboot-practice/db.properties 里的账号、密码、端口是否正确
                      3. 用 DBeaver / 命令行 mysql -uroot -p 能不能连上
                    """.formatted(host, port, user, e.getMessage());
        }
    }

    private static String value(Properties p, String env, String key, String def) {
        String v = System.getenv(env);
        if (v != null && !v.isBlank()) {
            return v;
        }
        return p.getProperty(key, def).trim();
    }

    // ------------------------------------------------------------------ 直接查库，检查你的代码有没有真的改到数据库

    protected long count(String sql, Object... args) {
        Long n = jdbc.queryForObject(sql, Long.class, args);
        return n == null ? 0 : n;
    }

    protected Map<String, Object> row(String sql, Object... args) {
        List<Map<String, Object>> rows = jdbc.queryForList(sql, args);
        return rows.isEmpty() ? null : rows.get(0);
    }

    protected <T> List<T> column(String sql, Class<T> type, Object... args) {
        return jdbc.queryForList(sql, type, args);
    }
}
