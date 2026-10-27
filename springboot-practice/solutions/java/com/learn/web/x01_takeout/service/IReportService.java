package com.learn.web.x01_takeout.service;

import com.learn.web.x01_takeout.domain.vo.SalesTop10ReportVo;
import com.learn.web.x01_takeout.domain.vo.TurnoverReportVo;

import java.time.LocalDate;

/**
 * 数据统计（接口已写好，实现见 ReportServiceImpl）
 */
public interface IReportService {

    TurnoverReportVo turnoverStatistics(LocalDate begin, LocalDate end);

    SalesTop10ReportVo top10(LocalDate begin, LocalDate end);
}
