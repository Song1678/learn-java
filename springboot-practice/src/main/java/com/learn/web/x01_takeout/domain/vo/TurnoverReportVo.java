package com.learn.web.x01_takeout.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 营业额统计（已写好）。和苍穹外卖一样，前端图表要的是逗号分隔的字符串：
 * <pre>{"dateList": "2024-05-01,2024-05-02", "turnoverList": "150.00,185.00"}</pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnoverReportVo {

    private String dateList;
    private String turnoverList;
}
