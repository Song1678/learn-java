package com.learn.ch06_generics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch06 泛型")
class GenericsTest {

    @Test
    @DisplayName("练习1：R<T> 统一返回结果")
    void r() {
        R<Void> r1 = R.ok();
        assertEquals(200, r1.getCode());
        assertEquals("操作成功", r1.getMsg());
        assertNull(r1.getData());
        assertTrue(r1.isSuccess());

        R<List<String>> r2 = R.ok(List.of("a", "b"));
        assertEquals(List.of("a", "b"), r2.getData());

        R<Integer> r3 = R.ok("查询成功", 42);
        assertEquals("查询成功", r3.getMsg());
        assertEquals(42, r3.getData());

        R<String> r4 = R.fail("用户名已存在");
        assertEquals(500, r4.getCode());
        assertFalse(r4.isSuccess());

        R<String> r5 = R.fail(401, "未登录");
        assertEquals(401, r5.getCode());
        assertEquals("未登录", r5.getMsg());
    }

    @Test
    @DisplayName("练习2：Pair<A, B>")
    void pair() {
        Pair<String, Integer> p = Pair.of("age", 18);
        assertEquals("age", p.getFirst());
        assertEquals(18, p.getSecond());
        Pair<Integer, String> s = p.swap();
        assertEquals(18, s.getFirst());
        assertEquals("(18, age)", s.toString());
        assertEquals(new Pair<>("age", 18), p);
        assertEquals(new Pair<>("age", 18).hashCode(), p.hashCode());
        assertNotEquals(Pair.of("age", 19), p);
        assertEquals(Pair.of(null, 1), Pair.of(null, 1));
    }

    @Test
    @DisplayName("练习3：分页")
    void page() {
        List<Integer> all = IntStream.rangeClosed(1, 10).boxed().toList();
        PageResult<Integer> p2 = PageResult.of(all, 2, 3);
        assertEquals(10, p2.getTotal());
        assertEquals(List.of(4, 5, 6), p2.getRows());
        assertEquals(List.of(10), PageResult.of(all, 4, 3).getRows());
        assertEquals(List.of(), PageResult.of(all, 5, 3).getRows());
        assertEquals(List.of(), PageResult.of(List.<Integer>of(), 1, 10).getRows());
        assertEquals(List.of(), PageResult.of(all, Integer.MAX_VALUE, Integer.MAX_VALUE).getRows(), "注意 int 溢出");
        assertThrows(IllegalArgumentException.class, () -> PageResult.of(all, 0, 3));
        assertThrows(IllegalArgumentException.class, () -> PageResult.of(all, 1, 0));
    }

    @Test
    @DisplayName("练习4：泛型方法与通配符")
    void utils() {
        assertEquals(9, GenericUtils.max(List.of(3, 9, 1)));
        assertEquals("c", GenericUtils.max(List.of("b", "c", "a")));
        assertNull(GenericUtils.max(List.<Integer>of()));

        assertEquals(6.0, GenericUtils.sum(List.of(1, 2, 3)), 1e-9);
        assertEquals(3.5, GenericUtils.sum(List.of(1.5, 2.0)), 1e-9);
        assertEquals(0.3, GenericUtils.sum(List.of(new BigDecimal("0.1"), new BigDecimal("0.2"))), 1e-9);

        List<Number> dest = new ArrayList<>(List.of(0.5));
        GenericUtils.copy(dest, List.of(1, 2));
        assertEquals(List.of(0.5, 1, 2), dest);

        Map<String, Integer> inv = GenericUtils.invert(Map.of(1, "待付款", 2, "待接单"));
        assertEquals(Map.of("待付款", 1, "待接单", 2), inv);
    }

    /** 测试用实体 */
    record Dish(Long id, String name) implements Identifiable<Long> {
        @Override
        public Long getId() {
            return id;
        }
    }

    @Test
    @DisplayName("练习5：InMemoryRepository")
    void repository() {
        InMemoryRepository<Dish, Long> repo = new InMemoryRepository<>();
        repo.save(new Dish(1L, "宫保鸡丁"));
        repo.save(new Dish(2L, "鱼香肉丝"));
        repo.save(new Dish(1L, "宫保鸡丁(新)"));
        assertEquals(2, repo.count());
        assertEquals("宫保鸡丁(新)", repo.findById(1L).name());
        assertNull(repo.findById(3L));
        assertEquals(List.of(1L, 2L), repo.findAll().stream().map(Dish::id).toList());

        repo.findAll().clear();
        assertEquals(2, repo.count(), "findAll 应该返回拷贝");

        assertTrue(repo.deleteById(1L));
        assertFalse(repo.deleteById(1L));
        assertEquals(1, repo.count());
        assertThrows(IllegalArgumentException.class, () -> repo.save(new Dish(null, "x")));
        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
    }
}
