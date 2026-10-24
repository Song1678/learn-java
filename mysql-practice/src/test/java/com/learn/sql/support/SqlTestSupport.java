package com.learn.sql.support;

import com.learn.sql.DbConfig;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * 判题工具：重建数据库、读取并执行你写的 SQL 文件、比对查询结果。
 * 你不需要修改这个类，但可以读一读，里面全是 JDBC 的用法（m08 会学）。
 */
public final class SqlTestSupport {

    /** 和 MySQL 8 默认值一致，保证每个人的判题环境相同 */
    private static final String SQL_MODE = "ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,"
            + "NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION";

    /** 写 SQL 的位置从这一行之后开始 */
    public static final String ANSWER_MARKER = "-- 在下面写你的 SQL：";

    private static String connectError;

    private SqlTestSupport() {
    }

    // ------------------------------------------------------------------ 连接与建库

    /** SQL 文件所在目录：默认 exercises，-Psolutions 时为 solutions */
    public static Path sqlDir() {
        return Path.of(System.getProperty("sql.dir", "exercises"));
    }

    /** 连接到 MySQL 服务器（不选库），连不上时给出中文提示 */
    public static Connection serverConnection() {
        return open(null);
    }

    /** 连接到练习库 */
    public static Connection connection() {
        return open(DbConfig.database());
    }

    /** 连接到指定库 */
    public static Connection connection(String database) {
        return open(database);
    }

    private static Connection open(String database) {
        if (connectError != null) {
            fail(connectError);
        }
        try {
            Connection conn = DbConfig.getConnection(database);
            try (Statement st = conn.createStatement()) {
                st.execute("SET SESSION sql_mode = '" + SQL_MODE + "'");
            }
            return conn;
        } catch (SQLException e) {
            connectError = """
                    连接不上 MySQL（%s）：%s
                    请检查：
                      1. MySQL 服务是否已经启动（Windows：服务里的 MySQL80；Mac：brew services list）
                      2. mysql-practice/db.properties 里的账号、密码、端口是否正确
                      3. 用 DBeaver / 命令行 mysql -uroot -p 能不能连上
                    """.formatted(DbConfig.describe(), e.getMessage());
            fail(connectError);
            return null;
        }
    }

    /** 删除并重建练习库，导入 sql/schema.sql 和 sql/data.sql */
    public static void resetDatabase() {
        recreate(DbConfig.database());
        try (Connection conn = connection()) {
            executeScript(conn, read(Path.of("sql/schema.sql")));
            executeScript(conn, read(Path.of("sql/data.sql")));
        } catch (SQLException e) {
            fail("初始化数据库失败：" + e.getMessage(), e);
        }
    }

    /** 删除并重建一个空库 */
    public static void recreate(String database) {
        try (Connection conn = serverConnection(); Statement st = conn.createStatement()) {
            st.execute("DROP DATABASE IF EXISTS `" + database + "`");
            st.execute("CREATE DATABASE `" + database + "` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_0900_ai_ci");
        } catch (SQLException e) {
            fail("重建数据库 " + database + " 失败：" + e.getMessage(), e);
        }
    }

    // ------------------------------------------------------------------ 读文件、拆语句

