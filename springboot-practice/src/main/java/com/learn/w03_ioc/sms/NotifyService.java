package com.learn.w03_ioc.sms;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * w03 练习 3.1：短信通知，主通道发送失败时自动切换到备用通道
 *
 * <p>构造方法有两个 SmsSender 参数：
 * <ul>
 *   <li>primary：不加注解，做完 3.1 后会注入 @Primary 的那个（阿里云）</li>
 *   <li>backup：要用 {@code @Qualifier("bean名字")} 指定注入腾讯云</li>
 * </ul>
 */
@Service
public class NotifyService {

    private final SmsSender primary;
    private final SmsSender backup;

    // TODO 3.1 给 backup 参数加上 @Qualifier，指定注入腾讯云
    public NotifyService(SmsSender primary, SmsSender backup) {
        this.primary = primary;
        this.backup = backup;
    }

    public String primaryName() {
        return primary.name();
    }

    public String backupName() {
        return backup.name();
    }

    /**
     * 先用主通道发送；主通道抛出任何 RuntimeException，就改用备用通道发送。返回实际发送的记录。
     * <pre>
     * send("13800000001", "您的订单已送达") → "[aliyun] 13800000001：您的订单已送达"
     * send("17000000001", "您的订单已送达") → "[tencent] 17000000001：您的订单已送达"
     * </pre>
     */
    public String send(String phone, String content) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
