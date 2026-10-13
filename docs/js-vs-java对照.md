# JS → Java 速查对照

写 Java 时脑子里冒出 JS 写法，就来这里查。

## 变量与类型

| JS | Java | 说明 |
|---|---|---|
| `let n = 1` | `int n = 1;` 或 `var n = 1;` | Java 是静态类型，`var` 只是让编译器推断，类型定下来就不能变 |
| `const PI = 3.14` | `final double PI = 3.14;` | 类级常量写成 `public static final` |
| `number` | `int` / `long` / `double` / `BigDecimal` | 钱一律用 `BigDecimal` |
| `7 / 2 === 3.5` | `7 / 2 == 3`，`7 / 2.0 == 3.5` | ⚠️ 整数除法丢小数 |
| `null` / `undefined` | 只有 `null` | 基本类型（int）不能是 null，包装类型（Integer）可以 |
| `typeof x` | `x instanceof String`、`x.getClass()` | |
| `===` | 基本类型用 `==`；对象用 `.equals()` | ⚠️ 字符串比较必须 `a.equals(b)` |
| `` `Hi ${name}` `` | `"Hi " + name` 或 `"Hi %s".formatted(name)` | |
| `parseInt("12")` | `Integer.parseInt("12")` | 格式错误会抛 `NumberFormatException` |
| `String(12)` | `String.valueOf(12)` | |

## 数组与集合

| JS | Java |
|---|---|
| `[1, 2, 3]`（长度可变） | `List<Integer> list = new ArrayList<>(List.of(1, 2, 3));` |
| `arr.length` | 数组 `arr.length`；List `list.size()` |
| `arr[0]` | 数组 `arr[0]`；List `list.get(0)` |
| `arr.push(x)` | `list.add(x)` |
| `arr.includes(x)` | `list.contains(x)` |
| `arr.indexOf(x)` | `list.indexOf(x)` |
| `arr.slice(1, 3)` | `list.subList(1, 3)` |
| `arr.filter(x => x > 0)` | `list.stream().filter(x -> x > 0).toList()` |
| `arr.map(x => x * 2)` | `list.stream().map(x -> x * 2).toList()` |
| `arr.reduce((a, b) => a + b, 0)` | `list.stream().reduce(0, Integer::sum)` |
| `arr.find(x => x > 0)` | `list.stream().filter(x -> x > 0).findFirst()`（返回 `Optional`） |
| `arr.some(...)` / `arr.every(...)` | `anyMatch(...)` / `allMatch(...)` |
| `arr.sort((a, b) => a - b)` | `list.sort(Comparator.naturalOrder())` 或 `list.sort((a, b) -> a - b)` |
| `arr.join(",")` | `String.join(",", list)` |
| `[...new Set(arr)]` | `new ArrayList<>(new LinkedHashSet<>(list))` |
| `{a: 1}` / `new Map()` | `Map<String, Integer> map = new HashMap<>();` |
| `obj[key]` / `map.get(k)` | `map.get(k)`（不存在返回 null），`map.getOrDefault(k, 0)` |
| `Object.keys(obj)` | `map.keySet()` |
| `Object.entries(obj)` | `map.entrySet()`，每项 `e.getKey()` / `e.getValue()` |
| `for (const x of arr)` | `for (int x : arr)` / `for (String s : list)` |

## 函数与对象

| JS | Java |
|---|---|
| `function add(a, b) { return a + b }` | `static int add(int a, int b) { return a + b; }`（必须在类里） |
| `(a, b) => a + b` | `(a, b) -> a + b`（必须赋给一个函数式接口类型） |
| 回调参数 `fn` | `Function<T, R>`、`Predicate<T>`、`Consumer<T>`、`Supplier<T>` |
| `class A extends B` | `class A extends B`（单继承）+ `implements 接口1, 接口2` |
| `constructor(x) { this.x = x }` | `public A(int x) { this.x = x; }` |
| `#private` | `private` 字段 + public getter / setter |
| `static` | `static` |
| 对象字面量 `{name, age}` | `record Person(String name, int age) {}` 或 JavaBean 类 |
| `JSON.stringify(obj)` | 用 Jackson：`objectMapper.writeValueAsString(obj)`（Spring 自动做） |
| ES Module `import x from './x'` | `import com.learn.xxx.ClassName;`，一个文件一个 public 类，文件名 = 类名 |
| TypeScript `interface R<T> { data: T }` | `class R<T> { private T data; }` |

## 异常与异步

| JS | Java |
|---|---|
| `throw new Error("msg")` | `throw new IllegalArgumentException("msg");` 或自定义 `ServiceException` |
| `try {} catch (e) {} finally {}` | 一样，但 `catch (IOException e)` 要写异常类型，可以有多个 catch |
| `Promise` / `async await` | 后端一般同步写（一个请求一个线程），需要异步时用 `CompletableFuture`、线程池、消息队列 |
| `console.log(x)` | `System.out.println(x)`；项目里用日志 `log.info("x = {}", x)` |

## 前端视角看后端分层

```
前端 axios.get('/admin/dish/list')
        │  HTTP 请求（JSON）
        ▼
Controller   接收参数、返回 R<T>          ≈ 路由处理函数
        ▼
Service      业务逻辑、校验、抛 ServiceException
        ▼
Mapper       数据库操作（MyBatis 写 SQL）  ≈ 调用 API / 操作 store
        ▼
MySQL
```

第 9 章的迷你外卖就是按这个结构写的，只不过数据库换成了内存 Map。
