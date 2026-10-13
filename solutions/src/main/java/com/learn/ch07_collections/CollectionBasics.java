package com.learn.ch07_collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 练习 1：List / Set / Map / Deque 基本用法（对应「集合 - 使用List / Map / Set / Queue / Deque / Stack / Iterator」）
 *
 * <p>JS 对照：Array ≈ ArrayList，Map ≈ HashMap / LinkedHashMap，Set ≈ HashSet / LinkedHashSet。
 * 注意集合里只能放对象，int 会自动装箱成 Integer。
 */
public class CollectionBasics {

    private CollectionBasics() {
    }

    /**
     * 1.1 去重并保持原来的顺序。
     * <pre>
     * [3, 1, 3, 2, 1] -> [3, 1, 2]
     * </pre>
     * 提示：LinkedHashSet 会保持插入顺序。
     */
    public static List<Integer> dedupKeepOrder(List<Integer> list) {
        // @answer
        return new ArrayList<>(new LinkedHashSet<>(list));
        // @end
    }

    /**
     * 1.2 按字符串长度分组，key 从小到大排序。
     * <pre>
     * ["a", "bb", "cc", "d", "eee"] -> {1=[a, d], 2=[bb, cc], 3=[eee]}
     * </pre>
     * 提示：TreeMap 会按 key 排序；map.computeIfAbsent(key, k -&gt; new ArrayList&lt;&gt;()).add(s)
     */
    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        // @answer
        Map<Integer, List<String>> map = new TreeMap<>();
        for (String w : words) {
            map.computeIfAbsent(w.length(), k -> new ArrayList<>()).add(w);
        }
        return map;
        // @end
    }

    /**
     * 1.3 求交集：返回同时出现在 a 和 b 中的元素，按在 a 中出现的顺序，结果不重复。
     * <pre>
     * a=[1, 2, 2, 3, 4], b=[4, 2, 5] -> [2, 4]
     * </pre>
     * 提示：先把 b 放进 HashSet，contains 是 O(1) 的；List.contains 是 O(n) 的。
     */
    public static List<Integer> intersection(List<Integer> a, List<Integer> b) {
        // @answer
        Set<Integer> setB = new HashSet<>(b);
        Set<Integer> result = new LinkedHashSet<>();
        for (Integer x : a) {
            if (setB.contains(x)) {
                result.add(x);
            }
        }
        return new ArrayList<>(result);
        // @end
    }

    /**
     * 1.4 用 Iterator 原地删除所有负数。
     * 注意：在 for-each 循环里调用 list.remove() 会抛 ConcurrentModificationException，
     * 必须用 iterator.remove()（或者 list.removeIf，学完 Lambda 后可以试试）。
     */
    public static void removeNegatives(List<Integer> list) {
        // @answer
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() < 0) {
                it.remove();
            }
        }
        // @end
    }

    /**
     * 1.5 括号匹配：判断字符串中的 ()[]{} 是否正确配对，其他字符忽略。
     * <pre>
     * "{[()]}"  -> true
     * "([)]"    -> false
     * "(("      -> false
     * "a(b)c"   -> true
     * </pre>
     * 提示：用 Deque 当栈（push / pop / peek / isEmpty），Java 里不推荐用老的 Stack 类。
     */
    public static boolean isValidBrackets(String s) {
        // @answer
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '(', '[', '{' -> stack.push(c);
                case ')', ']', '}' -> {
                    if (stack.isEmpty()) {
                        return false;
                    }
                    char open = stack.pop();
                    if ((c == ')' && open != '(') || (c == ']' && open != '[') || (c == '}' && open != '{')) {
                        return false;
                    }
                }
                default -> {
                    // 其他字符忽略
                }
            }
        }
        return stack.isEmpty();
        // @end
    }
}
