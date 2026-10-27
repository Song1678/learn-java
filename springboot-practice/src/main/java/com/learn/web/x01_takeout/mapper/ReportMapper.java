package com.learn.web.x01_takeout.mapper;

import com.learn.web.x01_takeout.domain.vo.DayAmount;
import com.learn.web.x01_takeout.domain.vo.NameNumber;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 统计查询（接口已写好），SQL 写在 resources/mapper/x01/ReportMapper.xml（练习 x.9）
 */
@Mapper
public interface ReportMapper {

    /** [begin, end) 内已完成订单每天的营业额，返回列 order_date、amount，按日期升序；没有订单的日期不返回 */
    List<DayAmount> sumAmountByDay(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);

    /** [begin, end) 内已完成订单的商品销量前 10，返回列 name、number，按销量降序、名称升序 */
    List<NameNumber> salesTop10(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end);
}
