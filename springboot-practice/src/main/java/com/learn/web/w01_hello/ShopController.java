package com.learn.web.w01_hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

/**
 * w01 练习 1.6 ~ 1.8：读取配置
 *
 * <p>这个类已经加好了 @RestController，Spring 创建它的时候会通过构造方法把 ShopProperties 传进来（构造器注入）。
 */
@RestController
@RequestMapping("/w01/shop")
public class ShopController {

    private final ShopProperties shopProperties;

    /**
     * 1.8 用 @Value 读取单个配置 takeout.shop.slogan，配置文件里没有时默认为 "好吃不贵"。
     * 提示：@Value("${配置名:默认值}")
     */
    // TODO 1.8 给 slogan 字段加上 @Value 注解
    private String slogan;

    public ShopController(ShopProperties shopProperties) {
        this.shopProperties = shopProperties;
    }

    /**
     * GET /w01/shop → 直接返回 ShopProperties 对象（已写好，做完 1.5 就能看到配置的值）
     */
    @GetMapping
    public ShopProperties info() {
        return shopProperties;
    }

    /**
     * 1.7 GET /w01/shop/status?time=10:30 → 营业中返回 "营业中"，否则返回 "已打烊"
     * <p>提示：URL 参数 "10:30" 要转成 LocalTime，需要告诉 Spring 格式：@DateTimeFormat(pattern = "HH:mm")
     */
    // TODO 1.7 加上映射注解和参数注解
    public String status(LocalTime time) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * GET /w01/shop/slogan → 返回 slogan 字段（已写好）
     */
    @GetMapping("/slogan")
    public String slogan() {
        return slogan;
    }
}
