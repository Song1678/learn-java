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

    // TODO 声明一个 PriorityQueue，并在创建时传入比较器

    public void submit(Order order) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 取出下一个要处理的订单，没有则返回 null */
    public Order next() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public int size() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
