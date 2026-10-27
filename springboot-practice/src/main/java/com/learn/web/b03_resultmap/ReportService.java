package com.learn.web.b03_resultmap;

import com.learn.web.common.ServiceException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * b03 练习 3.6：SQL + Java 配合完成“营业额统计”
 */
@Service
public class ReportService {

    private final ReportMapper reportMapper;

    public ReportService(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    /**
     * 统计 begin ~ end 每一天的营业额（包含首尾两天），没有订单的日期营业额为 0，按日期升序。
     * begin 晚于 end：ServiceException("开始日期不能晚于结束日期")
     * <p>做法：调用一次 reportMapper.sumTurnoverByDay（时间范围是 [begin 的 0 点, end 的第二天 0 点)），
     * 把结果转成 Map&lt;日期, 营业额&gt;，再从 begin 循环到 end，Map 里没有的日期补 0。
     * <p>对照 mysql-practice m07 第 1 题：那里是用 SQL 补 0（递归 CTE），这里用 Java 补 0，实际项目两种都有。
     */
    public List<TurnoverDTO> turnover(LocalDate begin, LocalDate end) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