    public static String read(Path file) {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail("找不到文件：" + file.toAbsolutePath());
            return null;
        }
    }

    /**
     * 读取练习文件里你写的 SQL（只取 "-- 在下面写你的 SQL：" 之后的部分），拆成一条条语句。
     * 还没写的话直接判失败。
     */
    public static List<String> answerStatements(String relativePath) {
        Path file = sqlDir().resolve(relativePath);
        String text = read(file);
        int idx = text.indexOf(ANSWER_MARKER);
        String answer = idx >= 0 ? text.substring(idx + ANSWER_MARKER.length()) : text;
        List<String> statements = split(answer);
        if (statements.isEmpty()) {
            fail(file + " 还没有写 SQL");
        }
        return statements;
    }

    /**
     * 把一段 SQL 拆成多条语句：去掉注释，按分号切分（会跳过引号里的分号）。
     */
    public static List<String> split(String sql) {
        List<String> result = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        int n = sql.length();
        char quote = 0;
        for (int i = 0; i < n; i++) {
            char c = sql.charAt(i);
            if (quote != 0) {
                cur.append(c);
                if (c == '\\' && i + 1 < n) {
                    cur.append(sql.charAt(++i));
                } else if (c == quote) {
                    quote = 0;
                }
            } else if (c == '\'' || c == '"' || c == '`') {
                quote = c;
                cur.append(c);
            } else if (c == '-' && i + 1 < n && sql.charAt(i + 1) == '-') {
                while (i < n && sql.charAt(i) != '\n') {
                    i++;
                }
                cur.append('\n');
            } else if (c == '#') {
                while (i < n && sql.charAt(i) != '\n') {
                    i++;
                }
                cur.append('\n');
            } else if (c == '/' && i + 1 < n && sql.charAt(i + 1) == '*') {
                int end = sql.indexOf("*/", i + 2);
                i = end < 0 ? n : end + 1;
                cur.append(' ');
            } else if (c == ';') {
                addIfNotBlank(result, cur);
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        addIfNotBlank(result, cur);
        return result;
    }

    private static void addIfNotBlank(List<String> list, StringBuilder sb) {
        String s = sb.toString().trim();
        if (!s.isEmpty()) {
            list.add(s);
        }
    }

    public static void executeScript(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement()) {
            for (String s : split(sql)) {
                st.execute(s);
            }
        }
    }

    // ------------------------------------------------------------------ 执行与结果

    /** 一次查询的结果：列名 + 每行的值（都转成字符串，NULL 用 "NULL" 表示） */
    public record Result(List<String> columns, List<List<String>> rows) {

        /** 画成表格，出错时给你看 */
        public String toTable() {
            StringBuilder sb = new StringBuilder();
            sb.append("| ").append(String.join(" | ", columns)).append(" |\n");
            int shown = Math.min(rows.size(), 30);
            for (int i = 0; i < shown; i++) {
                sb.append("| ").append(String.join(" | ", rows.get(i))).append(" |\n");
            }
            if (rows.size() > shown) {
                sb.append("... 共 ").append(rows.size()).append(" 行\n");
            }
            sb.append("(").append(rows.size()).append(" 行)");
            return sb.toString();
        }
    }

    /**
     * 执行你写的全部语句，返回最后一条语句的查询结果（最后一条不是查询时返回 null）。
     * SQL 报错时直接判失败，并显示是哪一条语句、MySQL 的错误信息。
     */
    public static Result runAnswer(Connection conn, String relativePath) {
        List<String> statements = answerStatements(relativePath);
        Result last = null;
        for (String s : statements) {
            try {
                last = execute(conn, s);
            } catch (SQLException e) {
                fail("执行出错：" + e.getMessage() + "\n出错的语句：\n" + s);
            }
        }
        return last;
    }

    /** 执行一条语句；是查询则返回结果 */
    public static Result execute(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement()) {
            boolean isQuery = st.execute(sql);
            if (!isQuery) {
                return null;
            }
            try (ResultSet rs = st.getResultSet()) {
                return toResult(rs);
            }
        }
    }

    /** 执行一条查询（测试自己用来检查表里的数据） */
    public static Result query(Connection conn, String sql) {
        try {
            return execute(conn, sql);
        } catch (SQLException e) {
            fail("检查数据时执行出错：" + e.getMessage() + "\nSQL：" + sql);
            return null;
        }
    }

    public static Result toResult(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        List<String> columns = new ArrayList<>();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            columns.add(md.getColumnLabel(i));
        }
        List<List<String>> rows = new ArrayList<>();
        while (rs.next()) {
            List<String> row = new ArrayList<>();
            for (int i = 1; i <= columns.size(); i++) {
                String v = rs.getString(i);
                row.add(v == null ? "NULL" : v);
            }
            rows.add(row);
        }
        return new Result(columns, rows);
    }

    /** 方便测试里写期望行：row("宫保鸡丁", "28.00") */
    public static List<String> row(String... values) {
        return List.of(values);
    }

    // ------------------------------------------------------------------ 比对

    /** 读取 expected/ 目录里的期望结果文件（第一行 "# ordered" 或 "# unordered"，第二行列名，之后是数据，Tab 分隔） */
    public static Expected readExpected(Path tsv) {
        List<String> lines = read(tsv).lines().toList();
        boolean ordered = lines.get(0).trim().equals("# ordered");
        List<String> columns = splitTsv(lines.get(1));
        List<List<String>> rows = new ArrayList<>();
        for (String line : lines.subList(2, lines.size())) {
            if (!line.isEmpty()) {
                rows.add(splitTsv(line));
            }
        }
        return new Expected(ordered, new Result(columns, rows));
    }

    public record Expected(boolean ordered, Result result) {
    }

    private static List<String> splitTsv(String line) {
        List<String> values = new ArrayList<>();
        for (String v : line.split("\t", -1)) {
            values.add(v.replace("\\t", "\t").replace("\\n", "\n").replace("\\\\", "\\"));
        }
        return values;
    }

    /**
     * 比较查询结果。
     * <ul>
     *   <li>列数和列名必须一致（列名不区分大小写）—— 题目要求的别名要写对</li>
     *   <li>数字按数值比较：28、28.0、28.00 视为相同</li>
     *   <li>ordered=false 时不比较行的顺序</li>
     * </ul>
     */
    public static void assertResult(Result expected, Result actual, boolean ordered) {
        if (actual == null) {
            fail("你的最后一条语句不是查询（SELECT），没有返回结果");
        }
        String both = "\n期望结果：\n" + expected.toTable() + "\n\n你的结果：\n" + actual.toTable();
        if (expected.columns().size() != actual.columns().size()) {
            fail("列的数量不对，期望 " + expected.columns().size() + " 列，实际 " + actual.columns().size() + " 列" + both);
        }
        for (int i = 0; i < expected.columns().size(); i++) {
            if (!expected.columns().get(i).equalsIgnoreCase(actual.columns().get(i))) {
                fail("第 " + (i + 1) + " 列的列名不对，期望 " + expected.columns().get(i)
                        + "，实际 " + actual.columns().get(i) + "（检查列的顺序和别名 AS）" + both);
            }
        }
        if (expected.rows().size() != actual.rows().size()) {
            fail("行数不对，期望 " + expected.rows().size() + " 行，实际 " + actual.rows().size() + " 行" + both);
        }
        List<List<String>> exp = expected.rows();
        List<List<String>> act = actual.rows();
        if (!ordered) {
            exp = sorted(exp);
            act = sorted(act);
        }
        for (int r = 0; r < exp.size(); r++) {
            for (int c = 0; c < exp.get(r).size(); c++) {
                if (!sameValue(exp.get(r).get(c), act.get(r).get(c))) {
                    fail((ordered ? "第 " + (r + 1) + " 行" : "数据") + "的 " + expected.columns().get(c)
                            + " 不对：期望 " + exp.get(r).get(c) + "，实际 " + act.get(r).get(c)
                            + (ordered ? "（这道题要求排序，也检查一下 ORDER BY）" : "") + both);
                }
            }
        }
    }

    private static List<List<String>> sorted(List<List<String>> rows) {
        return rows.stream()
                .sorted(Comparator.comparing(r -> String.join("\u0001", r.stream().map(SqlTestSupport::normalize).toList())))
                .toList();
    }

    private static String normalize(String v) {
        try {
            BigDecimal d = new BigDecimal(v);
            return d.signum() == 0 ? "0" : d.stripTrailingZeros().toPlainString();
        } catch (NumberFormatException e) {
            return v;
        }
    }

    private static boolean sameValue(String a, String b) {
        return normalize(a).equals(normalize(b));
    }

    /** 列出某个目录下所有期望结果文件（按文件名排序） */
    public static List<Path> expectedFiles(String chapter) {
        Path dir = Path.of("expected", chapter);
        try (Stream<Path> s = Files.list(dir)) {
            return s.filter(p -> p.toString().endsWith(".tsv")).sorted().toList();
        } catch (IOException e) {
            fail("找不到目录：" + dir.toAbsolutePath());
            return List.of();
        }
    }
}
