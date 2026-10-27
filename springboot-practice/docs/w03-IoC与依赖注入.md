# w03 IoC 与依赖注入

> 先看：回顾 learn-java ch08 的 `MiniContainer`；黑马《JavaWeb 开发教程》“分层解耦”（IoC、DI）；
> 廖雪峰 Java 教程 → “Spring 开发” → IoC 容器（IoC 原理、装配 Bean、使用注解配置、定制 Bean、使用 Resource、注入配置）
> 练习：`src/main/java/com/learn/w03_ioc/`（注意不在 web 包里）　判题：`mvn test -Dtest=W03IocTest`

## 和 MiniContainer 对照

| 你在 ch08 写的 | Spring |
|---|---|
| `@Component` 标记要管理的类 | `@Component`，以及它的“别名” `@Service`、`@Controller`、`@RestController`、`@Repository`、`@Configuration` |
| `register(Class)` 手动注册 | 组件扫描：`@SpringBootApplication` 自动扫描启动类所在的包及子包 |
| `@Autowired` 字段注入 | `@Autowired` 字段注入 / **构造器注入（推荐）** / setter 注入 |
| 单例缓存 `Map<Class, Object>` | 默认单例（singleton），可以改成 prototype |
| `getBean(Class)` | `context.getBean(Class)`，但正常写代码时几乎不用，都是注入 |

构造器注入：类只有一个构造方法时，Spring 自动用它创建对象并传入参数，不用写 `@Autowired`。
RuoYi-Vue-Plus 用 Lombok 的 `@RequiredArgsConstructor` 自动生成这个构造方法（x01 会用到）：

```java
@Service
@RequiredArgsConstructor
public class DishServiceImpl implements IDishService {
    private final DishMapper baseMapper;          // final 字段 → 构造方法参数 → Spring 注入
    private final CategoryMapper categoryMapper;
}
```

## 为什么本章在 com.learn.web 外面

主程序 `WebPracticeApplication` 只扫描 `com.learn.web` 包。本章练的就是容器本身，**练习没做完时容器是启动不了的**（3.1 会遇到），
如果放在 web 包里，会导致所有章节的测试都启动失败。所以本章有自己的启动类 `IocApplication`，测试也用它。
你可以直接运行 `IocApplication` 的 main 方法，看看容器里的 bean。

## 速记

```java
@Component("discount")            // 指定 bean 名字；不写名字默认是类名首字母小写：discountPay
public class DiscountPay implements PayStrategy { }

@Component @Primary               // 同类型有多个 bean 时，默认注入这个
public class AliyunSmsSender implements SmsSender { }

public NotifyService(SmsSender primary,                                      // 注入 @Primary 的
                     @Qualifier("tencentSmsSender") SmsSender backup) { }    // 按名字指定

public PayService(Map<String, PayStrategy> strategies) { }    // 注入所有 PayStrategy，key 是 bean 名字
public PayService(List<PayStrategy> strategies) { }           // 也可以注入 List

@Configuration
public class AppConfig {
    @Bean                                                      // 方法返回值放进容器，bean 名字是方法名
    public IdGenerator idGenerator(@Value("${takeout.worker-id:1}") long workerId) {
        return new IdGenerator(workerId);
    }
}

@PostConstruct public void load() { }    // 依赖注入完成后自动调用一次
@Scope("prototype")                     // 每次 getBean 都创建新对象
```

**`@Component` 和 `@Bean` 的区别**：`@Component` 写在**自己的类**上；第三方 jar 包里的类你改不了源码，就在配置类里写 `@Bean` 方法自己 new。

## 练习（按顺序做）

| 题 | 文件 | 考点 |
|---|---|---|
| 3.1 | `sms/AliyunSmsSender`、`sms/NotifyService` | **先做这题**：现在容器启动不了，报 `expected single matching bean but found 2`。用 @Primary + @Qualifier 解决；再实现 send 的“失败切换” |
| 3.2 | `pay/DiscountPay`、`pay/FullReductionPay` | @Component 指定 bean 名字 |
| 3.3 | `pay/PayService` | 注入 Map；策略模式（对照 ch02 的 PayStrategy） |
| 3.4 | `config/AppConfig` + application.yml | @Configuration + @Bean；在 yml 的 `takeout:` 下加 `worker-id: 7` |
| 3.5 | `config/DictCache` | @PostConstruct |
| 3.6 | `config/CartSession` | @Scope("prototype") |

## 常见报错

| 报错 | 原因 |
|---|---|
| `Failed to load ApplicationContext` | 容器启动失败，往下翻找 `Caused by` |
| `expected single matching bean but found 2` | 同类型有多个 bean，不知道注入哪个 → @Primary / @Qualifier |
| `No qualifying bean of type 'xxx' available` | 容器里没有这个类型的 bean：忘了加 @Component？类不在扫描的包里？ |
| `The dependencies of some of the beans form a cycle` | 循环依赖：A 注入 B，B 又注入 A → 重新设计，把公共部分抽出来 |

## 常见坑

1. 自己 `new DishService()` 出来的对象不归 Spring 管，里面的 `@Autowired` 字段都是 null。要用就注入，不要 new。
2. 字段注入（`@Autowired private Xxx xxx;`）能用，但不推荐：不能声明 final、写单元测试时不好传依赖。
3. `@Qualifier` 里写的是 **bean 名字**，不是类名；默认名字是类名首字母小写。
4. 单例 bean 被所有请求共用，不要在 Service 里用普通字段保存“某个用户的数据”（线程安全问题）。
