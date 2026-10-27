package com.learn.web;

import com.learn.web.b02_dynamic.Dish;
import com.learn.web.b02_dynamic.DishFlavor;
import com.learn.web.b02_dynamic.DishMapper;
import com.learn.web.b02_dynamic.DishQuery;
import com.learn.web.b02_dynamic.DishSortService;
import com.learn.web.common.ServiceException;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("b02 XML 与动态 SQL")
class B02DynamicSqlTest extends DbTestBase {

    @Autowired
    DishMapper mapper;

    @Autowired
    DishSortService sortService;

    private static List<Long> ids(List<Dish> list) {
        return list.stream().map(Dish::getId).toList();
    }

    private static DishQuery query(String name, Long categoryId, Integer status, String min, String max) {
        DishQuery q = new DishQuery();
        q.setName(name);
        q.setCategoryId(categoryId);
        q.setStatus(status);
        q.setMinPrice(min == null ? null : new BigDecimal(min));
        q.setMaxPrice(max == null ? null : new BigDecimal(max));
        return q;
    }

    @Test
    @DisplayName("2.1 <where> + <if> 条件查询")
    void selectList() {
        assertEquals(20, mapper.selectList(new DishQuery()).size(), "一个条件都没有时查全部");
        assertEquals(List.of(1L, 6L, 9L, 19L), ids(mapper.selectList(query("鸡", null, null, null, null))));
        assertEquals(20, mapper.selectList(query("", null, null, null, null)).size(), "name 是空字符串时不过滤");
        assertEquals(List.of(7L, 8L, 9L, 10L, 20L), ids(mapper.selectList(query(null, 2L, null, null, null))));
        assertEquals(List.of(5L, 10L, 18L), ids(mapper.selectList(query(null, null, 0, null, null))));
        assertEquals(List.of(1L, 2L, 5L, 8L, 9L, 19L), ids(mapper.selectList(query(null, null, null, "26", "38"))),
                "价格范围包含两端");
        assertEquals(List.of(3L, 6L), ids(mapper.selectList(query(null, 1L, 1, "40", null))));
        assertEquals(List.of(11L, 15L), ids(mapper.selectList(query(null, null, null, null, "5"))));

        Dish d = mapper.selectList(query("宫保", null, null, null, null)).get(0);
        assertEquals(1L, d.getCategoryId(), "category_id 要映射到 categoryId");
        assertEquals(0, new BigDecimal("28.00").compareTo(d.getPrice()));
    }

    @Test
    @DisplayName("2.2 <sql> + <include> 复用条件")
    void selectCount() {
        assertEquals(20, mapper.selectCount(new DishQuery()));
        assertEquals(4, mapper.selectCount(query("鸡", null, null, null, null)));
        assertEquals(2, mapper.selectCount(query(null, 1L, 1, "40", null)));
    }

    @Test
    @DisplayName("2.3 <set> 选择性更新")
    void updateSelective() {
        Dish d = new Dish();
        d.setId(1L);
        d.setPrice(new BigDecimal("30.00"));
        assertEquals(1, mapper.updateSelective(d));
        Map<String, Object> row = row("SELECT * FROM dish WHERE id = 1");
        assertEquals(0, new BigDecimal("30.00").compareTo((BigDecimal) row.get("price")));
        assertEquals("宫保鸡丁", row.get("name"), "没传的字段不能被改成 null");
        assertEquals("经典川菜", row.get("description"));
        assertTrue(((LocalDateTime) row.get("update_time")).isAfter(LocalDateTime.of(2025, 1, 1, 0, 0)), "update_time 要更新为 NOW()");

        Dish d2 = new Dish();
        d2.setId(5L);
        d2.setName("老坛回锅肉");
        d2.setStatus(1);
        d2.setDescription("好吃");
        assertEquals(1, mapper.updateSelective(d2));
        row = row("SELECT * FROM dish WHERE id = 5");
        assertEquals("老坛回锅肉", row.get("name"));
        assertEquals(1, ((Number) row.get("status")).intValue());
        assertEquals("好吃", row.get("description"));
        assertEquals(0, new BigDecimal("32.00").compareTo((BigDecimal) row.get("price")));
    }

    @Test
    @DisplayName("2.4 + 2.5 <foreach> 批量查询、批量删除")
    void foreach() {
        assertEquals(List.of(1L, 3L, 20L), ids(mapper.selectByIds(List.of(20L, 3L, 1L))));
        assertEquals(List.of(2L), ids(mapper.selectByIds(List.of(2L, 99L))));

        assertEquals(2, mapper.deleteByIds(List.of(10L, 18L, 99L)));
        assertEquals(18, count("SELECT COUNT(*) FROM dish"));
        assertEquals(0, count("SELECT COUNT(*) FROM dish WHERE id IN (10, 18)"));
    }

    @Test
    @DisplayName("2.6 <foreach> 批量插入")
    void insertFlavors() {
        int n = mapper.insertFlavors(List.of(
                new DishFlavor(null, 7L, "辣度", "不辣,微辣"),
                new DishFlavor(null, 7L, "忌口", "不要蒜"),
                new DishFlavor(null, 13L, "辣度", "微辣,中辣")));
        assertEquals(3, n);
        assertEquals(2, count("SELECT COUNT(*) FROM dish_flavor WHERE dish_id = 7"));
        assertEquals("微辣,中辣", row("SELECT value FROM dish_flavor WHERE dish_id = 13").get("value"));
    }

    @Test
    @DisplayName("2.7 <choose> + ${} 排序")
    void selectOrderBy() {
        List<Long> byPriceDesc = ids(mapper.selectOrderBy("price", false));
        assertEquals(List.of(3L, 6L, 19L, 8L, 5L, 9L), byPriceDesc.subList(0, 6), "价格相同（32.00）时按 id 升序");
        List<Long> byPriceAsc = ids(mapper.selectOrderBy("price", true));
        assertEquals(List.of(11L, 15L, 14L, 16L), byPriceAsc.subList(0, 4));
    }

    @Test
    @DisplayName("2.8 排序字段白名单")
    void sortWhitelist() {
        assertEquals(3L, sortService.listSorted("price", false).get(0).getId());
        assertEquals(20L, sortService.listSorted("createTime", false).get(0).getId());
        assertEquals(1L, sortService.listSorted("id", true).get(0).getId());

        ServiceException e = assertThrows(ServiceException.class,
                () -> sortService.listSorted("price; DROP TABLE dish; --", true));
        assertEquals("不支持的排序字段：price; DROP TABLE dish; --", e.getMessage());
        assertThrows(ServiceException.class, () -> sortService.listSorted("create_time", true),
                "只接受前端字段名 createTime，不接受列名");
        assertEquals(20, count("SELECT COUNT(*) FROM dish"), "dish 表还在");
    }
}
