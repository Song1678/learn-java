package com.learn.web.b03_resultmap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * b03 练习 3.4 ~ 3.5：统计查询，SQL 写在 mapper/b03/ReportMapper.xml
 * （就是 mysql-practice m07 写过的 SQL，现在把它放进 MyBatis）
 */
@Mapper
public interface ReportMapper {

    /** 3.4 [begin, end) 时间段内每天的营业额（只统计已完成的订单），没有订单的日期不返回 */
    List<TurnoverDTO> sumTurnoverByDay(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

    /** 3.5 [begin, end) 时间段内销量前 limit 的商品 */
    List<SalesTopDTO> salesTop(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end,
                               @Param("limit") int limit);
}
