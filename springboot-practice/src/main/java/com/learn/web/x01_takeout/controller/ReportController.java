package com.learn.web.x01_takeout.controller;

import com.learn.web.common.R;
import com.learn.web.x01_takeout.domain.vo.SalesTop10ReportVo;
import com.learn.web.x01_takeout.domain.vo.TurnoverReportVo;
import com.learn.web.x01_takeout.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * x01 练习 x.10：数据统计接口
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/report")
public class ReportController {

    private final IReportService reportService;

    /** GET /admin/report/turnoverStatistics?begin=2024-05-01&amp;end=2024-05-08 → R&lt;TurnoverReportVo&gt; */
    // TODO 加注解
    public R<TurnoverReportVo> turnoverStatistics(LocalDate begin, LocalDate end) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** GET /admin/report/top10?begin=2024-05-01&amp;end=2024-05-08 → R&lt;SalesTop10ReportVo&gt; */
    // TODO 加注解
    public R<SalesTop10ReportVo> top10(LocalDate begin, LocalDate end) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
