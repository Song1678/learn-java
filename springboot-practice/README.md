# springboot-practice：Spring Boot + MyBatis + MyBatis-Plus 练习（判题版）

learn-java 阶段三-A 的第二部分（第一部分是 [mysql-practice](../mysql-practice/README.md)）。
玩法还是一样：**看指南 → 填代码 → 跑测试判题，全部变绿就算通过**。

所有练习都基于同一个“迷你外卖”数据库（和 mysql-practice 是同一套表和数据），
最后的综合项目是一套**和 RuoYi-Vue-Plus 代码生成器生成的代码几乎一模一样**的外卖后台接口，做完就可以进入阶段三-B / 三-C。

```
Web 基础    w01 Spring Boot 入门   w02 请求与响应   w03 IoC 与依赖注入   w04 统一异常处理与参数校验
MyBatis    b01 注解 SQL          b02 XML 与动态 SQL   b03 结果映射与多表查询   b04 事务
MyBatis-Plus p01 BaseMapper / IService   p02 条件构造器   p03 分页、自动填充、逻辑删除
综合项目    x01 外卖后台：分类管理 + 菜品管理（含口味）+ 营业额统计 / 销量 Top10
```

共 **96 个判题测试**。每天 5~6 小时的话，大约 **9 天**做完；加上 mysql-practice 一共约 **2 周**，
详细的每日安排见 **[docs/学习计划.md](docs/学习计划.md)**。

---

## 快速开始

1. 准备好 JDK 17、Maven、IDEA、MySQL 8（做 mysql-practice 时已经装好了），详见 [docs/00-环境准备.md](docs/00-环境准备.md)
2. 修改 [db.properties](db.properties) 里的 MySQL 账号密码（默认 `root / 123456`，和 mysql-practice 一样）
3. 用 IDEA 打开 **`springboot-practice` 这个目录**（独立的 Maven 项目），等右下角 Maven 下载完依赖（第一次要几分钟）
4. 运行 `src/main/java/com/learn/web/WebPracticeApplication.java` 的 main 方法，控制台出现
   `Started WebPracticeApplication` 就说明启动成功了。这时浏览器打开 http://localhost:8080/w01/hello 会看到
   `Whitelabel Error Page`（404），这是正常的 —— 这个接口就是你在 w01 第一题要写的
5. 打开 [docs/w01-SpringBoot入门.md](docs/w01-SpringBoot入门.md)，开始做题。判题：

```bash
cd springboot-practice

# 判某一章（推荐）
mvn test -Dtest=W01HelloTest        # w01
mvn test -Dtest='W0*'               # 整个 Web 部分（w01~w04，不需要数据库）
mvn test -Dtest='B0*'               # MyBatis 部分
mvn test -Dtest='P0*'               # MyBatis-Plus 部分
mvn test -Dtest='X01*'              # 综合项目

# 判某一题
mvn test -Dtest='W02RestTest#page'

# 用参考答案跑全部测试（应该全绿，用来确认题目本身没问题）
mvn test -Psolutions

# 启动项目（也可以直接在 IDEA 里运行 WebPracticeApplication）
mvn spring-boot:run
```

IDEA 里也可以直接点测试类旁边的绿色 ▶。测试类在 `src/test/java/com/learn/web/`，名字和章节对应：`W01HelloTest`、`B02DynamicSqlTest`……

> ⚠️ 测试每个方法之前都会**重置** `learn_web_test` 库；启动项目时会**重置** `learn_web` 库（每次重启数据都恢复成示例数据）。
> 不要在这两个库里放自己的东西。

---

## 推荐的做题方式

1. **先看指南**：每章的指南（`docs/` 下）开头写了要先看的教程 / 视频，然后是知识点速记和题目说明
2. **读练习文件的注释**：每道题的要求都写在方法的注释里，`// TODO` 是你要写代码的地方
3. **跑测试**：红了就看报错信息（测试里写了很多中文提示），改到变绿
4. **自己调一调接口**：启动项目，用浏览器 / Apifox / Postman 调一下你写的接口（Web 和综合项目部分尤其要做）
5. **对答案**：通过后打开 `solutions/` 里的同名文件，看看参考写法

> 卡住超过 20 分钟再看答案。看完后关掉答案，自己重写一遍。

---

## 目录结构

