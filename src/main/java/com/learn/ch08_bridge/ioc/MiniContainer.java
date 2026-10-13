package com.learn.ch08_bridge.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 练习 4：手写一个迷你 IoC 容器 —— 理解 Spring 最核心的功能
 *
 * <p>以前：DishController 里自己 new DishService()，DishService 里自己 new DishMapperImpl()，层层耦合。
 * <br>现在：每个类只声明“我需要一个 DishService”（@Autowired 字段），由容器负责创建对象并“注入”进去。
 * 这就是控制反转（IoC）/ 依赖注入（DI）。
 *
 * <p>你会用到：反射创建对象（第4章）、读取注解（第5章）、泛型方法（第6章）、Map 缓存（第7章）。
 *
 * <p>要求：
 * <ol>
 *   <li>构造方法传入要管理的类，没有 @Component 注解的类抛 IllegalArgumentException</li>
 *   <li>getBean(type)：在注册的类里找到“是 type 本身或者是 type 的实现类/子类”的那一个
 *       （提示：type.isAssignableFrom(clazz)），所以 getBean(接口.class) 能拿到实现类对象</li>
 *   <li>找不到抛 IllegalArgumentException("找不到 Bean: " + type.getName())；
 *       找到多个抛 IllegalStateException("找到多个 Bean: " + type.getName())</li>
 *   <li>单例：同一个类只创建一次，以后都返回同一个对象</li>
 *   <li>创建对象后，给所有带 @Autowired 的字段注入 getBean(字段类型)</li>
 *   <li>进阶：A 依赖 B、B 又依赖 A（循环依赖）也要能正常工作。
 *       提示：对象 new 出来后先放进缓存，再做字段注入</li>
 * </ol>
 */
public class MiniContainer {

    // TODO 声明需要的字段：注册的类列表、单例缓存 Map<Class<?>, Object>

    public MiniContainer(Class<?>... componentClasses) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public <T> T getBean(Class<T> type) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
    // TODO 可以抽一个 private 方法：从缓存取，没有就创建 + 放入缓存 + 注入字段
}
