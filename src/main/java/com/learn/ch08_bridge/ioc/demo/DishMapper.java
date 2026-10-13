package com.learn.ch08_bridge.ioc.demo;

import java.util.List;

/**
 * 数据访问层接口（已写好）。MyBatis 里 Mapper 就是接口，实现类由框架生成。
 */
public interface DishMapper {

    List<String> selectNames();
}
