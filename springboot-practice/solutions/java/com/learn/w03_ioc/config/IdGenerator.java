package com.learn.w03_ioc.config;

/**
 * 订单号生成器（已写好）。假设它来自一个第三方 jar 包 —— 你不能修改源码，没法在类上加 @Component。
 *
 * <p>这种类要放进 Spring 容器，就得在 @Configuration 类里写一个 @Bean 方法，自己 new 出来返回（见 AppConfig）。
 * RuoYi-Vue-Plus 里大量的配置类（RedisConfig、MybatisPlusConfig ...）都是这么干的。
 */
public class IdGenerator {

    private final long workerId;
    private long sequence;

    public IdGenerator(long workerId) {
        this.workerId = workerId;
    }

    public long getWorkerId() {
        return workerId;
    }

    /** 生成 id = workerId * 1_000_000 + 自增序号（从 1 开始） */
    public synchronized long nextId() {
        sequence++;
        return workerId * 1_000_000 + sequence;
    }
}
