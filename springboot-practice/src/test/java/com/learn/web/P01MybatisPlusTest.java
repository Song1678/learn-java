package com.learn.web;

import com.learn.web.p01_mp.domain.Category;
import com.learn.web.p01_mp.domain.Dish;
import com.learn.web.p01_mp.service.CategoryMpService;
import com.learn.web.p01_mp.service.IDishMpService;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("p01 MyBatis-Plus 入门")
class P01MybatisPlusTest extends DbTestBase {

    @Autowired
    CategoryMpService categoryService;

    @Autowired
    IDishMpService dishService;

    @Test
    @DisplayName("1.1 + 1.3 @TableId 自增主键")
    void addCategory() {
        Long id = categoryService.add("烧烤", 1, 8);
        assertNotNull(id);
        assertEquals(8L, id, "id 应该是数据库自增的 8。如果是一个 19 位的大数字，说明用了默认的雪花算法，检查 @TableId(type = ...)");
        Category c = categoryService.getById(8L);
        assertEquals("烧烤", c.getName());
        assertEquals(1, c.getStatus());
        assertNotNull(c.getCreateTime());
    }

    @Test
    @DisplayName("1.4 + 1.5 查询")
    void select() {
        assertEquals("饮品", categoryService.getById(4L).getName());
        assertNull(categoryService.getById(99L));
        List<String> names = categoryService.listByIds(List.of(1L, 3L, 99L)).stream().map(Category::getName).sorted().toList();
        assertEquals(List.of("主食", "热菜"), names);
    }

    @Test
    @DisplayName("1.6 updateById 不更新 null 字段")
    void rename() {
        assertTrue(categoryService.rename(2L, "冷盘"));
        Category c = categoryService.getById(2L);
        assertEquals("冷盘", c.getName());
        assertEquals(2, c.getSort(), "没 set 的字段不能被改成 null");
        assertEquals(1, c.getType());
        assertFalse(categoryService.rename(99L, "xx"));
    }

    @Test
    @DisplayName("1.7 + 1.8 删除、计数")
    void removeAndCount() {
        assertEquals(7, categoryService.countAll());
        assertTrue(categoryService.remove(7L));
        assertFalse(categoryService.remove(7L));
        assertEquals(6, categoryService.countAll());
    }

    @Test
    @DisplayName("1.2 @TableField(exist = false)")
    void existFalse() {
        Dish d = dishService.getById(1L);
        assertNotNull(d, "查不出来？看看报错是不是 Unknown column 'category_name'");
        assertEquals("宫保鸡丁", d.getName());
        assertNull(d.getCategoryName());
    }

    @Test
    @DisplayName("1.9 lambdaQuery")
    void listOnSale() {
        List<String> names = dishService.listOnSaleByCategory(2L).stream().map(Dish::getName).toList();
        assertEquals(List.of("拍黄瓜", "凉拌土豆丝", "口水鸡", "夫妻肺片"), names, "凉拌木耳停售了，不要");
    }

    @Test
    @DisplayName("1.10 one()")
    void getByName() {
        assertEquals(3L, dishService.getByName("水煮鱼").getId());
        assertNull(dishService.getByName("佛跳墙"));
    }

    @Test
    @DisplayName("1.11 IService 自带方法 + 避免 N+1")
    void withCategoryName() {
        Dish a = new Dish();
        a.setName("小笼包");
        a.setCategoryId(3L);
        a.setPrice(new BigDecimal("18"));
        a.setStatus(1);
        assertTrue(dishService.save(a), "save 是 IService 自带的");
        assertEquals(21L, a.getId());

        List<Dish> list = dishService.listWithCategoryName(List.of(21L, 15L, 1L, 7L));
        assertEquals(List.of(1L, 7L, 15L, 21L), list.stream().map(Dish::getId).toList(), "按 id 升序");
        assertEquals(List.of("热菜", "凉菜", "饮品", "主食"), list.stream().map(Dish::getCategoryName).toList());
        assertTrue(dishService.listWithCategoryName(List.of(99L)).isEmpty());
    }
}
