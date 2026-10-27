package com.learn.web.p01_mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.web.p01_mp.domain.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
