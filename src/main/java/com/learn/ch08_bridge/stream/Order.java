package com.learn.ch08_bridge.stream;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单（已写好）。
 *
 * @param id        订单 id
 * @param userName  下单用户
 * @param status    状态
 * @param amount    实付金额
 * @param orderTime 下单时间
 * @param items     订单明细
 */
public record Order(Long id, String userName, Status status, BigDecimal amount,
                    LocalDateTime orderTime, List<Item> items) {

    public enum Status { PENDING_PAYMENT, COMPLETED, CANCELLED }

    /**
     * 订单明细
     *
     * @param dishName 菜名
     * @param quantity 数量
     */
    public record Item(String dishName, int quantity) {
    }
}
