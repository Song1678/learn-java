package com.learn.ch07_collections;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 练习 6：PriorityQueue —— 商家接单队列
 *
 * <p>VIP 订单优先；同样是/不是 VIP 时，下单时间早的优先。
 */
public class OrderQueue {

    /**
     * 订单（已写好）。
     *
     * @param orderNo   订单号
     * @param vip       是否 VIP
     * @param orderTime 下单时间（毫秒时间戳，越小越早）
     */
    public record Order(String orderNo, boolean vip, long orderTime) {
    }

    // @hide 声明一个 PriorityQueue，并在创建时传入比较器
    private final Queue<Order> queue = new PriorityQueue<>((a, b) -> {
        if (a.vip() != b.vip()) {
            return a.vip() ? -1 : 1;
        }
        return Long.compare(a.orderTime(), b.orderTime());
    });
    // @end

    public void submit(Order order) {
        // @answer
        queue.offer(order);
        // @end
    }

    /** 取出下一个要处理的订单，没有则返回 null */
    public Order next() {
        // @answer
        return queue.poll();
        // @end
    }

    public int size() {
        // @answer
        return queue.size();
        // @end
    }
}
