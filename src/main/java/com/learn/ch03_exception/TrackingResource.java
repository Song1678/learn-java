package com.learn.ch03_exception;

import java.io.IOException;
import java.util.List;

/**
 * 一个会记录日志的“资源”，模拟数据库连接、文件流这类用完必须关闭的东西。（已写好，直接用）
 *
 * <p>实现了 AutoCloseable，所以可以放进 try-with-resources 里自动关闭。
 */
public class TrackingResource implements AutoCloseable {

    private final String name;
    private final List<String> log;

    public TrackingResource(String name, List<String> log) {
        this.name = name;
        this.log = log;
        log.add("open " + name);
    }

    /**
     * 干活。fail 为 true 时抛出 IOException("xxx failed")。
     */
    public void work(boolean fail) throws IOException {
        log.add("work " + name);
        if (fail) {
            throw new IOException(name + " failed");
        }
    }

    @Override
    public void close() {
        log.add("close " + name);
    }
}
