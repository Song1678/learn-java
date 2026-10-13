package com.learn.ch08_bridge.lambda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 练习 1：Lambda 与函数式接口（廖雪峰「函数式编程 - Lambda基础 / 方法引用」，第 8 章之后的内容，先预习）
 *
 * <p>JS 里函数是一等公民：arr.filter(x =&gt; x &gt; 0)。
 * Java 里 Lambda 本质是“只有一个抽象方法的接口”（函数式接口）的实现：
 * <ul>
 *   <li>Predicate&lt;T&gt;：T -&gt; boolean   （test 方法）</li>
 *   <li>Function&lt;T, R&gt;：T -&gt; R        （apply 方法）</li>
 *   <li>Consumer&lt;T&gt;：T -&gt; void        （accept 方法）</li>
 *   <li>Supplier&lt;T&gt;：() -&gt; T         （get 方法）</li>
 *   <li>Comparator&lt;T&gt;：(T, T) -&gt; int  （compare 方法）</li>
 * </ul>
 * 本练习不要用 Stream，用 for 循环 + 函数式接口自己实现，理解原理。
 */
public class LambdaExercises {

    private LambdaExercises() {
    }

    /**
     * 1.1 自己实现 filter：返回满足条件的元素组成的新列表。相当于 JS 的 arr.filter(fn)
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        // @answer
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }
        return result;
        // @end
    }

    /**
     * 1.2 自己实现 map。相当于 JS 的 arr.map(fn)
     */
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        // @answer
        List<R> result = new ArrayList<>();
        for (T t : list) {
            result.add(mapper.apply(t));
        }
        return result;
        // @end
    }

    /**
     * 1.3 返回起售中、且价格不超过 maxPrice 元的菜名。
     * 要求：调用上面你写的 filter 和 map，传 Lambda 或方法引用（Dish::name）。
     */
    public static List<String> affordableOnSaleNames(List<Dish> dishes, int maxPrice) {
        // @answer
        List<Dish> ok = filter(dishes, d -> d.onSale() && d.price().compareTo(BigDecimal.valueOf(maxPrice)) <= 0);
        return map(ok, Dish::name);
        // @end
    }

    /**
     * 1.4 返回排好序的新列表（不修改原列表）：先按价格从高到低，价格相同按销量从高到低，再相同按名称升序。
     * 提示：Comparator.comparing(Dish::price).reversed()
     *          .thenComparing(Dish::sales, Comparator.reverseOrder())
     *          .thenComparing(Dish::name)
     */
    public static List<Dish> sorted(List<Dish> dishes) {
        // @answer
        List<Dish> copy = new ArrayList<>(dishes);
        copy.sort(Comparator.comparing(Dish::price).reversed()
                .thenComparing(Dish::sales, Comparator.reverseOrder())
                .thenComparing(Dish::name));
        return copy;
        // @end
    }

    /**
     * 1.5 原地删除所有停售的菜。提示：list.removeIf(Lambda)
     */
    public static void removeOffSale(List<Dish> dishes) {
        // @answer
        dishes.removeIf(d -> !d.onSale());
        // @end
    }

    /**
     * 1.6 返回一个“打折函数”：输入原价，输出打 rate 折后的价格（不做舍入）。
     * 函数也可以作为返回值 —— 和 JS 的闭包一样。
     * <pre>
     * Function&lt;BigDecimal, BigDecimal&gt; f = discount(new BigDecimal("0.8"));
     * f.apply(new BigDecimal("100")) -> 80.0
     * </pre>
     */
    public static Function<BigDecimal, BigDecimal> discount(BigDecimal rate) {
        // @answer
        return price -> price.multiply(rate);
        // @end
    }
}
