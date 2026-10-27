package com.learn.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("x01 综合项目：数据统计接口")
class X01ReportApiTest extends DbTestBase {

    @Test
    @DisplayName("x.9 + x.10 营业额统计")
    void turnover() {
        JsonNode r = assertOk(getJson("/admin/report/turnoverStatistics?begin=2024-05-01&end=2024-05-08"));
        assertEquals("2024-05-01,2024-05-02,2024-05-03,2024-05-04,2024-05-05,2024-05-06,2024-05-07,2024-05-08",
                r.get("dateList").asText());
        assertEquals("150.00,185.00,99.00,0.00,182.00,170.00,40.00,122.00", r.get("turnoverList").asText());

        JsonNode one = assertOk(getJson("/admin/report/turnoverStatistics?begin=2024-04-28&end=2024-04-28"));
        assertEquals("2024-04-28", one.get("dateList").asText());
        assertEquals("30.00", one.get("turnoverList").asText());

        assertFail(500, "开始日期不能晚于结束日期",
                getJson("/admin/report/turnoverStatistics?begin=2024-05-08&end=2024-05-01"));
        assertFail(400, "缺少必需的请求参数: end", getJson("/admin/report/turnoverStatistics?begin=2024-05-08"));
    }

    @Test
    @DisplayName("x.9 + x.10 销量 Top10")
    void top10() {
        JsonNode r = assertOk(getJson("/admin/report/top10?begin=2024-05-01&end=2024-05-08"));
        String[] names = r.get("nameList").asText().split(",");
        String[] numbers = r.get("numberList").asText().split(",");
        assertEquals(10, names.length);
        assertEquals("米饭", names[0]);
        assertEquals("宫保鸡丁", names[1]);
        assertEquals("可乐", names[2]);
        assertEquals("18,6,5,3,3,3,3,2,2,2", r.get("numberList").asText());
        assertEquals(10, numbers.length);

        JsonNode day = assertOk(getJson("/admin/report/top10?begin=2024-05-02&end=2024-05-02"));
        assertEquals("米饭,双人套餐,拍黄瓜,水煮鱼,蛋炒饭,酸梅汤", day.get("nameList").asText(),
                "不足 10 个就有几个返回几个；销量相同按名称升序（utf8mb4_0900_ai_ci 下汉字按 Unicode 编码排序，不是拼音）");
        assertEquals("2,1,1,1,1,1", day.get("numberList").asText());
    }
}
