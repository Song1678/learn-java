package com.learn.ch07_collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch07 集合")
class CollectionsTest {

    private static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    @Test
    @DisplayName("1.1 去重保持顺序")
    void dedup() {
        assertEquals(List.of(3, 1, 2), CollectionBasics.dedupKeepOrder(List.of(3, 1, 3, 2, 1)));
    }

    @Test
    @DisplayName("1.2 按长度分组")
    void groupByLength() {
        Map<Integer, List<String>> m = CollectionBasics.groupByLength(List.of("a", "bb", "cc", "d", "eee"));
        assertEquals(Map.of(1, List.of("a", "d"), 2, List.of("bb", "cc"), 3, List.of("eee")), m);
        assertEquals(List.of(1, 2, 3), new ArrayList<>(m.keySet()), "key 要从小到大");
    }

    @Test
    @DisplayName("1.3 交集")
    void intersection() {
        assertEquals(List.of(2, 4), CollectionBasics.intersection(List.of(1, 2, 2, 3, 4), List.of(4, 2, 5)));
        assertEquals(List.of(), CollectionBasics.intersection(List.of(1), List.of()));
    }

    @Test
    @DisplayName("1.4 Iterator 删除")
    void removeNegatives() {
        List<Integer> list = new ArrayList<>(List.of(1, -2, -3, 4, -5));
        CollectionBasics.removeNegatives(list);
        assertEquals(List.of(1, 4), list);
    }

    @Test
    @DisplayName("1.5 括号匹配")
    void brackets() {
        assertTrue(CollectionBasics.isValidBrackets("{[()]}"));
        assertTrue(CollectionBasics.isValidBrackets("a(b)c"));
        assertTrue(CollectionBasics.isValidBrackets(""));
        assertFalse(CollectionBasics.isValidBrackets("([)]"));
        assertFalse(CollectionBasics.isValidBrackets("(("));
        assertFalse(CollectionBasics.isValidBrackets("())"));
    }

    @Test
    @DisplayName("2 单词计数与 TopN")
    void wordCounter() {
        Map<String, Integer> counts = WordCounter.count("Hello world, hello Java! JAVA is fun; hello?");
        assertEquals(Map.of("hello", 3, "world", 1, "java", 2, "is", 1, "fun", 1), counts);
        assertEquals(List.of("hello", "java", "fun"), WordCounter.topN(counts, 3));
        assertEquals(5, WordCounter.topN(counts, 100).size());
        assertEquals(Map.of(), WordCounter.count("  ,,  "));
    }

    @Test
    @DisplayName("3 equals 与 hashCode")
    void equalsHashCode() {
        Dish a = new Dish("宫保鸡丁", bd("28"));
        Dish b = new Dish("宫保鸡丁", bd("28.00"));
        Dish c = new Dish("宫保鸡丁", bd("29"));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "宫保鸡丁");

        Set<Dish> set = new HashSet<>(List.of(a, b, c));
        assertEquals(2, set.size());

        Map<Dish, Integer> sales = new HashMap<>();
        sales.put(a, 10);
        assertEquals(10, sales.get(new Dish("宫保鸡丁", bd("28.0"))));
    }

    @Test
    @DisplayName("4 购物车")
    void cart() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("宫保鸡丁", bd("28"));
        cart.add("米饭", bd("2"));
        cart.add("宫保鸡丁", bd("99"));
        assertEquals(2, cart.quantityOf("宫保鸡丁"));
        assertEquals(0, bd("58").compareTo(cart.total()));
        assertEquals(List.of(
                new ShoppingCart.Item("宫保鸡丁", bd("28"), 2),
                new ShoppingCart.Item("米饭", bd("2"), 1)), cart.list());

        cart.sub("米饭");
        cart.sub("不存在");
        assertEquals(0, cart.quantityOf("米饭"));
        assertEquals(1, cart.list().size());

        cart.list().clear();
        assertEquals(1, cart.list().size(), "list() 要返回拷贝");

        cart.clear();
        assertEquals(0, BigDecimal.ZERO.compareTo(cart.total()));
    }

    @Test
    @DisplayName("5 LRU 缓存")
    void lru() {
        LRUCache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        assertEquals(1, cache.get("a")); // 访问 a，b 变成最久未使用
        cache.put("c", 3);               // 淘汰 b
        assertFalse(cache.containsKey("b"));
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("c"));
        cache.put("a", 10);              // 更新 a，c 变成最久未使用
        cache.put("d", 4);               // 淘汰 c
        assertNull(cache.get("c"));
        assertEquals(10, cache.get("a"));
        assertEquals(2, cache.size());
        assertThrows(IllegalArgumentException.class, () -> new LRUCache<String, String>(0));
    }

    @Test
    @DisplayName("6 优先队列")
    void orderQueue() {
        OrderQueue q = new OrderQueue();
        q.submit(new OrderQueue.Order("A", false, 1));
        q.submit(new OrderQueue.Order("B", true, 5));
        q.submit(new OrderQueue.Order("C", false, 0));
        q.submit(new OrderQueue.Order("D", true, 3));
        assertEquals(4, q.size());
        assertEquals("D", q.next().orderNo());
        assertEquals("B", q.next().orderNo());
        assertEquals("C", q.next().orderNo());
        assertEquals("A", q.next().orderNo());
        assertNull(q.next());
    }
}
