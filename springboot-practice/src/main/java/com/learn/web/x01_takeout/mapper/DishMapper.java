package com.learn.web.x01_takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.web.x01_takeout.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