```
springboot-practice/
├── db.properties                ← 改成你的 MySQL 账号密码
├── sql/schema.sql、data.sql      ← 外卖库表结构 + 示例数据（启动和测试时自动执行）
├── src/main/java/com/learn/
│   ├── web/                     ← 主程序（WebPracticeApplication 扫描这个包）
│   │   ├── common/                R、ServiceException、PageQuery、TableDataInfo ...（已写好）
│   │   ├── w01_hello/ w02_rest/ w04_exception/        Web 练习
│   │   ├── b01_mybatis/ ... b04_tx/                   MyBatis 练习
│   │   ├── p01_mp/ p02_wrapper/ p03_page/             MyBatis-Plus 练习
│   │   └── x01_takeout/                               综合项目（RuoYi-Vue-Plus 风格的分层结构）
│   └── w03_ioc/                 ← w03 故意放在 web 包外面，有自己的启动类（原因见 docs/w03）
├── src/main/resources/
│   ├── application.yml          ← Spring Boot 配置（w01、w03 要往里加配置）
│   └── mapper/                  ← MyBatis XML 映射文件（b02、b03、x01 练习）
├── src/test/                    ← 判题测试（不用改）
├── solutions/java、resources     ← 参考答案（同包同类名），答案部分用 // @answer ... // @end 包起来
└── docs/                        ← 学习计划、环境准备、每章指南、常见报错
```

想把某道题恢复成初始状态：`git checkout -- src/main/java/com/learn/web/w02_rest/DishController.java`

---

## 学习进度（做完一项把 ☐ 改成 ✅）

| 章节 | 指南 | 内容 | 判题 |
|---|---|---|---|
| ☐ w01 Spring Boot 入门 | [docs/w01](docs/w01-SpringBoot入门.md) | @RestController、@GetMapping、@RequestParam、@PathVariable、yml 配置、@ConfigurationProperties、@Value | `W01HelloTest` |
| ☐ w02 请求与响应 | [docs/w02](docs/w02-请求与响应.md) | RESTful CRUD、@RequestBody、对象接收查询参数、分页、批量删除、日期参数、@JsonFormat、请求头 | `W02RestTest` |
| ☐ w03 IoC 与依赖注入 | [docs/w03](docs/w03-IoC与依赖注入.md) | @Component、@Primary、@Qualifier、注入 Map、@Configuration + @Bean、@PostConstruct、作用域 | `W03IocTest` |
| ☐ w04 异常处理与参数校验 | [docs/w04](docs/w04-异常处理与参数校验.md) | @RestControllerAdvice、@ExceptionHandler、@Validated、分组校验、自定义校验注解 | `W04ExceptionTest` |
| ☐ b01 MyBatis 注解 SQL | [docs/b01](docs/b01-MyBatis入门.md) | @Mapper、@Select/@Insert/@Update/@Delete、#{}、@Param、回填自增 id | `B01MyBatisTest` |
| ☐ b02 XML 与动态 SQL | [docs/b02](docs/b02-XML与动态SQL.md) | XML 映射文件、&lt;where&gt; &lt;if&gt; &lt;set&gt; &lt;foreach&gt; &lt;choose&gt; &lt;sql&gt;、${} 与 SQL 注入 | `B02DynamicSqlTest` |
| ☐ b03 结果映射与多表查询 | [docs/b03](docs/b03-结果映射与多表查询.md) | JOIN + 别名、resultMap、一对多 / 多对多 &lt;collection&gt;、统计 SQL + Java 补零 | `B03ResultMapTest` |
| ☐ b04 事务 | [docs/b04](docs/b04-事务.md) | @Transactional、回滚、下单事务、事务失效的三个坑 | `B04TransactionTest` |
| ☐ p01 MyBatis-Plus 入门 | [docs/p01](docs/p01-MyBatis-Plus入门.md) | @TableName、@TableId、@TableField、BaseMapper、IService、避免 N+1 | `P01MybatisPlusTest` |
| ☐ p02 条件构造器 | [docs/p02](docs/p02-条件构造器.md) | LambdaQueryWrapper、动态条件、and/or 括号、select、分组统计、LambdaUpdateWrapper | `P02WrapperTest` |
| ☐ p03 分页、自动填充、逻辑删除 | [docs/p03](docs/p03-分页与自动填充.md) | 分页插件、PageQuery / TableDataInfo、MetaObjectHandler、@TableLogic | `P03PageTest` |
| ☐ x01 综合项目 | [docs/x01](docs/x01-综合项目.md) | 外卖后台 15 个接口：分类、菜品（含口味、联动停售套餐）、营业额统计、销量 Top10 | `X01*` |

其他文档：
- [docs/学习计划.md](docs/学习计划.md)：每天 5~6 小时、1~2 周完成阶段三-A 的安排
- [docs/00-环境准备.md](docs/00-环境准备.md)：打开项目、启动、调接口、看 SQL 日志
- [docs/常见报错.md](docs/常见报错.md)：遇到报错先来这里查

做完之后回到 [docs/后续路线.md](../docs/后续路线.md)，下一步是阶段三-B（登录鉴权、Redis）。
