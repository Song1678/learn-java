package com.learn.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("x01 综合项目：分类管理接口")
class X01CategoryApiTest extends DbTestBase {

    private static List<String> names(JsonNode array) {
        List<String> names = new ArrayList<>();
        array.forEach(n -> names.add(n.get("name").asText()));
        return names;
    }

    @Test
    @DisplayName("x.1 GET /admin/category/list 分页")
    void list() {
        JsonNode p = getJson("/admin/category/list?pageNum=1&pageSize=3");
        assertEquals(200, p.get("code").asInt());
        assertEquals(7, p.get("total").asInt(), "total 不对，检查分页插件（p03 练习 3.1）");
        assertEquals(List.of("热菜", "凉菜", "主食"), names(p.get("rows")));
        assertEquals("2024-04-01 09:00:00", p.get("rows").get(0).get("createTime").asText());

        assertEquals(List.of("超值套餐"), names(getJson("/admin/category/list?type=2").get("rows")));
        assertEquals(List.of("热菜", "凉菜"), names(getJson("/admin/category/list?name=菜&type=1").get("rows")));
        assertEquals(List.of("甜品"), names(getJson("/admin/category/list?pageNum=3&pageSize=3").get("rows")));
    }

    @Test
    @DisplayName("x.1 listByType、详情")
    void listByTypeAndGet() {
        assertEquals(List.of("热菜", "凉菜", "主食", "饮品", "甜品"),
                names(assertOk(getJson("/admin/category/listByType?type=1"))), "汤羹是禁用的，不要");
        assertEquals("饮品", assertOk(getJson("/admin/category/4")).get("name").asText());
        assertFail(500, "分类不存在", getJson("/admin/category/99"));
    }

    @Test
    @DisplayName("x.2 POST /admin/category 新增")
    void add() {
        assertEquals(8, assertOk(postJson("/admin/category", """
                {"type": 1, "name": "烧烤", "sort": 8}
                """)).asInt());
        JsonNode c = assertOk(getJson("/admin/category/8"));
        assertEquals("烧烤", c.get("name").asText());
        assertEquals(1, c.get("status").asInt(), "新增的分类默认启用");
        assertFalse(c.get("createTime").isNull(), "createTime 要自动填充（p03 练习 3.2）");

        assertFail(500, "分类名称已存在", postJson("/admin/category", """
                {"type": 1, "name": "热菜", "sort": 1}
                """));
        assertFail(400, "分类名称不能为空", postJson("/admin/category", """
                {"type": 1, "sort": 1}
                """));
        assertFail(400, "分类类型只能是1或2", postJson("/admin/category", """
                {"type": 3, "name": "火锅", "sort": 1}
                """));
        assertFail(400, "排序不能为空", postJson("/admin/category", """
                {"type": 1, "name": "火锅"}
                """));
        assertEquals(8, count("SELECT COUNT(*) FROM category"));
    }

    @Test
    @DisplayName("x.2 PUT /admin/category 修改")
    void edit() {
        assertOk(putJson("/admin/category", """
                {"id": 2, "type": 1, "name": "冷盘", "sort": 9}
                """));
        assertEquals("冷盘", row("SELECT name FROM category WHERE id = 2").get("name"));
        assertOk(putJson("/admin/category", """
                {"id": 2, "type": 1, "name": "冷盘", "sort": 10}
                """), "名字没改，不算重复");

        assertFail(500, "分类名称已存在", putJson("/admin/category", """
                {"id": 2, "type": 1, "name": "热菜", "sort": 1}
                """));
        assertFail(500, "分类不存在", putJson("/admin/category", """
                {"id": 99, "type": 1, "name": "火锅", "sort": 1}
                """));
        assertFail(400, "id不能为空", putJson("/admin/category", """
                {"type": 1, "name": "火锅", "sort": 1}
                """));
    }

    @Test
    @DisplayName("x.3 DELETE /admin/category/{id}")
    void remove() {
        assertFail(500, "当前分类关联了菜品，不能删除", deleteJson("/admin/category/1"));
        assertFail(500, "当前分类关联了套餐，不能删除", deleteJson("/admin/category/6"));
        assertFail(500, "分类不存在", deleteJson("/admin/category/99"));
        assertOk(deleteJson("/admin/category/7"));
        assertEquals(0, count("SELECT COUNT(*) FROM category WHERE id = 7"));
    }

    @Test
    @DisplayName("x.3 PUT /admin/category/{id}/status/{status}")
    void changeStatus() {
        assertOk(putJson("/admin/category/5/status/1"));
        assertEquals(1, ((Number) row("SELECT status FROM category WHERE id = 5").get("status")).intValue());
        assertOk(putJson("/admin/category/1/status/0"));
        assertEquals(0, ((Number) row("SELECT status FROM category WHERE id = 1").get("status")).intValue());
        assertFail(500, "状态不合法", putJson("/admin/category/1/status/3"));
        assertFail(500, "分类不存在", putJson("/admin/category/99/status/1"));
    }
}
