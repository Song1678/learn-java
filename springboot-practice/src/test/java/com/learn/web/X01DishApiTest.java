package com.learn.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("x01 综合项目：菜品管理接口")
class X01DishApiTest extends DbTestBase {

    private static List<String> values(JsonNode array, String field) {
        List<String> list = new ArrayList<>();
        array.forEach(n -> list.add(n.get(field).asText()));
        return list;
    }

    private static final String NEW_DISH = """
            {"name": "小笼包", "categoryId": 3, "price": 18.00, "description": "皮薄馅大",
             "flavors": [{"name": "蘸料", "value": "醋,辣椒油"}, {"name": "忌口", "value": "不要葱"}]}
            """;

    @Test
    @DisplayName("x.4 GET /admin/dish/list 分页 + 分类名称")
    void list() {
        JsonNode p = getJson("/admin/dish/list?pageNum=2&pageSize=5");
        assertEquals(20, p.get("total").asInt());
        assertEquals(List.of("6", "7", "8", "9", "10"), values(p.get("rows"), "id"));
        assertEquals(List.of("热菜", "凉菜", "凉菜", "凉菜", "凉菜"), values(p.get("rows"), "categoryName"));
        assertDecimal("42.00", p.get("rows").get(0).get("price"));
        assertEquals("2024-04-06 11:00:00", p.get("rows").get(0).get("updateTime").asText());

        JsonNode drinks = getJson("/admin/dish/list?categoryId=4&status=1");
        assertEquals(List.of("可乐", "酸梅汤", "鲜榨橙汁"), values(drinks.get("rows"), "name"));
        assertEquals(List.of("饮品", "饮品", "饮品"), values(drinks.get("rows"), "categoryName"));

        assertEquals(List.of("宫保鸡丁", "辣子鸡", "口水鸡", "椒麻鸡"), values(getJson("/admin/dish/list?name=鸡").get("rows"), "name"));
    }

    @Test
    @DisplayName("x.5 GET /admin/dish/{id} 详情 + 口味")
    void getInfo() {
        JsonNode fish = assertOk(getJson("/admin/dish/3"));
        assertEquals("水煮鱼", fish.get("name").asText());
        assertEquals("热菜", fish.get("categoryName").asText());
        assertEquals(List.of("辣度", "忌口"), values(fish.get("flavors"), "name"));
        assertEquals(0, assertOk(getJson("/admin/dish/11")).get("flavors").size());
        assertFail(500, "菜品不存在", getJson("/admin/dish/99"));
    }

    @Test
    @DisplayName("x.6 POST /admin/dish 新增")
    void add() {
        assertEquals(21, assertOk(postJson("/admin/dish", NEW_DISH)).asInt());
        JsonNode d = assertOk(getJson("/admin/dish/21"));
        assertEquals("小笼包", d.get("name").asText());
        assertEquals("主食", d.get("categoryName").asText());
        assertEquals(0, d.get("status").asInt(), "新菜品默认停售");
        assertEquals("皮薄馅大", d.get("description").asText());
        assertEquals(List.of("醋,辣椒油", "不要葱"), values(d.get("flavors"), "value"));
        assertNotNull(row("SELECT create_time FROM dish WHERE id = 21").get("create_time"), "create_time 要自动填充");

        assertFail(500, "菜品名称已存在", postJson("/admin/dish", NEW_DISH));
        assertFail(500, "分类不存在", postJson("/admin/dish", NEW_DISH.replace("小笼包", "生煎").replace("\"categoryId\": 3", "\"categoryId\": 6")),
                "6 号是套餐分类，不能放菜品");
        assertFail(500, "分类不存在", postJson("/admin/dish", NEW_DISH.replace("小笼包", "生煎").replace("\"categoryId\": 3", "\"categoryId\": 99")));
        assertEquals(21, count("SELECT COUNT(*) FROM dish"));
    }

