package com.learn.ch08_bridge.ioc.demo;

import com.learn.ch08_bridge.ioc.Autowired;
import com.learn.ch08_bridge.ioc.Component;

/**
 * 控制层（已写好）。
 */
@Component
public class DishController {

    @Autowired
    private DishService dishService;

    public String list() {
        return "菜品: " + String.join("、", dishService.list());
    }

    public DishService getDishService() {
        return dishService;
    }
}
