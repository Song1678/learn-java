package com.learn.ch03_exception;

import java.io.IOException;
import java.util.List;

/**
 * 练习 4：try-with-resources 与 finally 的执行顺序
 */
public class ResourceDemo {

    private ResourceDemo() {
    }

    /**
     * 用 try-with-resources 依次打开资源 A 和 B（new TrackingResource("A", log)、new TrackingResource("B", log)），
     * 然后调用 B 的 work(fail)。
     * <ul>
     *   <li>如果抛出 IOException，catch 住并往 log 里加一条 "catch: " + 异常消息</li>
     *   <li>无论成功失败，最后 finally 里往 log 加一条 "finally"</li>
     * </ul>
     * 写完后看看测试里期望的日志顺序，想想为什么 close 在 catch 之前、B 比 A 先关闭。
     */
    public static void run(List<String> log, boolean fail) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
