package com.learn.web.b04_tx;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单（已写好），对应表 orders
 */
@Data
public class TxOrder {

    private Long id;
    private String number;
    /** 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 */
    private Integer status;
    private Long userId;
    private Long addressBookId;
    private LocalDateTime orderTime;
    private BigDecimal amount;
    private String remark;
}