    @Test
    @DisplayName("x.6 新增时的参数校验")
    void addValidation() {
        assertFail(400, "菜品名称不能为空", postJson("/admin/dish", """
                {"categoryId": 3, "price": 18}
                """));
        assertFail(400, "价格必须大于0", postJson("/admin/dish", """
                {"name": "生煎", "categoryId": 3, "price": 0}
                """));
        assertFail(400, "分类不能为空", postJson("/admin/dish", """
                {"name": "生煎", "price": 18}
                """));
        assertFail(400, "口味名称不能为空", postJson("/admin/dish", """
                {"name": "生煎", "categoryId": 3, "price": 18, "flavors": [{"value": "微辣"}]}
                """));
    }

    @Test
    @DisplayName("x.7 PUT /admin/dish 修改 + 替换口味")
    void edit() {
        assertOk(putJson("/admin/dish", """
                {"id": 3, "name": "酸菜鱼", "categoryId": 1, "price": 52.00,
                 "flavors": [{"name": "辣度", "value": "不辣,微辣"}]}
                """));
        JsonNode d = assertOk(getJson("/admin/dish/3"));
        assertEquals("酸菜鱼", d.get("name").asText());
        assertDecimal("52.00", d.get("price"));
        assertEquals(List.of("不辣,微辣"), values(d.get("flavors"), "value"), "旧口味要删掉，换成新的");
        assertEquals(1, count("SELECT COUNT(*) FROM dish_flavor WHERE dish_id = 3"));

        assertOk(putJson("/admin/dish", """
                {"id": 3, "name": "酸菜鱼", "categoryId": 1, "price": 52.00}
                """), "名字没改不算重复；不传 flavors 表示没有口味");
        assertEquals(0, count("SELECT COUNT(*) FROM dish_flavor WHERE dish_id = 3"));

        assertFail(500, "菜品名称已存在", putJson("/admin/dish", """
                {"id": 3, "name": "米饭", "categoryId": 1, "price": 52.00}
                """));
        assertFail(500, "菜品不存在", putJson("/admin/dish", """
                {"id": 99, "name": "佛跳墙", "categoryId": 1, "price": 52.00}
                """));
    }

    @Test
    @DisplayName("x.8 DELETE /admin/dish/{ids}")
    void remove() {
        assertFail(500, "起售中的菜品不能删除", deleteJson("/admin/dish/10,1"));
        assertFail(500, "当前菜品关联了套餐，不能删除", deleteJson("/admin/dish/18,5"));
        assertFail(500, "菜品不存在", deleteJson("/admin/dish/18,99"));
        assertEquals(20, count("SELECT COUNT(*) FROM dish"), "失败时一个都不能删");

        assertOk(deleteJson("/admin/dish/10,18"));
        assertEquals(18, count("SELECT COUNT(*) FROM dish"));
        assertEquals(0, count("SELECT COUNT(*) FROM dish_flavor WHERE dish_id = 18"), "口味也要删掉");
    }

    @Test
    @DisplayName("x.8 PUT /admin/dish/{id}/status/{status} 停售时联动套餐")
    void changeStatus() {
        assertOk(putJson("/admin/dish/1/status/0"));
        assertEquals(0, ((Number) row("SELECT status FROM dish WHERE id = 1").get("status")).intValue());
        assertEquals(0, ((Number) row("SELECT status FROM setmeal WHERE id = 1").get("status")).intValue(),
                "宫保鸡丁停售了，包含它的“单人工作餐”也要停售");
        assertEquals(1, ((Number) row("SELECT status FROM setmeal WHERE id = 2").get("status")).intValue(), "别的套餐不受影响");

        assertOk(putJson("/admin/dish/1/status/1"));
        assertEquals(1, ((Number) row("SELECT status FROM dish WHERE id = 1").get("status")).intValue());
        assertEquals(0, ((Number) row("SELECT status FROM setmeal WHERE id = 1").get("status")).intValue(),
                "起售菜品不会自动起售套餐");

        assertFail(500, "状态不合法", putJson("/admin/dish/1/status/5"));
        assertFail(500, "菜品不存在", putJson("/admin/dish/99/status/0"));
    }
}
