package com.learn.ch09_takeout.service;

import com.learn.ch09_takeout.common.ServiceException;
import com.learn.ch09_takeout.domain.OrderDetail;
import com.learn.ch09_takeout.domain.OrderStatus;
import com.learn.ch09_takeout.domain.Orders;
import com.learn.ch09_takeout.domain.ShoppingCart;
import com.learn.ch09_takeout.mapper.OrdersMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

/**
 * 练习 9.5：订单 —— 整个项目最核心的业务
 *
 * <p>订单状态流转：
 * <pre>
 *  下单 submit        支付 pay        接单 confirm     派送 deliver           完成 complete
 *  ───────────▶ 待付款 ───────▶ 待接单 ─────────▶ 已接单 ─────────▶ 派送中 ──────────▶ 已完成
 *                 │               │
 *                 └──── 取消 cancel ┴──▶ 已取消       （超过 15 分钟未支付也会被自动取消）
 * </pre>
 *
 * <p>当前时间通过 now.get() 获取，而不是直接 LocalDateTime.now() —— 这样测试时可以“控制时间”。
 */
public class OrderService {

    /** 超时未支付的分钟数 */
    public static final int PAY_TIMEOUT_MINUTES = 15;

    private final OrdersMapper ordersMapper;
    private final ShoppingCartService cartService;
    private final Supplier<LocalDateTime> now;
    /** 订单号里的序号，每下一单 +1 */
    private int sequence = 0;

    public OrderService(OrdersMapper ordersMapper, ShoppingCartService cartService, Supplier<LocalDateTime> now) {
        this.ordersMapper = ordersMapper;
        this.cartService = cartService;
        this.now = now;
    }

    /**
     * 用户下单：
     * <ol>
     *   <li>购物车为空：ServiceException("购物车数据为空，不能下单")</li>
     *   <li>把购物车每一项转成 OrderDetail</li>
     *   <li>金额 = 购物车总价；状态 = 待付款；下单时间 = now.get()</li>
     *   <li>订单号 = 下单时间 "yyyyMMddHHmmss" + 4 位序号（第一单是 0001，之后递增）</li>
     *   <li>插入订单，清空该用户购物车，返回订单</li>
     * </ol>
     */
    public Orders submit(Long userId) {
        // @answer
        List<ShoppingCart> cart = cartService.list(userId);
        if (cart.isEmpty()) {
            throw new ServiceException("购物车数据为空，不能下单");
        }
        List<OrderDetail> details = new ArrayList<>();
        for (ShoppingCart c : cart) {
            details.add(new OrderDetail(c.getDishId(), c.getName(), c.getAmount(), c.getNumber()));
        }
        LocalDateTime time = now.get();
        Orders order = new Orders();
        order.setUserId(userId);
        order.setDetails(details);
        order.setAmount(cartService.total(userId));
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setOrderTime(time);
        order.setNumber(time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + String.format("%04d", ++sequence));
        ordersMapper.insert(order);
        cartService.clean(userId);
        return order;
        // @end
    }

    /**
     * 根据 id 查订单，不存在抛 ServiceException("订单不存在")
     */
    public Orders getById(Long orderId) {
        // @answer
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        return order;
        // @end
    }

    /**
     * 用户支付：
     * <ul>
     *   <li>订单不存在，或者不是这个用户的订单：ServiceException("订单不存在")（不要告诉别人这个订单存在）</li>
     *   <li>不是待付款状态：ServiceException("订单状态错误")</li>
     *   <li>成功：状态改为待接单，checkoutTime = now.get()</li>
     * </ul>
     */
    public void pay(Long userId, Long orderId) {
        // @answer
        Orders order = getById(orderId);
        if (!order.getUserId().equals(userId)) {
            throw new ServiceException("订单不存在");
        }
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new ServiceException("订单状态错误");
        }
        order.setStatus(OrderStatus.TO_BE_CONFIRMED);
        order.setCheckoutTime(now.get());
        ordersMapper.updateById(order);
        // @end
    }

    /**
     * 取消订单：只有 OrderStatus.canCancel() 为 true 的状态可以取消，否则 ServiceException("订单状态错误，不能取消")。
     * 成功：状态改为已取消，记录 cancelReason 和 cancelTime。
     */
    public void cancel(Long orderId, String reason) {
        // @answer
        Orders order = getById(orderId);
        if (!order.getStatus().canCancel()) {
            throw new ServiceException("订单状态错误，不能取消");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(reason);
        order.setCancelTime(now.get());
        ordersMapper.updateById(order);
        // @end
    }

    /** 商家接单：待接单 -&gt; 已接单，否则 ServiceException("订单状态错误") */
    public void confirm(Long orderId) {
        // @answer 提示：confirm / deliver / complete 逻辑几乎一样，抽一个 private 方法
        transition(orderId, OrderStatus.TO_BE_CONFIRMED, OrderStatus.CONFIRMED);
        // @end
    }

    /** 派送：已接单 -&gt; 派送中，否则 ServiceException("订单状态错误") */
    public void deliver(Long orderId) {
        // @answer
        transition(orderId, OrderStatus.CONFIRMED, OrderStatus.DELIVERY_IN_PROGRESS);
        // @end
    }

    /** 完成：派送中 -&gt; 已完成，否则 ServiceException("订单状态错误") */
    public void complete(Long orderId) {
        // @answer
        transition(orderId, OrderStatus.DELIVERY_IN_PROGRESS, OrderStatus.COMPLETED);
        // @end
    }
    // @hide

    private void transition(Long orderId, OrderStatus from, OrderStatus to) {
        Orders order = getById(orderId);
        if (order.getStatus() != from) {
            throw new ServiceException("订单状态错误");
        }
        order.setStatus(to);
        ordersMapper.updateById(order);
    }
    // @end

    /**
     * 定时任务：把下单超过 15 分钟（严格大于）还没支付的订单自动取消，取消原因为 "订单超时，自动取消"。
     * 返回取消了多少个订单。
     */
    public int cancelTimeoutOrders() {
        // @answer
        LocalDateTime deadline = now.get().minusMinutes(PAY_TIMEOUT_MINUTES);
        List<Orders> timeout = ordersMapper.selectList(o -> o.getStatus() == OrderStatus.PENDING_PAYMENT
                && o.getOrderTime().isBefore(deadline));
        for (Orders o : timeout) {
            cancel(o.getId(), "订单超时，自动取消");
        }
        return timeout.size();
        // @end
    }

    /**
     * 用户的历史订单：下单时间倒序（最新的在前），时间相同按 id 倒序。
     */
    public List<Orders> history(Long userId) {
        // @answer
        return ordersMapper.selectList(o -> o.getUserId().equals(userId)).stream()
                .sorted(Comparator.comparing(Orders::getOrderTime).thenComparing(Orders::getId).reversed())
                .toList();
        // @end
    }
}
