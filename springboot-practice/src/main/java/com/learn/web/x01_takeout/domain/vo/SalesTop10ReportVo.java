package com.learn.web.x01_takeout.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 销量 Top10（已写好）
 * <pre>{"nameList": "米饭,宫保鸡丁,可乐", "numberList": "18,6,5"}</pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesTop10ReportVo {

    private String nameList;
    private String numberList;
}
