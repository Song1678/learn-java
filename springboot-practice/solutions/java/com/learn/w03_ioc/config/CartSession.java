package com.learn.w03_ioc.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * w03 练习 3.6：bean 的作用域
 *
 * <p>Spring 默认是单例（singleton）：整个容器只有一个对象，每次 getBean 拿到的都是同一个（ch08 MiniContainer 也是这样）。
 * 购物车这种“每个人一份”的对象不能是单例。给这个类加上 {@code @Scope("prototype")}，让每次 getBean 都创建新对象。
 *
 * <p>（真实项目里购物车是存数据库 / Redis 的，这里只是演示作用域。）
 */
@Component
// @hide 3.6 加上 @Scope 注解
@Scope("prototype")
// @end
public class CartSession {

    private final List<String> items = new ArrayList<>();

    public void add(String item) {
        items.add(item);
    }

    public List<String> getItems() {
        return items;
    }
}
