# p01 MyBatis-Plus 入门：BaseMapper 与 IService

> 先看：[MyBatis-Plus 文档](https://baomidou.com/) “快速开始”“注解配置”“持久层接口”（Mapper 接口、Service 接口 两节）；回顾 ch09 的 `BaseMapper` / `InMemoryMapper`
> 练习：`src/main/java/com/learn/web/p01_mp/`　判题：`mvn test -Dtest=P01MybatisPlusTest`

## 和 ch09 对照

| ch09 你写的 | MyBatis-Plus |
|---|---|
| `interface BaseMapper<T>` | `com.baomidou.mybatisplus.core.mapper.BaseMapper<T>` |
| `class DishMapper extends InMemoryMapper<Dish>` | `interface DishMapper extends BaseMapper<Dish>`（实现类由 MP 生成，SQL 也由 MP 生成） |
| `insert`、`selectById`、`updateById`、`deleteById` | 同名方法，一模一样 |
| `selectList(Predicate<T> where)` | `selectList(Wrapper<T> wrapper)`（p02） |
| `updateById` 刷新 updateTime | 自动填充（p03） |

RuoYi-Vue-Plus 里的 Mapper 继承的是 `BaseMapperPlus<T, V>`，它也是继承自 `BaseMapper`，多加了一些转换 Vo 的方法。

## 实体类注解

```java
@TableName("dish")                    // 对应的表（类名驼峰转下划线就是表名时可以省略）
public class Dish {
    @TableId(type = IdType.AUTO)      // 主键，数据库自增。不写 type 默认是雪花算法（ASSIGN_ID）
    private Long id;
    private Long categoryId;          // 默认对应列 category_id，不用写注解
    @TableField("value")              // 字段名和列名对不上时才写
    private String flavorValue;
    @TableField(exist = false)        // 不是表里的列（比如为了返回给前端加的字段）
    private String categoryName;
}
```

## BaseMapper 常用方法

```java
mapper.insert(entity);                    // 插入后自增 id 自动回填到 entity
mapper.selectById(id);                    // 查不到返回 null
mapper.selectByIds(List.of(1L, 2L));      // 批量查（旧版本叫 selectBatchIds）
mapper.selectList(wrapper);               // 条件查询，wrapper 传 null 查全部
mapper.selectCount(wrapper);
mapper.updateById(entity);                // 只更新不为 null 的字段！
mapper.deleteById(id);
mapper.deleteByIds(ids);
mapper.exists(wrapper);
```

## IService / ServiceImpl

```java
public interface IDishMpService extends IService<Dish> { ... }

@Service
public class DishMpServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishMpService { ... }
```

继承之后自动有 `save`、`saveBatch`、`getById`、`listByIds`、`list`、`count`、`updateById`、`removeById`、`lambdaQuery()` …… 父类里的 `baseMapper` 就是注入好的 DishMapper。

**两种风格都要认识**：很多项目（包括苍穹外卖、MyBatis-Plus 官方示例）Service 继承 IService；
**RuoYi-Vue-Plus 的 Service 不继承 IService**，而是 `XxxServiceImpl implements IXxxService`，里面注入 `private final XxxMapper baseMapper;` 自己调用 —— x01 用的是这种。

## N+1 问题（练习 1.11）

查 10 个菜品，再循环 10 次 `categoryMapper.selectById(dish.getCategoryId())` 查分类名 —— 一共 1 + 10 次 SQL，这就是 N+1。
正确做法：收集所有 categoryId，`selectByIds` **一次**查出来转成 Map，再挨个 set。列表页数据一多，两种写法速度差几十倍。

## 练习

| 题 | 考点 |
|---|---|
| 1.1 | `@TableId(type = IdType.AUTO)`：不写的话 insert 后 id 是一个 19 位的数字 |
| 1.2 | 找 bug：`@TableField(exist = false)`（不修好，p01 所有菜品相关的测试都报 `Unknown column 'category_name'`） |
| 1.3 ~ 1.8 | `CategoryMpService`：BaseMapper 增删改查 |
| 1.9 ~ 1.10 | `DishMpServiceImpl`：IService 的 lambdaQuery 链式查询 |
| 1.11 | IService 自带方法 + 避免 N+1 |

## 常见坑

1. `updateById` 前先 `selectById` 查出旧对象，改一个字段再更新 —— 能用，但会把所有字段都 UPDATE 一遍。只改一个字段时 new 一个只有 id 和该字段的对象更好。
2. `updateById` 想把某个字段改成 null，是改不了的（null 字段被跳过）→ 用 p02 的 `LambdaUpdateWrapper.set(字段, null)`。
3. `selectOne` / `.one()` 查出多条会抛异常 `TooManyResultsException`，只在“最多一条”的场景用。
