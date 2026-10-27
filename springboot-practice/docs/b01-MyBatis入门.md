# b01 MyBatis 入门：注解 SQL

> 先看：mysql-practice 的 [m08 JDBC](../../mysql-practice/docs/m08-JDBC.md)（最后那张“JDBC 和 MyBatis 的对应”表）；
> 黑马《JavaWeb 开发教程》MyBatis 入门、MyBatis 基础操作（增删改查）；[MyBatis 中文文档](https://mybatis.org/mybatis-3/zh_CN/index.html) “入门”“Java API → 映射器注解”
> 练习：`src/main/java/com/learn/web/b01_mybatis/`　判题：`mvn test -Dtest=B01MyBatisTest`

从这一章开始需要 MySQL。数据库就是 mysql-practice 的那套外卖库，表结构见 [mysql-practice/docs/表结构说明.md](../../mysql-practice/docs/表结构说明.md)。

## MyBatis 做了什么

```java
@Mapper                                   // 告诉 MyBatis：给这个接口生成实现类，放进 Spring 容器
public interface CategoryMapper {
    @Select("SELECT * FROM category WHERE id = #{id}")
    Category selectById(Long id);
}
```

调用 `categoryMapper.selectById(2L)` 时，MyBatis 帮你：

1. 从连接池拿一个连接（Spring Boot 默认用 HikariCP 连接池，配置在 application.yml 的 `spring.datasource`）
2. 把 `#{id}` 换成 `?`，`prepareStatement`，`setLong(1, 2L)` —— 所以 `#{}` 能防 SQL 注入
3. 执行，把结果集的每一列按名字装进 `Category` 的同名字段（`create_time` → `createTime`，靠驼峰映射）
4. 关闭资源

这些就是你在 m08 的 `DishDao` 里手写的代码。

## 速记

```java
@Select("SELECT * FROM category WHERE type = #{type} AND status = #{status}")
List<Category> selectByTypeAndStatus(@Param("type") Integer type, @Param("status") Integer status);

@Select("SELECT COUNT(*) FROM category WHERE status = #{status}")
long countByStatus(Integer status);                         // 返回简单类型

@Select("SELECT * FROM category WHERE name LIKE CONCAT('%', #{keyword}, '%')")
List<Category> searchByName(String keyword);                // 模糊查询

@Insert("INSERT INTO category (type, name, sort, status, create_time, update_time) " +
        "VALUES (#{type}, #{name}, #{sort}, #{status}, NOW(), NOW())")
@Options(useGeneratedKeys = true, keyProperty = "id")       // 插入后把自增 id 回填到 category.id
int insert(Category category);                              // 参数是对象：#{name} 就是 category.getName()

@Update("UPDATE category SET sort = #{sort}, update_time = NOW() WHERE id = #{id}")
int updateSort(@Param("id") Long id, @Param("sort") Integer sort);   // 返回影响的行数

@Delete("DELETE FROM category WHERE id = #{id}")
int deleteById(Long id);
```

**返回值类型**：查一条用实体类（查不到是 null），查多条用 `List<实体>`（查不到是空 List），
`COUNT(*)` 用 `long`，只查一列用 `List<String>` 等，增删改返回 `int`（影响的行数）。

**@Param**：多个参数时给参数起名字，SQL 里用 `#{名字}` 引用。
（Spring Boot 编译时加了 `-parameters`，其实不写 @Param 也能用参数名，但写上更保险，大部分项目都写。）

## 练习

| 题 | 考点 |
|---|---|
| 1.1 ~ 1.2 | @Select 查一条、查多条、ORDER BY |
| 1.3 | 多个参数 @Param |
| 1.4 ~ 1.5 | 返回简单类型 |
| 1.6 | LIKE + CONCAT |
| 1.7 | @Insert + @Options 回填自增 id |
| 1.8 ~ 1.9 | @Update、@Delete |
| 1.10 | `CategoryService` 注入 Mapper 并使用（对照 ch09 的 CategoryService） |

做题时可以开着 DBeaver，先在客户端里把 SQL 调对，再粘贴到注解里。

## 常见坑

1. `Invalid bound statement (not found): xxx.CategoryMapper.selectAll` → 这个方法还没写 SQL 注解（或者 b02 的 XML 里 id 写错了）。
2. `LIKE '%#{keyword}%'` 是错的：`#{}` 在引号里面不会被替换。用 `CONCAT('%', #{keyword}, '%')`。
3. `Parameter 'xxx' not found. Available parameters are [arg1, arg0, param1, param2]` → 多参数没写 @Param，或者 #{} 里的名字和 @Param 不一致。
4. 插入后 id 还是 null → 忘了 `@Options(useGeneratedKeys = true, keyProperty = "id")`。
5. 查出来的 `createTime` 是 null → 列名和字段名对不上：确认开了驼峰映射（本项目已开），或者 SQL 里起别名 `create_time AS createTime`。
6. 注解里的 SQL 拼接时，两段字符串之间要有空格：`"... update_time) " + "VALUES ..."`。
