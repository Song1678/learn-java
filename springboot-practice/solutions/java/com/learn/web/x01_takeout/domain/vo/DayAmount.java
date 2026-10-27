package com.learn.web.x01_takeout.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 某天的营业额（已写好），ReportMapper 的查询结果
 */
@Data
public class DayAmount {

    private LocalDate orderDate;
    private BigDecimal amount;
}
