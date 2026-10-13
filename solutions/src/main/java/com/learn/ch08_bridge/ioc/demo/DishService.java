package com.learn.ch08_bridge.ioc.demo;

import com.learn.ch08_bridge.ioc.Autowired;
import com.learn.ch08_bridge.ioc.Component;

import java.util.List;

/**
 * 业务层（已写好）。注意：它只声明依赖 DishMapper 接口，完全不知道实现类是谁。
 */
@Component
public class DishService {

    @Autowired
    private DishMapper dishMapper;

    public List<String> list() {
        return dishMapper.selectNames();
    }

    public DishMapper getDishMapper() {
        return dishMapper;
    }
}
