package com.learn.web;

import com.learn.web.b03_resultmap.DishVO;
import com.learn.web.b03_resultmap.DishVoMapper;
import com.learn.web.b03_resultmap.ReportMapper;
import com.learn.web.b03_resultmap.ReportService;
import com.learn.web.b03_resultmap.SalesTopDTO;
import com.learn.web.b03_resultmap.SetmealVO;
import com.learn.web.b03_resultmap.TurnoverDTO;
import com.learn.web.common.ServiceException;
import com.learn.web.support.DbTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("b03 结果映射与多表查询")
class B03ResultMapTest extends DbTestBase {

    @Autowired
    DishVoMapper dishVoMapper;

    @Autowired
    ReportMapper reportMapper;

    @Autowired
    ReportService reportService;

    private static final LocalDateTime BEGIN = LocalDateTime.of(2024, 5, 1, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2024, 5, 9, 0, 0);

    @Test
    @DisplayName("3.1 JOIN + 别名映射")
    void withCategory() {
        List<DishVO> all = dishVoMapper.selectWithCategory(null);
        assertEquals(20, all.size());
        DishVO first = all.get(0);
        assertEquals("宫保鸡丁", first.getName(), "两张表都有 name 列，菜品名称要取 d.name");
        assertEquals("热菜", first.getCategoryName(), "分类名称要起别名 category_name");
        assertEquals("饮品", all.get(14).getCategoryName());

        List<DishVO> drinks = dishVoMapper.selectWithCategory(4L);
        assertEquals(List.of("可乐", "酸梅汤", "鲜榨橙汁", "柠檬茶"), drinks.stream().map(DishVO::getName).toList());
        assertTrue(drinks.stream().allMatch(d -> "饮品".equals(d.getCategoryName())));
    }

    @Test
    @DisplayName("3.2 resultMap + collection 一对多")
    void detail() {
        DishVO fish = dishVoMapper.selectDetail(3L);
        assertNotNull(fish);
        assertEquals("水煮鱼", fish.getName());
        assertEquals("热菜", fish.getCategoryName());
        assertEquals(0, new BigDecimal("58.00").compareTo(fish.getPrice()));
        assertEquals(2, fish.getFlavors().size(), "水煮鱼有 2 个口味，应该合并成 1 个 DishVO");
        assertEquals("辣度", fish.getFlavors().get(0).getName());
        assertEquals("微辣,中辣,重辣", fish.getFlavors().get(0).getValue());
        assertEquals("忌口", fish.getFlavors().get(1).getName());

        DishVO rice = dishVoMapper.selectDetail(11L);
        assertNotNull(rice, "没有口味的菜品也要查出来（LEFT JOIN）");
        assertEquals("米饭", rice.getName());
        assertTrue(rice.getFlavors().isEmpty(), "没有口味时 flavors 是空列表");

        assertNull(dishVoMapper.selectDetail(99L));
    }

    @Test
    @DisplayName("3.3 多对多")
    void setmeals() {
        List<SetmealVO> list = dishVoMapper.selectSetmeals();
        assertEquals(3, list.size());
        SetmealVO couple = list.get(1);
        assertEquals("双人套餐", couple.getName());
        assertEquals(List.of("水煮鱼", "鱼香肉丝", "米饭", "酸梅汤"),
                couple.getDishes().stream().map(SetmealVO.SetmealDishVO::getDishName).toList());
        assertEquals(List.of(1, 1, 2, 2), couple.getDishes().stream().map(SetmealVO.SetmealDishVO::getCopies).toList());
        assertEquals(2, list.get(2).getDishes().size());
    }

    @Test
    @DisplayName("3.4 按天统计营业额")
    void sumTurnoverByDay() {
        List<TurnoverDTO> list = reportMapper.sumTurnoverByDay(BEGIN, END);
        assertEquals(7, list.size(), "05-04 没有订单，不返回");
        assertEquals(LocalDate.of(2024, 5, 1), list.get(0).getOrderDate());
        assertEquals(0, new BigDecimal("150.00").compareTo(list.get(0).getTurnover()), "只统计已完成（status=5）的订单");
        assertEquals(LocalDate.of(2024, 5, 5), list.get(3).getOrderDate());
        assertEquals(0, new BigDecimal("122.00").compareTo(list.get(6).getTurnover()));

        assertEquals(1, reportMapper.sumTurnoverByDay(BEGIN, LocalDateTime.of(2024, 5, 2, 0, 0)).size(),
                "结束时间是开区间");
    }

    @Test
    @DisplayName("3.5 销量 Top N")
    void salesTop() {
        List<SalesTopDTO> top = reportMapper.salesTop(BEGIN, END, 3);
        assertEquals(List.of("米饭", "宫保鸡丁", "可乐"), top.stream().map(SalesTopDTO::getName).toList());
        assertEquals(List.of(18, 6, 5), top.stream().map(SalesTopDTO::getNumber).toList());
        assertEquals(10, reportMapper.salesTop(BEGIN, END, 10).size());
    }

    @Test
    @DisplayName("3.6 Java 补齐没有数据的日期")
    void turnover() {
        List<TurnoverDTO> list = reportService.turnover(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 8));
        assertEquals(8, list.size(), "05-01 ~ 05-08 一共 8 天");
        String[] expected = {"150", "185", "99", "0", "182", "170", "40", "122"};
        for (int i = 0; i < 8; i++) {
            assertEquals(LocalDate.of(2024, 5, 1 + i), list.get(i).getOrderDate());
            assertEquals(0, new BigDecimal(expected[i]).compareTo(list.get(i).getTurnover()),
                    list.get(i).getOrderDate() + " 的营业额不对");
        }
        List<TurnoverDTO> one = reportService.turnover(LocalDate.of(2024, 5, 4), LocalDate.of(2024, 5, 4));
        assertEquals(1, one.size());
        assertEquals(0, BigDecimal.ZERO.compareTo(one.get(0).getTurnover()));

        assertEquals("开始日期不能晚于结束日期", assertThrows(ServiceException.class,
                () -> reportService.turnover(LocalDate.of(2024, 5, 2), LocalDate.of(2024, 5, 1))).getMessage());
    }
}
