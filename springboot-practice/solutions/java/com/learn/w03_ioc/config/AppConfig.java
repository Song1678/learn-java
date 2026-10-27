package com.learn.w03_ioc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * w03 练习 3.4：用 @Bean 把第三方类放进容器
 */
@Configuration
public class AppConfig {

    /**
     * 3.4 创建一个 IdGenerator 放进容器，workerId 读取配置 takeout.worker-id（没有配置时默认 1）。
     * <p>别忘了去 application.yml 里加上 takeout.worker-id: 7
     * <p>提示：@Bean 方法的参数也可以用 @Value 注入配置值。
     */
    // @swap
    @Bean
    public IdGenerator idGenerator(@Value("${takeout.worker-id:1}") long workerId) {
        return new IdGenerator(workerId);
    }
    //> // TODO 3.4 写一个 @Bean 方法，返回 new IdGenerator(workerId)
    // @end
}
