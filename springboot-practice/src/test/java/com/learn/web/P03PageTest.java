package com.learn.web;

import com.learn.web.common.PageQuery;
import com.learn.web.common.TableDataInfo;
import com.learn.web.p03_page.Dish;
import com.learn.web.p03_page.DishBo;
import com.learn.web.p03_page.DishPageService;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("p03 分页、自动填充、逻辑删除")
class P03PageTest extends DbTestBase {

    @Autowired
    DishPageService service;

    private static PageQuery page(int num, int size) {
        PageQuery pq = new PageQuery();
        pq.setPageNum(num);
        pq.setPageSize(size);
        return pq;
    }

    private static List<Long> ids(TableDataInfo<Dish> t) {
        return t.getRows().stream().map(Dish::getId).toList();
    }

    @Test
    @DisplayName("3.1 + 3.5 分页插件")
    void pagination() {
        TableDataInfo<Dish> p2 = service.queryPageList(new DishBo(), page(2, 5));
        assertEquals(5, p2.getRows().size(), "每页 5 条。如果返回了 20 条，说明分页插件没生效（3.1）");
        assertEquals(20, p2.getTotal(), "total 是总条数。如果是 0，说明分页插件没生效（3.1）");
        assertEquals(List.of(6L, 7L, 8L, 9L, 10L), ids(p2));

        DishBo bo = new DishBo();
        bo.setCategoryId(1L);
        TableDataInfo<Dish> hot = service.queryPageList(bo, page(1, 3));
        assertEquals(7, hot.getTotal());
        assertEquals(List.of(1L, 2L, 3L), ids(hot));

        bo.setStatus(1);
        bo.setName("鸡");
        TableDataInfo<Dish> chicken = service.queryPageList(bo, page(1, 10));
        assertEquals(List.of(1L, 6L, 19L), ids(chicken));
        assertEquals(3, chicken.getTotal());

        assertEquals(0, service.queryPageList(new DishBo(), page(5, 5)).getRows().size());
    }

    @Test
    @DisplayName("3.2 + 3.3 + 3.6 插入时自动填充")
    void insertFill() {
        Dish d = new Dish();
        d.setName("小笼包");
        d.setCategoryId(3L);
        d.setPrice(new BigDecimal("18.00"));
        d.setStatus(1);
        LocalDateTime before = LocalDateTime.now().minusSeconds(5);
        Long id = service.add(d);
        assertEquals(21L, id);
        Map<String, Object> row = row("SELECT * FROM dish WHERE id = 21");
        assertNotNull(row.get("create_time"), "create_time 要自动填充");
        assertNotNull(row.get("update_time"), "update_time 要自动填充");
        assertTrue(((LocalDateTime) row.get("create_time")).isAfter(before));
        assertEquals(0, ((Number) row.get("del_flag")).intValue());
    }

    @Test
    @DisplayName("3.2 + 3.3 + 3.7 更新时自动填充")
    void updateFill() {
        assertTrue(service.changePrice(1L, new BigDecimal("30.00")));
        Map<String, Object> row = row("SELECT * FROM dish WHERE id = 1");
        assertEquals(0, new BigDecimal("30.00").compareTo((BigDecimal) row.get("price")));
        assertTrue(((LocalDateTime) row.get("update_time")).isAfter(LocalDateTime.of(2025, 1, 1, 0, 0)),
                "update_time 要自动更新为当前时间");
        assertEquals(LocalDateTime.of(2024, 4, 5, 10, 10), row.get("create_time"), "create_time 不能被改");
        assertFalse(service.changePrice(99L, BigDecimal.ONE));
    }

    @Test
    @DisplayName("3.4 + 3.8 逻辑删除")
    void logicDelete() {
        assertTrue(service.remove(10L));
        assertEquals(1, count("SELECT COUNT(*) FROM dish WHERE id = 10"), "逻辑删除：数据还在表里");
        assertEquals(1, count("SELECT del_flag FROM dish WHERE id = 10"), "del_flag 变成 1");
        assertNull(service.getById(10L), "逻辑删除的数据查不到");
        assertEquals(19, service.queryPageList(new DishBo(), page(1, 10)).getTotal(), "分页的 total 也不算它");
        assertFalse(service.remove(10L), "已经删过的再删返回 false");
    }
}
