# b02 XML 映射文件与动态 SQL

> 先看：黑马《JavaWeb 开发教程》MyBatis XML 映射文件、动态 SQL；[MyBatis 中文文档](https://mybatis.org/mybatis-3/zh_CN/index.html) “XML 映射器”“动态 SQL”
> 练习：`src/main/resources/mapper/b02/DishMapper.xml`（SQL）+ `src/main/java/com/learn/web/b02_dynamic/DishSortService.java`
> 判题：`mvn test -Dtest=B02DynamicSqlTest`

## XML 映射文件

```xml
<mapper namespace="com.learn.web.b02_dynamic.DishMapper">     <!-- 接口的全类名 -->
    <select id="selectList" resultType="com.learn.web.b02_dynamic.Dish">   <!-- id = 方法名 -->
        SELECT * FROM dish ...
    </select>
</mapper>
```

- `namespace` + `id` 对应接口的方法；`resultType` 是**一行**结果对应的类型（返回 List 也写元素类型）
- application.yml 里 `mybatis-plus.mapper-locations: classpath*:mapper/**/*.xml` 告诉 MyBatis 去哪找 XML
- IDEA 装个 **MyBatisX** 插件，可以在接口方法和 XML 之间点击跳转

## 动态 SQL

```xml
<sql id="whereClause">                                   <!-- 可复用的 SQL 片段 -->
    <where>                                              <!-- 有条件才加 WHERE，并去掉开头多余的 AND / OR -->
        <if test="name != null and name != ''">           <!-- test 里写的是参数的字段名，不是列名 -->
            AND name LIKE CONCAT('%', #{name}, '%')
        </if>
        <if test="categoryId != null">AND category_id = #{categoryId}</if>
        <if test="maxPrice != null">AND price &lt;= #{maxPrice}</if>      <!-- XML 里 < 要写成 &lt; -->
    </where>
</sql>
<select id="selectList" resultType="...Dish">
    SELECT * FROM dish <include refid="whereClause"/> ORDER BY id
</select>

<update id="updateSelective">
    UPDATE dish
    <set>                                                <!-- 加 SET，并去掉最后多余的逗号 -->
        <if test="name != null">name = #{name},</if>
        <if test="price != null">price = #{price},</if>
        update_time = NOW()
    </set>
    WHERE id = #{id}
</update>

<delete id="deleteByIds">
    DELETE FROM dish WHERE id IN
    <foreach collection="ids" item="id" open="(" separator="," close=")">#{id}</foreach>
</delete>

<choose>                                                  <!-- if / else if / else -->
    <when test="asc">ASC</when>
    <otherwise>DESC</otherwise>
</choose>
```

## #{} 和 ${}（面试必问）

| | `#{}` | `${}` |
|---|---|---|
| 原理 | 预编译参数，变成 `?` | 字符串直接拼接进 SQL |
| SQL 注入 | 安全 | **危险** |
| 用在哪 | 所有“值” | 只能用于表名、列名、ORDER BY 这种不能用 `?` 的地方 |

`ORDER BY #{column}` 会变成 `ORDER BY 'price'`（按一个常量字符串排序，等于没排序），所以排序列只能用 `${}`。
这时**列名必须来自白名单**，绝不能直接用前端传来的字符串 —— 练习 2.8 就是干这个的。

## 练习

| 题 | 考点 |
|---|---|
| 2.1 ~ 2.2 | `<sql>` + `<include>` + `<where>` + `<if>`：条件查询和条件计数共用一份条件 |
| 2.3 | `<set>` 选择性更新（MyBatis-Plus 的 updateById 就是这么干的） |
| 2.4 ~ 2.6 | `<foreach>`：IN 查询、批量删除、批量插入 |
| 2.7 | `<choose>` + `${}` 动态排序 |
| 2.8 | `DishSortService`：排序字段白名单（Java） |

## 常见坑

1. **XML 写错会导致整个项目启动失败**，所有测试都红。看报错里的 `Error parsing Mapper XML` / `DishMapper.xml` 和行号。
2. `元素内容必须由格式正确的字符数据或标记组成` → SQL 里直接写了 `<`，改成 `&lt;` 或用 `<![CDATA[ ... ]]>` 包起来。
3. `<if test="name != ''">`：test 里是 OGNL 表达式，字符串用单引号，“并且”写 `and`（不是 `&&`）。
4. 字符串判空要判两次：`name != null and name != ''`，否则前端传了空字符串会变成 `LIKE '%%'`。
5. `<foreach collection="ids">` 的名字要和接口方法里 `@Param("ids")` 一致。
6. 同一个方法既写了注解又写了 XML → 启动报 `Mapped Statements collection already contains value`。
