package com.learn.ch07_collections;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 练习 5：LRU 缓存（最近最少使用淘汰）—— 面试高频题，也是理解 Redis 淘汰策略的入门。
 *
 * <p>容量满了再放新数据时，淘汰“最久没被访问（get 或 put）”的那个。
 *
 * <p>提示：LinkedHashMap 有一个构造方法 new LinkedHashMap&lt;&gt;(16, 0.75f, true)，
 * 第三个参数 true 表示按“访问顺序”排序（最近访问的在最后）。
 * 再重写它的 protected boolean removeEldestEntry(Map.Entry eldest) 方法即可（可以用匿名内部类）。
 * 也可以完全自己用 HashMap + 双向链表实现，挑战一下。
 */
public class LRUCache<K, V> {

    // @hide 声明需要的字段
    private final Map<K, V> map;
    // @end

    public LRUCache(int capacity) {
        // @answer
        if (capacity < 1) {
            throw new IllegalArgumentException("容量至少为1");
        }
        this.map = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
        // @end
    }

    /** 不存在返回 null */
    public V get(K key) {
        // @answer
        return map.get(key);
        // @end
    }

    public void put(K key, V value) {
        // @answer
        map.put(key, value);
        // @end
    }

    public int size() {
        // @answer
        return map.size();
        // @end
    }

    public boolean containsKey(K key) {
        // @answer 注意：containsKey 不算“访问”，不改变顺序（LinkedHashMap.containsKey 正好如此）
        return map.containsKey(key);
        // @end
    }
}
