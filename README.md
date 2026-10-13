# learn-java：从 Java 基础到 RuoYi-Vue-Plus 的练手教程

给有 JS 基础、刚看完廖雪峰 Java 教程 1~7 章的同学准备的**练习项目**。
做法很像刷 LeetCode：每道题已经写好方法签名和说明，你来填代码，然后跑单元测试，**全部变绿就算通过**。

```
阶段一：巩固基础（ch01~ch07）   → 对应廖雪峰 1~7 章，100 多个待填写的方法
阶段二：衔接框架（ch08~ch09）   → Lambda/Stream/时间 API、手写迷你 IoC 容器、内存版“迷你外卖”
阶段三：框架与项目（docs/后续路线.md）→ Spring Boot → MyBatis-Plus → RuoYi-Vue-Plus → 外卖项目
```

---

## 快速开始

1. 安装 JDK 17、Maven、IntelliJ IDEA（详见 [docs/00-环境准备.md](docs/00-环境准备.md)）
2. 用 IDEA 打开本项目根目录（识别成 Maven 项目）
3. 打开 `src/main/java/com/learn/ch01_basics/Ex01_Types.java`，找到 `// TODO`，开始写代码
4. 打开对应测试 `src/test/java/com/learn/ch01_basics/Ex01_TypesTest.java`，点类名旁边的绿色 ▶ 运行
5. 红了就看报错信息改代码，直到全绿 ✅

命令行也可以：

```bash
# 只跑某一章（推荐）
mvn test -Dtest='com.learn.ch01_basics.**'

# 只跑某一个测试类
mvn test -Dtest=Ex01_TypesTest

# 跑全部练习（刚开始会一片红，正常）
mvn test

# 用参考答案跑全部测试（应该全绿，用来确认题目本身没问题）
mvn test -Psolutions
```

> 运行 main 方法：`mvn -q compile exec:java -Dexec.mainClass=com.learn.ch09_takeout.TakeoutApp`
> （加 `-Psolutions` 就是运行参考答案版）

---

## 目录结构

```
learn-java/
├── src/main/java/com/learn/        ← 你的练习（写代码的地方）
│   ├── ch01_basics/                  变量、类型、字符串、数组、流程控制、井字棋
│   ├── ch02_oop/                     继承、多态、抽象类、接口、static、枚举、record、BigDecimal
│   ├── ch03_exception/               自定义异常、受检/非受检、异常转换、try-with-resources
│   ├── ch04_reflection/              读写字段、调用方法、手写 BeanCopier
│   ├── ch05_annotation/              自定义注解、手写参数校验器、注解驱动导出 CSV
│   ├── ch06_generics/                R<T>、Pair、分页、通配符、泛型仓库
│   ├── ch07_collections/             List/Set/Map/Deque、equals/hashCode、购物车、LRU、优先队列
│   ├── ch08_bridge/                  Lambda、Stream、java.time、迷你 IoC 容器
│   └── ch09_takeout/                 综合项目：内存版迷你外卖（分层架构）
├── src/test/java/com/learn/        ← 单元测试 = 判题器（不用改）
├── solutions/src/main/java/        ← 参考答案（同包同类名，含讲解注释）
└── docs/                           ← 每章的知识点和讲解、环境准备、后续学习路线
```

### 关于参考答案

- `solutions/` 里每个文件和练习一一对应，答案部分用 `// @answer ... // @end` 包起来，方便你找。
- **先自己写，卡住超过 20 分钟再看答案**。看完后关掉答案，自己重写一遍。
- 你的代码和答案不一样没关系，测试通过就行。之后可以对比一下写法。
- 想把某个练习恢复成初始状态：`git checkout -- src/main/java/com/learn/ch01_basics/Ex01_Types.java`

---

## 学习进度（做完一项把 ☐ 改成 ✅）

### 阶段一：巩固基础

| 章节 | 指南 | 练习 | 对应廖雪峰 |
|---|---|---|---|
| ch01 基础语法 | [docs/ch01](docs/ch01-基础语法.md) | ☐ Ex01_Types<br>☐ Ex02_Strings<br>☐ Ex03_Arrays<br>☐ Ex04_TicTacToe | Java 快速入门、程序基础、流程控制、数组 |
| ch02 面向对象 | [docs/ch02](docs/ch02-面向对象.md) | ☐ shape 包（多态）<br>☐ Employee / Manager<br>☐ pay 包（接口 + BigDecimal）<br>☐ OrderStatus（枚举）<br>☐ CartItem / Receipt（record） | 面向对象基础、核心类 |
| ch03 异常 | [docs/ch03](docs/ch03-异常处理.md) | ☐ ServiceException<br>☐ AgeParser<br>☐ Account / TransferService<br>☐ ResourceDemo<br>☐ UserRegisterService | 异常处理 |
| ch04 反射 | [docs/ch04](docs/ch04-反射.md) | ☐ ReflectionUtils<br>☐ BeanCopier | 反射 |
| ch05 注解 | [docs/ch05](docs/ch05-注解.md) | ☐ Length（找 bug）<br>☐ Validator<br>☐ CsvExporter | 注解 |
| ch06 泛型 | [docs/ch06](docs/ch06-泛型.md) | ☐ R<br>☐ Pair<br>☐ PageResult<br>☐ GenericUtils<br>☐ InMemoryRepository | 泛型 |
| ch07 集合 | [docs/ch07](docs/ch07-集合.md) | ☐ CollectionBasics<br>☐ WordCounter<br>☐ Dish（equals/hashCode）<br>☐ ShoppingCart<br>☐ LRUCache<br>☐ OrderQueue | 集合 |

### 阶段二：衔接框架

| 章节 | 指南 | 练习 | 预习廖雪峰 |
|---|---|---|---|
| ch08 衔接篇 | [docs/ch08](docs/ch08-衔接篇.md) | ☐ LambdaExercises<br>☐ StreamExercises<br>☐ TimeExercises<br>☐ MiniContainer（迷你 IoC） | 函数式编程、日期与时间 |
| ch09 迷你外卖 | [docs/ch09](docs/ch09-迷你外卖.md) | ☐ InMemoryMapper<br>☐ CategoryService<br>☐ DishService<br>☐ ShoppingCartService<br>☐ OrderService<br>☐ ReportService<br>☐ 运行 TakeoutApp | — |

### 阶段三：框架与完整项目

见 [docs/后续路线.md](docs/后续路线.md)。

---

## 其他文档

- [docs/00-环境准备.md](docs/00-环境准备.md)：JDK / Maven / IDEA 安装，怎么跑测试，常见问题
- [docs/js-vs-java对照.md](docs/js-vs-java对照.md)：JS 写法和 Java 写法对照速查
- [docs/后续路线.md](docs/后续路线.md)：学完这个项目之后学什么、按什么顺序学

## 建议节奏

- 每天 2~3 小时的话：阶段一大约 1~1.5 周，阶段二大约 1 周。
- 每做完一章，回头看一眼对应的廖雪峰章节，查漏补缺。
- 每完成一章就 `git commit` 一次，养成提交习惯（公司项目也是这么干的）。
