package com.learn.ch06_generics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 练习 4：泛型方法、extends / super 通配符（对应「泛型 - extends通配符 / super通配符」）
 *
 * <p>记住 PECS 原则：Producer Extends, Consumer Super。
 * 只从集合里“读”（它生产数据给你）用 ? extends T；只往集合里“写”（它消费你的数据）用 ? super T。
 */
public class GenericUtils {

    private GenericUtils() {
    }

    /**
     * 4.1 求最大值，列表为空返回 null。
     * T 必须是可比较的，所以有上界 T extends Comparable&lt;? super T&gt;。
     * <pre>
     * max(List.of(3, 9, 1))         -> 9
     * max(List.of("b", "c", "a"))  -> "c"
     * </pre>
     */
    public static <T extends Comparable<? super T>> T max(List<T> list) {
        // @answer
        T max = null;
        for (T t : list) {
            if (max == null || t.compareTo(max) > 0) {
                max = t;
            }
        }
        return max;
        // @end
    }

    /**
     * 4.2 对任意数字列表求和：List&lt;Integer&gt;、List&lt;Double&gt;、List&lt;BigDecimal&gt; 都能传进来。
     * 提示：Number 有 doubleValue() 方法。
     * 思考：为什么参数不能写成 List&lt;Number&gt;？（List&lt;Integer&gt; 不是 List&lt;Number&gt; 的子类型）
     */
    public static double sum(List<? extends Number> numbers) {
        // @answer
        double total = 0;
        for (Number n : numbers) {
            total += n.doubleValue();
        }
        return total;
        // @end
    }

    /**
     * 4.3 把 src 里的所有元素追加到 dest 末尾。
     * 例如可以把 List&lt;Integer&gt; 复制进 List&lt;Number&gt; 或 List&lt;Object&gt;。
     */
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        // @answer
        for (T t : src) {
            dest.add(t);
        }
        // @end
    }

    /**
     * 4.4 反转 Map：key 变 value，value 变 key。如果有重复的 value，后出现的覆盖先出现的。
     * <pre>
     * {1="待付款", 2="待接单"} -> {"待付款"=1, "待接单"=2}
     * </pre>
     */
    public static <K, V> Map<V, K> invert(Map<K, V> map) {
        // @answer
        Map<V, K> result = new HashMap<>();
        for (Map.Entry<K, V> e : map.entrySet()) {
            result.put(e.getValue(), e.getKey());
        }
        return result;
        // @end
    }
}
