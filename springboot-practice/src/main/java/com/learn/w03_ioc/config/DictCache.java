package com.learn.w03_ioc.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * w03 练习 3.5：容器启动时加载字典数据（模仿 RuoYi-Vue-Plus 启动时把字典缓存起来）
 *
 * <p>{@code @PostConstruct}：对象创建好、依赖都注入完以后，Spring 自动调用这个方法，适合做初始化。
 */
@Component
public class DictCache {

    /** key：字典类型；value：字典值 → 显示文字 */
    private final Map<String, Map<Integer, String>> cache = new HashMap<>();
    private int loadCount;

    /**
     * 3.5 给这个方法加上 @PostConstruct，并在里面往 cache 放两类字典：
     * <pre>
     * "order_status"：1 待付款，2 待接单，3 已接单，4 派送中，5 已完成，6 已取消
     * "dish_status" ：1 起售，0 停售
     * </pre>
     * 每调用一次 loadCount 加 1（测试用它检查 Spring 是不是只调用了一次）。
     */
    // TODO 3.5 加上 @PostConstruct
    public void load() {
        // TODO 在这里写你的代码
    }

    /** 查字典，找不到返回 "未知"（已写好） */
    public String label(String type, int value) {
        return cache.getOrDefault(type, Map.of()).getOrDefault(value, "未知");
    }

    public int getLoadCount() {
        return loadCount;
    }
}
