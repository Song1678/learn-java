package com.learn.ch09_takeout.service;

import com.learn.ch09_takeout.common.ServiceException;
import com.learn.ch09_takeout.domain.OrderDetail;
import com.learn.ch09_takeout.domain.OrderStatus;
import com.learn.ch09_takeout.domain.Orders;
import com.learn.ch09_takeout.dto.SalesTop;
import com.learn.ch09_takeout.mapper.OrdersMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 练习 9.6：数据统计（管理端首页的图表）
 *
 * <p>只统计【已完成】的订单。日期区间 [begin, end] 两端都包含。
 * begin 晚于 end 时抛 ServiceException("开始日期不能晚于结束日期")。
 */
public class ReportService {

    private final OrdersMapper ordersMapper;

    public ReportService(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    /**
     * 营业额统计：返回区间内【每一天】的营业额，按日期升序；没有订单的日子营业额为 0（BigDecimal.ZERO）。
     * 提示：先用 LinkedHashMap 把每一天放进去并初始化为 0，再遍历订单累加。
     */
    public Map<LocalDate, BigDecimal> turnover(LocalDate begin, LocalDate end) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 销量排名 Top N：按菜名汇总数量，数量降序，数量相同按菜名升序，最多 n 个。
     */
    public List<SalesTop> top(LocalDate begin, LocalDate end, int n) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
    // TODO 可以抽出公共的 private 方法：校验日期区间、查询区间内已完成的订单
}
