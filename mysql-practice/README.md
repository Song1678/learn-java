# mysql-practice：MySQL 练习（判题版）

learn-java 的第二阶段：**看教程 → 写 SQL → 跑测试判题**，玩法和 ch01~ch09 完全一样。
所有题目都基于同一个“迷你外卖”数据库（苍穹外卖简化版，11 张表），和你在 ch09 写过的业务一一对应，
做完正好衔接 MyBatis 和外卖项目。

```
m01 建库建表   m02 基础查询   m03 函数与聚合   m04 多表查询
m05 增删改     m06 事务与索引  m07 综合实战（统计报表 SQL）  m08 JDBC（Java 访问数据库）
```

共 **64 道 SQL 题**，外加 m08 的 4 组 Java 练习（JDBC）。

---

## 快速开始

1. **安装 MySQL 8** 和一个图形客户端，见 [docs/00-安装MySQL与客户端.md](docs/00-安装MySQL与客户端.md)
2. 修改 [db.properties](db.properties) 里的账号密码（默认 `root / 123456`，端口 3306）
3. 用 IDEA 打开 **`mysql-practice` 这个目录**（它是一个独立的 Maven 项目，有自己的 pom.xml）
4. 打开 `exercises/m02_select/q01_select_all.sql`，读题，在 `-- 在下面写你的 SQL：` 下面写 SQL
5. 运行测试判题：

```bash
cd mysql-practice

# 判某一章（推荐）
mvn test -Dtest='QueryExercisesTest#m02'     # m02 / m03 / m04 / m07 是查询题
mvn test -Dtest=M01DdlTest                   # m01 建表
mvn test -Dtest=M05DmlTest                   # m05 增删改
mvn test -Dtest=M06TxIndexTest               # m06 事务与索引
mvn test -Dtest=M08JdbcTest                  # m08 JDBC

# 判某一道题
mvn test -Dtest='M05DmlTest#q04'

# 用参考答案跑全部测试（应该全绿）
mvn test -Psolutions
```

IDEA 里也可以直接点测试类旁边的 ▶，查询题会按文件名列出每一道题的结果。

> ⚠️ 测试每次运行都会**删除并重建** `learn_sql_test` 库（以及 m01 用的 `learn_sql_ddl` 库），不要在这两个库里放自己的东西。

---

## 推荐的做题方式

1. **先读教程**：每章的指南（下表“指南”列）开头列出了要先读的教程章节
2. **在客户端里写和调试**：在 DBeaver / DataGrip 里新建一个库（比如 `takeout`），执行 `sql/schema.sql` 和 `sql/data.sql` 导入示例数据，在客户端里边写边看结果
3. **粘贴到练习文件里判题**：调好后把 SQL 复制到 `exercises/` 对应文件，运行测试
4. **对答案**：通过后打开 `solutions/` 里的同名文件，看看参考写法和【讲解】

每道查询题的文件头部都写了**期望结果**，可以先对照着自己检查。

---

## 目录结构

```
mysql-practice/
├── db.properties          ← 改成你的 MySQL 账号密码
├── sql/
│   ├── schema.sql         ← 外卖库表结构（11 张表）
│   └── data.sql           ← 示例数据（固定日期，方便核对）
├── exercises/             ← 你写 SQL 的地方（m01 ~ m07）
├── src/main/java/         ← m08 JDBC 练习（Java 填空）
├── solutions/             ← 参考答案（SQL + Java），含讲解
├── expected/              ← 查询题的期望结果（判题用，不要改）
├── src/test/java/         ← 判题测试（不用改）
├── docs/                  ← 安装说明、表结构说明、每章指南
└── docker-compose.yml     ← 可选：装了 Docker 的话可以用它一键启动 MySQL（没有就忽略）
```

想把某道题恢复成初始状态：`git checkout -- exercises/m02_select/q05_between.sql`

---

## 学习进度（做完一项把 ☐ 改成 ✅）

| 章节 | 指南 | 题目 | 判题 |
|---|---|---|---|
| ☐ m01 建库建表 | [docs/m01](docs/m01-建库建表.md) | 5 题：建表、数据类型、约束、外键、ALTER | `M01DdlTest` |
| ☐ m02 基础查询 | [docs/m02](docs/m02-基础查询.md) | 13 题：WHERE、LIKE、IS NULL、排序、分页、DISTINCT | `QueryExercisesTest#m02` |
| ☐ m03 函数与聚合 | [docs/m03](docs/m03-函数与聚合.md) | 13 题：COUNT/SUM/AVG、GROUP BY、HAVING、CASE、日期函数 | `QueryExercisesTest#m03` |
| ☐ m04 多表查询 | [docs/m04](docs/m04-多表查询.md) | 13 题：JOIN、LEFT JOIN、子查询、EXISTS、UNION | `QueryExercisesTest#m04` |
| ☐ m05 增删改 | [docs/m05](docs/m05-增删改.md) | 8 题：INSERT、UPDATE、DELETE、NOT IN 的坑、备份 | `M05DmlTest` |
| ☐ m06 事务与索引 | [docs/m06](docs/m06-事务与索引.md) | 4 题：事务、创建索引、最左前缀、索引失效 | `M06TxIndexTest` |
| ☐ m07 综合实战 | [docs/m07](docs/m07-综合实战.md) | 8 题：营业额/用户/订单统计、Top10、窗口函数 | `QueryExercisesTest#m07` |
| ☐ m08 JDBC | [docs/m08](docs/m08-JDBC.md) | 4 组：JDBC 流程、DAO、SQL 注入、手动事务 | `M08JdbcTest` |

建议节奏（每天 2~3 小时）：m01~m03 两三天，m04 两天，m05~m06 一两天，m07~m08 两三天，合计约 1.5~2 周。

其他文档：
- [docs/表结构说明.md](docs/表结构说明.md)：每张表是干什么的、表之间怎么关联（**做题前先看一遍**）
- [docs/常见报错.md](docs/常见报错.md)：做题时遇到报错先来这里查

做完之后回到上一级的 [docs/后续路线.md](../docs/后续路线.md)，下一步是 Spring Boot + MyBatis。
