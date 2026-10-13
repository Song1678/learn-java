package com.learn.ch09_takeout;

import com.learn.ch09_takeout.domain.Category;
import com.learn.ch09_takeout.mapper.CategoryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch09 练习9.1：InMemoryMapper")
class MapperTest {

    @Test
    void insertAndSelect() {
        CategoryMapper m = new CategoryMapper();
        Category a = new Category("川菜", 1, 1);
        Category b = new Category("主食", 2, 0);
        assertEquals(1, m.insert(a));
        m.insert(b);
        assertEquals(1L, a.getId());
        assertEquals(2L, b.getId());
        assertNotNull(a.getCreateTime());
        assertNotNull(a.getUpdateTime());
        assertSame(a, m.selectById(1L));
        assertNull(m.selectById(3L));
        assertEquals(List.of(a, b), m.selectList());
        assertEquals(List.of(b), m.selectList(c -> c.getStatus() == 0));
        assertEquals(1, m.selectCount(c -> c.getName().startsWith("川")));
    }

    @Test
    void updateAndDelete() {
        CategoryMapper m = new CategoryMapper();
        Category a = new Category("川菜", 1, 1);
        m.insert(a);
        a.setName("湘菜");
        assertEquals(1, m.updateById(a));
        assertEquals("湘菜", m.selectById(1L).getName());
        assertEquals(0, m.updateById(new Category("x", 1, 1)), "id 为 null");
        Category ghost = new Category("x", 1, 1);
        ghost.setId(99L);
        assertEquals(0, m.updateById(ghost), "id 不存在");

        assertEquals(1, m.deleteById(1L));
        assertEquals(0, m.deleteById(1L));
        assertTrue(m.selectList().isEmpty());

        Category c = new Category("粤菜", 1, 1);
        m.insert(c);
        assertEquals(2L, c.getId(), "删除后 id 也不会复用");
    }
}
