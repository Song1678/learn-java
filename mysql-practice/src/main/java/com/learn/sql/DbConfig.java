package com.learn.sql;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 读取 db.properties，提供数据库连接（已写好）。
 *
 * <p>优先级：环境变量 DB_HOST / DB_PORT / DB_USER / DB_PASSWORD &gt; db.properties &gt; 默认值。
 */
public final class DbConfig {

    private static final Properties PROPS = load();

    private DbConfig() {
    }

    private static Properties load() {
        Properties p = new Properties();
        Path file = Path.of(System.getProperty("db.config", "db.properties"));
        if (Files.exists(file)) {
            try (InputStream in = Files.newInputStream(file)) {
                p.load(in);
            } catch (IOException e) {
                throw new IllegalStateException("读取 " + file.toAbsolutePath() + " 失败", e);
            }
        }
        return p;
    }

    private static String get(String env, String key, String def) {
        String v = System.getenv(env);
        if (v != null && !v.isBlank()) {
            return v;
        }
        return PROPS.getProperty(key, def).trim();
    }

    public static String host() {
        return get("DB_HOST", "db.host", "localhost");
    }

    public static String port() {
        return get("DB_PORT", "db.port", "3306");
    }

    public static String username() {
        return get("DB_USER", "db.username", "root");
    }

    public static String password() {
        return get("DB_PASSWORD", "db.password", "123456");
    }

    public static String database() {
        return get("DB_NAME", "db.database", "learn_sql_test");
    }

    /**
     * 拼出 JDBC URL。database 为空时只连到 MySQL 服务器，不选数据库。
     * 参数说明：
     * useSSL=false 本地练习不用 SSL；allowPublicKeyRetrieval=true MySQL 8 默认认证方式需要；
     * serverTimezone=Asia/Shanghai 时区；characterEncoding=utf8 中文不乱码。
     */
    public static String url(String database) {
        return "jdbc:mysql://" + host() + ":" + port() + "/" + (database == null ? "" : database)
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai"
                + "&characterEncoding=utf8&connectTimeout=3000";
    }

    /** 连接到练习数据库（db.database） */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url(database()), username(), password());
    }

    /** 连接到指定数据库 */
    public static Connection getConnection(String database) throws SQLException {
        return DriverManager.getConnection(url(database), username(), password());
    }

    /** 描述当前连接配置，用于出错提示 */
    public static String describe() {
        return username() + "@" + host() + ":" + port();
    }
}
