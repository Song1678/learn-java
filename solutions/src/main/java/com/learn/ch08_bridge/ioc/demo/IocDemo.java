package com.learn.ch08_bridge.ioc.demo;

import com.learn.ch08_bridge.ioc.MiniContainer;

/**
 * 运行看看效果（完成 MiniContainer 之后）：
 * mvn -q compile exec:java -Dexec.mainClass=com.learn.ch08_bridge.ioc.demo.IocDemo
 */
public class IocDemo {

    public static void main(String[] args) {
        MiniContainer container = new MiniContainer(DishController.class, DishService.class, DishMapperImpl.class);
        DishController controller = container.getBean(DishController.class);
        System.out.println(controller.list());
    }
}
