package com.learn.web.x01_takeout.service.impl;

import com.learn.web.common.ServiceException;
import com.learn.web.x01_takeout.domain.vo.DayAmount;
import com.learn.web.x01_takeout.domain.vo.NameNumber;
import com.learn.web.x01_takeout.domain.vo.SalesTop10ReportVo;
import com.learn.web.x01_takeout.domain.vo.TurnoverReportVo;
import com.learn.web.x01_takeout.mapper.ReportMapper;
import com.learn.web.x01_takeout.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * x01 练习 x.10：数据统计（SQL 在 x.9 的 ReportMapper.xml 里）
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final ReportMapper reportMapper;

    /**
     * x.10 营业额统计：begin ~ end 每一天（包含首尾），没有订单的日期为 0。
     * <ul>
     *   <li>dateList：日期用逗号连接，"2024-05-01,2024-05-02,..."</li>
     *   <li>turnoverList：金额保留 2 位小数，用逗号连接，"150.00,185.00,99.00,0.00,..."</li>
     *   <li>begin 晚于 end：ServiceException("开始日期不能晚于结束日期")</li>
     * </ul>
     * 提示：BigDecimal 保留两位小数转字符串：amount.setScale(2, RoundingMode.HALF_UP).toPlainString()
     */
    @Override
    public TurnoverReportVo turnoverStatistics(LocalDate begin, LocalDate end) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * x.10 销量 Top10：nameList "米饭,宫保鸡丁,..."，numberList "18,6,..."。日期范围同上（同样要检查 begin &lt;= end）。
     */
    @Override
    public SalesTop10ReportVo top10(LocalDate begin, LocalDate end) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    // TODO 可以抽一个私有方法检查日期范围
}
