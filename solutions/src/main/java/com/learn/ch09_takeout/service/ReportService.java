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
        // @answer
        checkRange(begin, end);
        Map<LocalDate, BigDecimal> result = new LinkedHashMap<>();
        for (LocalDate d = begin; !d.isAfter(end); d = d.plusDays(1)) {
            result.put(d, BigDecimal.ZERO);
        }
        for (Orders o : completedBetween(begin, end)) {
            result.merge(o.getOrderTime().toLocalDate(), o.getAmount(), BigDecimal::add);
        }
        return result;
        // @end
    }

    /**
     * 销量排名 Top N：按菜名汇总数量，数量降序，数量相同按菜名升序，最多 n 个。
     */
    public List<SalesTop> top(LocalDate begin, LocalDate end, int n) {
        // @answer
        checkRange(begin, end);
        Map<String, Integer> sales = completedBetween(begin, end).stream()
                .flatMap(o -> o.getDetails().stream())
                .collect(Collectors.groupingBy(OrderDetail::name, Collectors.summingInt(OrderDetail::number)));
        return sales.entrySet().stream()
                .map(e -> new SalesTop(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(SalesTop::number).reversed().thenComparing(SalesTop::name))
                .limit(n)
                .toList();
        // @end
    }
    // @hide 可以抽出公共的 private 方法：校验日期区间、查询区间内已完成的订单

    private void checkRange(LocalDate begin, LocalDate end) {
        if (begin.isAfter(end)) {
            throw new ServiceException("开始日期不能晚于结束日期");
        }
    }

    private List<Orders> completedBetween(LocalDate begin, LocalDate end) {
        return ordersMapper.selectList(o -> {
            LocalDate d = o.getOrderTime().toLocalDate();
            return o.getStatus() == OrderStatus.COMPLETED && !d.isBefore(begin) && !d.isAfter(end);
        });
    }
    // @end
}
