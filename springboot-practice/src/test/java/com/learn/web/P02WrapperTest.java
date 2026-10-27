package com.learn.web;

import com.learn.web.p02_wrapper.Dish;
import com.learn.web.p02_wrapper.DishQuery;
import com.learn.web.p02_wrapper.DishWrapperService;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("p02 条件构造器")
class P02WrapperTest extends DbTestBase {

    @Autowired
    DishWrapperService service;

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
    @DisplayName("2.1 动态条件 + 排序")
    void search() {
        assertEquals(20, service.search(new DishQuery()).size());
        assertEquals(20, service.search(query("  ", null, null, null, null)).size(), "name 是空白时不过滤");
        assertEquals(List.of(1L, 9L, 19L, 6L), ids(service.search(query("鸡", null, null, null, null))), "按价格升序");
        assertEquals(List.of(7L, 20L, 10L, 9L, 8L), ids(service.search(query(null, 2L, null, null, null))));
        assertEquals(List.of(4L, 2L, 1L), ids(service.search(query(null, 1L, 1, "18", "30"))));
        assertEquals(List.of(18L, 10L, 5L), ids(service.search(query(null, null, 0, null, null))));
    }

    @Test
    @DisplayName("2.2 and() 生成括号")
    void searchOnSale() {
        assertEquals(List.of(2L, 4L, 6L, 8L), ids(service.searchOnSale("辣")),
                "名称或描述包含“辣”、并且起售中");
        jdbc.update("UPDATE dish SET status = 0 WHERE id = 8");
        assertEquals(List.of(2L, 4L, 6L), ids(service.searchOnSale("辣")), "夫妻肺片停售后不能再查出来（检查括号）");
        assertEquals(List.of(16L), ids(service.searchOnSale("腻")));
    }

    @Test
    @DisplayName("2.3 select 指定列")
    void listNames() {
        assertEquals(List.of("可乐", "酸梅汤", "鲜榨橙汁", "柠檬茶"), service.listNames(4L));
        assertTrue(service.listNames(99L).isEmpty());
    }

    @Test
    @DisplayName("2.4 last")
    void cheapest() {
        assertEquals(List.of(11L, 15L, 14L), ids(service.cheapest(3)));
        assertEquals(List.of(11L, 15L, 14L, 16L, 7L), ids(service.cheapest(5)), "柠檬茶 12 元但停售了，不算");
    }

    @Test
    @DisplayName("2.5 exists")
    void exists() {
        assertTrue(service.existsByName("米饭"));
        assertFalse(service.existsByName("米"));
    }

    @Test
    @DisplayName("2.6 分组统计 selectMaps")
    void countByCategory() {
        Map<Long, Long> m = service.countByCategory();
        assertEquals(Map.of(1L, 7L, 2L, 5L, 3L, 4L, 4L, 4L), m);
        assertEquals(List.of(1L, 2L, 3L, 4L), List.copyOf(m.keySet()), "按分类 id 排序");
    }

    @Test
    @DisplayName("2.7 LambdaUpdateWrapper")
    void stopSale() {
        assertEquals(4, service.stopSaleByCategory(2L), "凉菜里起售的有 4 个");
        assertEquals(5, count("SELECT COUNT(*) FROM dish WHERE category_id = 2 AND status = 0"));
        assertEquals(0, service.stopSaleByCategory(2L));
        assertEquals(13, count("SELECT COUNT(*) FROM dish WHERE status = 1"), "别的分类不能动");
    }

    @Test
    @DisplayName("2.8 setIncrBy")
    void raisePrice() {
        assertEquals(4, service.raisePrice(4L, new BigDecimal("1.50")));
        assertEquals(0, new BigDecimal("6.50").compareTo(jdbc.queryForObject("SELECT price FROM dish WHERE id = 15", BigDecimal.class)));
        assertEquals(0, new BigDecimal("13.50").compareTo(jdbc.queryForObject("SELECT price FROM dish WHERE id = 18", BigDecimal.class)));
        assertEquals(0, new BigDecimal("28.00").compareTo(jdbc.queryForObject("SELECT price FROM dish WHERE id = 1", BigDecimal.class)),
                "别的分类不能动");
    }
}
