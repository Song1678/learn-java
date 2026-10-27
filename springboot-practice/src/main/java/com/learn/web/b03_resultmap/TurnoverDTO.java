package com.learn.web.b03_resultmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 某一天的营业额（已写好）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoverDTO {

    private LocalDate orderDate;
    private BigDecimal turnover;
}
