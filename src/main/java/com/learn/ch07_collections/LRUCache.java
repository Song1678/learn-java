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

    // TODO 声明需要的字段

    public LRUCache(int capacity) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 不存在返回 null */
    public V get(K key) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public void put(K key, V value) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public int size() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public boolean containsKey(K key) {
        // TODO 注意：containsKey 不算“访问”，不改变顺序（LinkedHashMap.containsKey 正好如此）
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
