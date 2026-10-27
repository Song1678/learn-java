package com.learn.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.learn.web.support.WebTestBase;
import com.learn.web.w01_hello.ShopProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@DisplayName("w01 Spring Boot 入门")
class W01HelloTest extends WebTestBase {

    @Autowired
    ShopProperties shopProperties;

    @Test
    @DisplayName("1.1 GET /w01/hello")
    void hello() {
        assertEquals("Hello Spring Boot", text(get("/w01/hello")));
    }

    @Test
    @DisplayName("1.2 @RequestParam 默认值")
    void greet() {
        assertEquals("Hello, Tom!", text(get("/w01/greet").param("name", "Tom")));
        assertEquals("Hello, 张三!", text(get("/w01/greet").param("name", "张三")));
        assertEquals("Hello, World!", text(get("/w01/greet")), "不传 name 时用默认值 World");
    }

    @Test
    @DisplayName("1.3 参数类型转换")
    void add() {
        assertEquals("3", text(get("/w01/add?a=1&b=2")));
        assertEquals("-5", text(get("/w01/add?a=5&b=-10")));
    }

    @Test
    @DisplayName("1.4 @PathVariable + 自动转 JSON")
    void pathVariable() {
        JsonNode dish = getJson("/w01/dishes/1");
        assertEquals(1, dish.get("id").asInt());
        assertEquals("宫保鸡丁", dish.get("name").asText());
        assertDecimal("28.00", dish.get("price"));
        assertEquals("鱼香肉丝", getJson("/w01/dishes/2").get("name").asText());
        assertEquals("", text(get("/w01/dishes/99")), "不存在时返回 null，响应体为空");
    }

    @Test
    @DisplayName("1.5 @ConfigurationProperties 绑定配置")
    void configurationProperties() {
        assertEquals("苍穹外卖", shopProperties.getName(), "检查 application.yml 里的 takeout.shop.name 和类上的注解");
        assertEquals("400-800-8888", shopProperties.getPhone());
        assertEquals(LocalTime.of(9, 0), shopProperties.getOpenTime());
        assertEquals(LocalTime.of(22, 0), shopProperties.getCloseTime());

        JsonNode info = getJson("/w01/shop");
        assertEquals("苍穹外卖", info.get("name").asText());
    }

    @Test
    @DisplayName("1.6 isOpenAt")
    void isOpenAt() {
        assertFalse(shopProperties.isOpenAt(LocalTime.of(8, 59)));
        assertTrue(shopProperties.isOpenAt(LocalTime.of(9, 0)), "开门那一刻算营业");
        assertTrue(shopProperties.isOpenAt(LocalTime.of(12, 30)));
        assertTrue(shopProperties.isOpenAt(LocalTime.of(21, 59)));
        assertFalse(shopProperties.isOpenAt(LocalTime.of(22, 0)), "打烊那一刻不算营业");
    }

    @Test
    @DisplayName("1.7 @DateTimeFormat 接收时间参数")
    void status() {
        assertEquals("营业中", text(get("/w01/shop/status?time=10:30")));
        assertEquals("已打烊", text(get("/w01/shop/status?time=23:15")));
        assertEquals("已打烊", text(get("/w01/shop/status?time=07:00")));
    }

    @Test
    @DisplayName("1.8 @Value 默认值")
    void value() {
        assertEquals("好吃不贵", text(get("/w01/shop/slogan")), "配置文件里没有 takeout.shop.slogan，应该用默认值");
    }
}
