package com.learn.ch08_bridge.stream;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 练习 2：Stream API（廖雪峰「函数式编程 - 使用Stream」）
 *
 * <p>Stream 类似 JS 数组的链式调用：orders.filter(...).map(...).reduce(...)。
 * 企业代码里到处都是 Stream，必须熟练。每个方法尽量用“一条 Stream 链”完成，不要写 for 循环。
 *
 * <p>常用：filter / map / flatMap / distinct / sorted / limit / reduce / collect /
 * Collectors.toList / toSet / groupingBy / counting / summingInt / joining，以及 Optional。
 */
public class StreamExercises {

    private StreamExercises() {
    }

    /**
     * 2.1 已完成（COMPLETED）订单的营业额之和。没有则返回 BigDecimal.ZERO。
     * 提示：.map(Order::amount).reduce(BigDecimal.ZERO, BigDecimal::add)
     */
    public static BigDecimal completedRevenue(List<Order> orders) {
        // @answer
        return orders.stream()
                .filter(o -> o.status() == Order.Status.COMPLETED)
                .map(Order::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // @end
    }

    /**
     * 2.2 所有下过单的用户名：去重、按字母排序。
     */
    public static List<String> userNames(List<Order> orders) {
        // @answer
        return orders.stream().map(Order::userName).distinct().sorted().toList();
        // @end
    }

    /**
     * 2.3 每种状态各有多少个订单。
     * 提示：Collectors.groupingBy(Order::status, Collectors.counting())
     */
    public static Map<Order.Status, Long> countByStatus(List<Order> orders) {
        // @answer
        return orders.stream().collect(Collectors.groupingBy(Order::status, Collectors.counting()));
        // @end
    }

    /**
     * 2.4 已完成订单里销量最高的前 n 道菜（按总数量降序，数量相同按菜名升序）。
     * 提示：flatMap(o -&gt; o.items().stream()) 把“订单流”摊平成“明细流”，
     * 再 groupingBy(Item::dishName, summingInt(Item::quantity)) 得到 Map&lt;菜名, 总数量&gt;，
     * 然后对 entrySet().stream() 排序、limit、map 出 key。
     */
    public static List<String> topDishes(List<Order> orders, int n) {
        // @answer
        Map<String, Integer> sales = orders.stream()
                .filter(o -> o.status() == Order.Status.COMPLETED)
                .flatMap(o -> o.items().stream())
                .collect(Collectors.groupingBy(Order.Item::dishName, Collectors.summingInt(Order.Item::quantity)));
        return sales.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
        // @end
    }

    /**
     * 2.5 金额最大的订单。列表为空时返回 Optional.empty()。
     * Optional 是“可能没有值”的容器，用来代替返回 null（类似 JS 的可选链思想）。
     */
    public static Optional<Order> maxAmountOrder(List<Order> orders) {
        // @answer
        return orders.stream().max(Comparator.comparing(Order::amount));
        // @end
    }

    /**
     * 2.6 查找某个用户最早的一笔订单的 id；找不到返回 -1。
     * 提示：filter + min(...) 得到 Optional&lt;Order&gt;，再 .map(Order::id).orElse(-1L)
     */
    public static long firstOrderIdOf(List<Order> orders, String userName) {
        // @answer
        return orders.stream()
                .filter(o -> o.userName().equals(userName))
                .min(Comparator.comparing(Order::orderTime))
                .map(Order::id)
                .orElse(-1L);
        // @end
    }

    /**
     * 2.7 按日期统计已完成订单的营业额，日期从早到晚排序 —— 苍穹外卖“营业额统计”报表的核心逻辑。
     * 提示：groupingBy(o -&gt; o.orderTime().toLocalDate(), TreeMap::new,
     *        Collectors.reducing(BigDecimal.ZERO, Order::amount, BigDecimal::add))
     */
    public static Map<LocalDate, BigDecimal> revenueByDate(List<Order> orders) {
        // @answer
        return orders.stream()
                .filter(o -> o.status() == Order.Status.COMPLETED)
                .collect(Collectors.groupingBy(o -> o.orderTime().toLocalDate(), TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Order::amount, BigDecimal::add)));
        // @end
    }

    /**
     * 2.8 把订单 id 拼成字符串，例如 "1,2,3"（按原顺序）。
     * 提示：map(o -&gt; String.valueOf(o.id())).collect(Collectors.joining(","))
     */
    public static String joinIds(List<Order> orders) {
        // @answer
        return orders.stream().map(o -> String.valueOf(o.id())).collect(Collectors.joining(","));
        // @end
    }
}
