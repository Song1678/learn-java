# w01 Spring Boot 入门

> 先看：黑马《JavaWeb 开发教程》Spring Boot Web 入门部分（HTTP 协议、第一个 Spring Boot 程序）；
> 廖雪峰 Java 教程 → “Spring Boot 开发” → 第一个 Spring Boot 应用（略读）
> 练习：`src/main/java/com/learn/web/w01_hello/` + `src/main/resources/application.yml`　判题：`mvn test -Dtest=W01HelloTest`

## Spring Boot 帮你做了什么

ch09 的 `TakeoutApp` 里，你手动 new 了所有对象、手动调用 `controller.add(dto)`。到了 Spring Boot：

| ch09（手动） | Spring Boot（自动） |
|---|---|
| `new DishMapper()`、`new DishService(mapper)` ... | 扫描到 `@Component` / `@Service` / `@RestController`，自动创建并注入（就是你写的 MiniContainer） |
| main 方法里调用 `controller.add(dto)` | 内置 Tomcat 监听 8080 端口，收到 HTTP 请求，按 URL 找到方法调用 |
| 方法参数自己传 | 从 URL、请求体里取出参数，**自动转换类型**后传进来 |
| 返回的 `R` 对象打印出来 | 返回值**自动转成 JSON** 写回给浏览器（Jackson） |
| 配置写死在代码里 | 写在 `application.yml` 里，用注解读取 |

## 速记

```java
@RestController                 // = @Controller + @ResponseBody：返回值直接作为响应体（对象转 JSON）
@RequestMapping("/w01")         // 类里所有接口的公共路径前缀
public class HelloController {

    @GetMapping("/greet")       // GET /w01/greet
    public String greet(@RequestParam(defaultValue = "World") String name) {   // ?name=Tom
        return "Hello, " + name + "!";
    }

    @GetMapping("/dishes/{id}")                  // GET /w01/dishes/1
    public DishInfo getDish(@PathVariable Long id) { ... }   // 路径里的 {id}
}
```

| 注解 | 取哪里的值 | 例子 |
|---|---|---|
| `@RequestParam` | URL 问号后面的参数（查询参数） | `/greet?name=Tom` |
| `@PathVariable` | 路径里的 `{xxx}` | `/dishes/1` |
| `@RequestBody` | 请求体里的 JSON（w02） | POST 新增 |
| `@RequestHeader` | 请求头（w02） | `Authorization: xxx` |

`@RequestParam` 常用属性：`required = false`（可以不传，不传时是 null）、`defaultValue = "..."`（不传时的默认值）。

## 配置文件 application.yml

```yaml
takeout:
  shop:
    name: 苍穹外卖
    open-time: "09:00"
```

YAML 用**缩进**（两个空格，不能用 Tab）表示层级，上面等价于 `takeout.shop.name=苍穹外卖`。冒号后面必须有空格。

读取配置的两种方式：

```java
// 1. 一组配置 → 一个对象（推荐，RuoYi-Vue-Plus 里大量使用，比如 XxxProperties）
@Component
@ConfigurationProperties(prefix = "takeout.shop")
public class ShopProperties { private String name; private LocalTime openTime; /* getter/setter */ }

// 2. 单个配置 → 字段
@Value("${takeout.shop.slogan:好吃不贵}")   // 冒号后面是默认值
private String slogan;
```

## 练习

| 题 | 文件 | 考点 |
|---|---|---|
| 1.0 ~ 1.4 | `HelloController` | @RestController、@RequestMapping、@GetMapping、@RequestParam（默认值、类型转换）、@PathVariable、返回对象自动转 JSON |
| 1.5 | `application.yml` 末尾 + `ShopProperties` | 在 yml 里加上 `takeout.shop` 的 4 个配置（值见下面），再用 @ConfigurationProperties 绑定 |
| 1.6 | `ShopProperties.isOpenAt` | LocalTime 比较 |
| 1.7 | `ShopController.status` | @DateTimeFormat 接收时间参数 |
| 1.8 | `ShopController.slogan` 字段 | @Value + 默认值 |

1.5 要加的配置（application.yml 末尾有 TODO 提示位置；w03 还会再加一行 `worker-id`）：

```yaml
takeout:
  shop:
    name: 苍穹外卖
    phone: 400-800-8888
    open-time: "09:00"
    close-time: "22:00"
```

做完后启动项目，浏览器访问：
- http://localhost:8080/w01/hello
- http://localhost:8080/w01/greet?name=张三
- http://localhost:8080/w01/dishes/1
- http://localhost:8080/w01/shop
- http://localhost:8080/w01/shop/status?time=10:30

## 常见坑

1. 类上忘了 `@RestController` → 所有接口 404。
2. 路径写成 `@GetMapping("hello")` 也行，但建议统一以 `/` 开头。
3. `@RequestParam int a` 用基本类型时，不传参数会报 400；可能不传的参数用包装类型 `Integer` + `required = false`。
4. yml 缩进错了、冒号后面没空格，配置读不到（字段是 null），也不会报错 —— 用 `GET /w01/shop` 看看绑定上没有。
5. `"09:00"` 要加引号，否则 YAML 可能把它当成数字（六十进制）解析。
6. 改了代码或配置要**重启**项目。
