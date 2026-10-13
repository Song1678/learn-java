package com.learn.ch07_collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 练习 2：用 Map 计数 + 排序
 */
public class WordCounter {

    private WordCounter() {
    }

    /**
     * 2.1 统计英文单词出现次数（忽略大小写，单词只由字母组成，其他字符都当作分隔符）。
     * <pre>
     * "Hello world, hello Java!" -> {hello=2, world=1, java=1}
     * </pre>
     * 提示：text.toLowerCase().split("[^a-z]+")，注意 split 结果里可能有空字符串；
     * map.merge(word, 1, Integer::sum) 或 map.put(word, map.getOrDefault(word, 0) + 1)
     */
    public static Map<String, Integer> count(String text) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 2.2 返回出现次数最多的前 n 个单词：次数从多到少；次数相同时按字母顺序。
     * n 大于单词总数时返回全部。
     * 提示：把 map.entrySet() 放进 ArrayList，然后用 list.sort(比较器) 排序。
     * 比较器可以写成匿名类 new Comparator&lt;Map.Entry&lt;String, Integer&gt;&gt;() { public int compare(...) {...} }
     */
    public static List<String> topN(Map<String, Integer> counts, int n) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
