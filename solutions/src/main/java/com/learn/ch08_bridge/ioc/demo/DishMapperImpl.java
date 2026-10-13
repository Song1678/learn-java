package com.learn.ch08_bridge.ioc.demo;

import com.learn.ch08_bridge.ioc.Component;

import java.util.List;

/**
 * 假装查了数据库（已写好）。
 */
@Component
public class DishMapperImpl implements DishMapper {

    @Override
    public List<String> selectNames() {
        return List.of("宫保鸡丁", "鱼香肉丝");
    }
}
