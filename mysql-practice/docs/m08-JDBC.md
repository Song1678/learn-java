# m08 JDBC：用 Java 访问数据库

> 先读：廖雪峰 **Java 教程** → “JDBC编程”（JDBC简介、JDBC查询、JDBC更新、JDBC事务、JDBC Batch，连接池先略读）
> 练习：`src/main/java/com/learn/sql/m08_jdbc/`（Java 填空，和 learn-java 一样）　判题：`mvn test -Dtest=M08JdbcTest`

## 为什么要学 JDBC

MyBatis / MyBatis-Plus 用起来只需要写一个接口和一条 SQL，但它底层做的就是本章这些事：
获取连接 → 准备语句 → 设置参数 → 执行 → 把结果集转换成 Java 对象 → 关闭资源 → 管理事务。
自己写一遍，以后用框架时出了问题才知道去哪里找原因。

## 练习

| 文件 | 考点 |
|---|---|
| `JdbcBasics` | DriverManager、Statement、ResultSet、try-with-resources |
| `DishDao` | PreparedStatement 传参、结果映射成 record、获取自增主键、executeUpdate 返回行数 |
| `EmployeeDao` | **SQL 注入**：看测试里拼接 SQL 是怎么被攻破的，再用 PreparedStatement 修好 |
| `OrderSubmitService` | 手动事务：setAutoCommit(false) / commit / rollback、批量插入 |

## 速记

```java
try (Connection conn = DbConfig.getConnection();
     PreparedStatement ps = conn.prepareStatement("SELECT id, name, price FROM dish WHERE category_id = ?")) {
    ps.setLong(1, categoryId);
    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Long id = rs.getLong("id");
            BigDecimal price = rs.getBigDecimal("price");
        }
    }
}
```

## 和 MyBatis 的对应

| JDBC（本章） | MyBatis |
|---|---|
| `"... WHERE id = ?"` + `ps.setLong(1, id)` | `WHERE id = #{id}` |
| 字符串拼接 SQL（危险） | `${xxx}`（危险，只能用于表名/排序字段等非用户输入） |
| `mapRow(rs)` 手动转对象 | `resultType` / `resultMap` / 驼峰映射自动转换 |
| `RETURN_GENERATED_KEYS` | `useGeneratedKeys="true" keyProperty="id"` |
| `setAutoCommit(false)` / `commit` / `rollback` | Spring 的 `@Transactional` |
| 每次 `DriverManager.getConnection` | 连接池（HikariCP），Spring Boot 默认配置好 |

## 常见坑

1. 资源（Connection / Statement / ResultSet）必须关闭，用 try-with-resources。
2. `rs.getLong()` 遇到 NULL 返回 0，要区分 NULL 用 `rs.getObject("col", Long.class)`。
3. `LIKE ?` 的参数要自己加 `%`：`ps.setString(1, "%" + keyword + "%")`。
4. `LocalDateTime` 用 `ps.setObject` / `rs.getObject(col, LocalDateTime.class)`；用 `Timestamp` 可能因为时区设置差几个小时。
5. 回滚之后要把异常继续抛出去，不能“吞掉”，否则调用方以为成功了。
