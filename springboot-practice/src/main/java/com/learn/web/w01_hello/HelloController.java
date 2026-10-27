package com.learn.web.w01_hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * w01 练习 1.1 ~ 1.4：第一个 Controller
 *
 * <p>启动 {@code WebPracticeApplication} 后，浏览器打开 http://localhost:8080/w01/hello 就能看到结果。
 *
 * <p>本类所有接口的路径都以 /w01 开头 —— 在类上写 {@code @RequestMapping("/w01")}，方法上只写剩下的部分。
 */
// TODO 1.0 给类加两个注解：声明这是一个返回 JSON/字符串 的 Controller；所有路径以 /w01 开头
public class HelloController {

    /** 示例数据（已写好） */
    private static final Map<Long, DishInfo> DISHES = Map.of(
            1L, new DishInfo(1L, "宫保鸡丁", new BigDecimal("28.00")),
            2L, new DishInfo(2L, "鱼香肉丝", new BigDecimal("26.00")));

    /**
     * 1.1 GET /w01/hello → 返回字符串 "Hello Spring Boot"
     */
    // TODO 1.1 加上映射注解
    public String hello() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.2 GET /w01/greet?name=Tom → "Hello, Tom!"
     * <p>不传 name 时 → "Hello, World!"（提示：@RequestParam 的 defaultValue）
     */
    // TODO 1.2 加上映射注解，给参数 name 加上 @RequestParam 并设置默认值
    public String greet(String name) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.3 GET /w01/add?a=1&amp;b=2 → 3
     * <p>a、b 都必须传。URL 里的参数都是字符串，Spring 会自动帮你转成 int；
     * 传了 a=abc 这种转不了的，Spring 会直接返回 400 错误，不用你管。
     */
    // TODO 1.3 加上映射注解和参数注解
    public int add(int a, int b) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.4 GET /w01/dishes/1 → 返回 DISHES 里 id=1 的菜品，Spring 会自动转成 JSON：
     * <pre>{"id":1,"name":"宫保鸡丁","price":28.00}</pre>
     * 不存在时返回 null（响应体为空）。
     * <p>提示：路径里的 {id} 用 @PathVariable 接收。
     */
    // TODO 1.4 加上映射注解和参数注解
    public DishInfo getDish(Long id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 菜品信息（已写好）。record 也能被 Jackson 转成 JSON。 */
    public record DishInfo(Long id, String name, BigDecimal price) {
    }
}
