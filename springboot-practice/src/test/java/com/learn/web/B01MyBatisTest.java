package com.learn.web;

import com.learn.web.b01_mybatis.Category;
import com.learn.web.b01_mybatis.CategoryMapper;
import com.learn.web.b01_mybatis.CategoryService;
import com.learn.web.common.ServiceException;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("b01 MyBatis 注解 SQL")
class B01MyBatisTest extends DbTestBase {

    @Autowired
    CategoryMapper mapper;

    @Autowired
    CategoryService service;

    private static List<String> names(List<Category> list) {
        return list.stream().map(Category::getName).toList();
    }

    @Test
    @DisplayName("1.1 selectById")
    void selectById() {
        Category c = mapper.selectById(2L);
        assertNotNull(c);
        assertEquals("凉菜", c.getName());
        assertEquals(1, c.getType());
        assertEquals(2, c.getSort());
        assertEquals(LocalDateTime.of(2024, 4, 1, 9, 5), c.getCreateTime(), "create_time 应该自动对应 createTime（驼峰映射）");
        assertNull(mapper.selectById(99L));
    }

    @Test
    @DisplayName("1.2 selectAll")
    void selectAll() {
        assertEquals(List.of("热菜", "凉菜", "主食", "饮品", "汤羹", "超值套餐", "甜品"), names(mapper.selectAll()));
        jdbc.update("UPDATE category SET sort = 2 WHERE id = 1");
        assertEquals(List.of("热菜", "凉菜"), names(mapper.selectAll()).subList(0, 2), "sort 相同按 id 升序");
    }

    @Test
    @DisplayName("1.3 多个参数 @Param")
    void selectByTypeAndStatus() {
        assertEquals(List.of("热菜", "凉菜", "主食", "饮品", "甜品"), names(mapper.selectByTypeAndStatus(1, 1)));
        assertEquals(List.of("汤羹"), names(mapper.selectByTypeAndStatus(1, 0)));
        assertEquals(List.of("超值套餐"), names(mapper.selectByTypeAndStatus(2, 1)));
    }

    @Test
    @DisplayName("1.4 + 1.5 返回简单类型")
    void simpleTypes() {
        assertEquals(6, mapper.countByStatus(1));
        assertEquals(1, mapper.countByStatus(0));
        assertEquals(List.of("热菜", "凉菜", "主食", "饮品", "汤羹", "超值套餐", "甜品"), mapper.selectNames());
    }

    @Test
    @DisplayName("1.6 LIKE 模糊查询")
    void searchByName() {
        assertEquals(List.of("热菜", "凉菜"), names(mapper.searchByName("菜")));
        assertEquals(List.of("超值套餐"), names(mapper.searchByName("套餐")));
        assertTrue(mapper.searchByName("火锅").isEmpty());
        assertTrue(mapper.searchByName("' OR '1'='1").isEmpty(), "#{} 是预编译参数，不会被 SQL 注入");
    }

    @Test
    @DisplayName("1.7 insert + 回填自增 id")
    void insert() {
        Category c = new Category();
        c.setType(1);
        c.setName("烧烤");
        c.setSort(8);
        c.setStatus(1);
        assertEquals(1, mapper.insert(c));
        assertEquals(8L, c.getId(), "插入后 category.id 应该被回填为数据库生成的 8");
        Category saved = mapper.selectById(8L);
        assertEquals("烧烤", saved.getName());
        assertNotNull(saved.getCreateTime(), "create_time 要用 NOW()");
        assertNotNull(saved.getUpdateTime(), "update_time 要用 NOW()");
    }

    @Test
    @DisplayName("1.8 + 1.9 update / delete")
    void updateAndDelete() {
        assertEquals(1, mapper.updateSort(7L, 0));
        assertEquals("甜品", mapper.selectAll().get(0).getName());
        assertTrue(mapper.selectById(7L).getUpdateTime().isAfter(LocalDateTime.of(2025, 1, 1, 0, 0)), "update_time 要更新为 NOW()");
        assertEquals(0, mapper.updateSort(99L, 1));

        assertEquals(1, mapper.deleteById(7L));
        assertNull(mapper.selectById(7L));
        assertEquals(0, mapper.deleteById(7L));
    }

    @Test
    @DisplayName("1.10 Service 调用 Mapper")
    void service() {
        Long id = service.addDishCategory("烧烤", 8);
        assertEquals(8L, id);
        Category c = mapper.selectById(id);
        assertEquals(1, c.getType());
        assertEquals(1, c.getStatus());
        assertEquals(8, c.getSort());

        assertEquals("分类名称已存在", assertThrows(ServiceException.class, () -> service.addDishCategory("热菜", 1)).getMessage());
        assertEquals("分类名称不能为空", assertThrows(ServiceException.class, () -> service.addDishCategory(" ", 1)).getMessage());
        assertEquals(8, count("SELECT COUNT(*) FROM category"));
        assertNotNull(service.addDishCategory("热", 1), "“热”和“热菜”不是同一个名字");
    }
}
