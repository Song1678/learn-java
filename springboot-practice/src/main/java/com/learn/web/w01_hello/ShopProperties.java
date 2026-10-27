package com.learn.web.w01_hello;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * w01 练习 1.5：把 application.yml 里 takeout.shop 开头的配置绑定到这个对象上
 *
 * <p>先去 application.yml 末尾加上配置（见 docs/w01），然后给这个类加注解
 * {@code @ConfigurationProperties(prefix = "takeout.shop")}：把 takeout.shop.xxx 绑定到同名字段上
 * （yml 里的 open-time 会自动对应字段 openTime）。
 * <p>{@code @Component} 已经加好了：交给 Spring 管理。
 * {@code @Data} 是 Lombok 注解，编译时自动生成所有 getter / setter（绑定配置要靠 setter）。
 */
@Data
@Component
// TODO 1.5 加上 @ConfigurationProperties 注解
public class ShopProperties {

    /** 店铺名称 */
    private String name;
    /** 联系电话 */
    private String phone;
    /** 开始营业时间，例如 09:00 */
    private LocalTime openTime;
    /** 结束营业时间，例如 22:00 */
    private LocalTime closeTime;

    /**
     * 1.6 判断某个时刻是否在营业：openTime &lt;= time &lt; closeTime（开门那一刻算营业，打烊那一刻不算）。
     */
    public boolean isOpenAt(LocalTime time) {
        // TODO 提示：LocalTime 有 isBefore / isAfter 方法
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
