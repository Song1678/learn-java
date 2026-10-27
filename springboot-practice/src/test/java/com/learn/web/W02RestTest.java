package com.learn.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.learn.web.support.WebTestBase;
import com.learn.web.w02_rest.DishStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@DisplayName("w02 请求与响应")
class W02RestTest extends WebTestBase {

    @Autowired
    DishStore dishStore;

    @BeforeEach
    void reset() {
        dishStore.reset();
    }

    private static List<String> names(JsonNode array) {
        List<String> names = new ArrayList<>();
        array.forEach(n -> names.add(n.get("name").asText()));
        return names;
    }

    @Test
    @DisplayName("2.1 GET /w02/dishes/{id}")
    void getById() {
        JsonNode data = assertOk(getJson("/w02/dishes/1"));
        assertEquals("宫保鸡丁", data.get("name").asText());
        assertEquals(1, data.get("categoryId").asInt());
        assertDecimal("28.00", data.get("price"));
        assertEquals("经典川菜", data.get("description").asText());
        assertEquals("口水鸡", assertOk(getJson("/w02/dishes/5")).get("name").asText());

        assertFail(500, "菜品不存在", getJson("/w02/dishes/99"));
    }

    @Test
    @DisplayName("2.2 GET /w02/dishes 条件查询（对象接收参数）")
    void list() {
        assertEquals(List.of("宫保鸡丁", "鱼香肉丝", "回锅肉", "拍黄瓜", "口水鸡", "米饭"),
                names(assertOk(getJson("/w02/dishes"))));
        assertEquals(List.of("宫保鸡丁", "口水鸡"), names(assertOk(getJson("/w02/dishes?name=鸡"))));
        assertEquals(List.of("拍黄瓜", "口水鸡"), names(assertOk(getJson("/w02/dishes?categoryId=2"))));
        assertEquals(List.of("宫保鸡丁", "鱼香肉丝"), names(assertOk(getJson("/w02/dishes?categoryId=1&status=1"))));
        assertEquals(List.of(), names(assertOk(getJson("/w02/dishes?name=鸡&categoryId=3"))));
    }

    @Test
    @DisplayName("2.3 GET /w02/dishes/page 分页")
    void page() {
        JsonNode p = getJson("/w02/dishes/page?pageNum=2&pageSize=4");
        assertEquals(200, p.get("code").asInt());
        assertEquals(6, p.get("total").asInt(), "total 是总条数");
        assertEquals(List.of("口水鸡", "米饭"), names(p.get("rows")));

        JsonNode first = getJson("/w02/dishes/page?pageSize=2");
        assertEquals(List.of("宫保鸡丁", "鱼香肉丝"), names(first.get("rows")), "pageNum 默认是 1");

        JsonNode all = getJson("/w02/dishes/page");
        assertEquals(6, all.get("rows").size(), "pageSize 默认是 10");

        JsonNode out = getJson("/w02/dishes/page?pageNum=3&pageSize=5");
        assertEquals(0, out.get("rows").size(), "超出范围返回空列表");
        assertEquals(6, out.get("total").asInt());
    }

    @Test
    @DisplayName("2.4 POST /w02/dishes（@RequestBody）")
    void add() {
        JsonNode r = postJson("/w02/dishes", """
                {"name": "小笼包", "categoryId": 3, "price": 18.5, "description": "皮薄馅大"}
                """);
        assertEquals(7, assertOk(r).asInt(), "返回新菜品的 id");
        JsonNode dish = assertOk(getJson("/w02/dishes/7"));
        assertEquals("小笼包", dish.get("name").asText());
        assertEquals(3, dish.get("categoryId").asInt());
        assertDecimal("18.5", dish.get("price"));
        assertEquals("皮薄馅大", dish.get("description").asText());
        assertEquals(0, dish.get("status").asInt(), "新菜品默认停售");
    }

    @Test
    @DisplayName("2.5 PUT /w02/dishes")
    void edit() {
        assertOk(putJson("/w02/dishes", """
                {"id": 1, "price": 30.00}
                """));
        JsonNode dish = assertOk(getJson("/w02/dishes/1"));
        assertDecimal("30.00", dish.get("price"));
        assertEquals("宫保鸡丁", dish.get("name").asText(), "没传的字段不修改");

        assertFail(500, "菜品不存在", putJson("/w02/dishes", """
                {"id": 99, "price": 1}
                """));
    }

    @Test
    @DisplayName("2.6 DELETE /w02/dishes/{ids}")
    void remove() {
        assertEquals(2, assertOk(deleteJson("/w02/dishes/1,3")).asInt());
        assertEquals(List.of("鱼香肉丝", "拍黄瓜", "口水鸡", "米饭"), names(assertOk(getJson("/w02/dishes"))));
        assertEquals(1, assertOk(deleteJson("/w02/dishes/2,99")).asInt(), "不存在的 id 不算");
        assertEquals(1, assertOk(deleteJson("/w02/dishes/6")).asInt(), "只删一个也要能用");
    }

    @Test
    @DisplayName("2.7 PUT /w02/dishes/{id}/status/{status}")
    void changeStatus() {
        assertOk(putJson("/w02/dishes/3/status/1"));
        assertEquals(1, assertOk(getJson("/w02/dishes/3")).get("status").asInt());
        assertOk(putJson("/w02/dishes/1/status/0"));
        assertEquals(0, assertOk(getJson("/w02/dishes/1")).get("status").asInt());

        assertFail(500, "状态不合法", putJson("/w02/dishes/1/status/2"));
        assertFail(500, "菜品不存在", putJson("/w02/dishes/99/status/1"));
    }

    @Test
    @DisplayName("2.8 @JsonFormat 日期格式")
    void jsonFormat() {
        JsonNode dish = assertOk(getJson("/w02/dishes/1"));
        assertEquals("2024-04-05 10:10:00", dish.get("createTime").asText());
    }

    @Test
    @DisplayName("2.9 @RequestHeader")
    void header() {
        assertEquals("Bearer abc123",
                assertOk(json(get("/w02/params/token").header("Authorization", "Bearer abc123"))).asText());
        assertFail(401, "未登录", getJson("/w02/params/token"));
    }

    @Test
    @DisplayName("2.10 日期参数")
    void dates() {
        JsonNode days = assertOk(getJson("/w02/params/dates?begin=2024-04-29&end=2024-05-02"));
        List<String> list = new ArrayList<>();
        days.forEach(d -> list.add(d.asText()));
        assertEquals(List.of("2024-04-29", "2024-04-30", "2024-05-01", "2024-05-02"), list);
        assertEquals(1, assertOk(getJson("/w02/params/dates?begin=2024-05-01&end=2024-05-01")).size());
        assertFail(500, "开始日期不能晚于结束日期", getJson("/w02/params/dates?begin=2024-05-02&end=2024-05-01"));
    }

    @Test
    @DisplayName("2.11 Map 接收 JSON")
    void keys() {
        JsonNode keys = assertOk(postJson("/w02/params/keys", """
                {"name": "宫保鸡丁", "flavors": [{"name": "辣度"}], "categoryId": 1, "price": 28}
                """));
        assertEquals("[\"categoryId\",\"flavors\",\"name\",\"price\"]", keys.toString());
    }
}
