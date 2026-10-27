package com.learn.w03_ioc.sms;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 阿里云短信。
 *
 * <p>3.1 容器里有两个 SmsSender，注入 SmsSender 时 Spring 不知道选哪个，会报错：
 * {@code expected single matching bean but found 2: aliyunSmsSender,tencentSmsSender}。
 * 给这个类加上 {@code @Primary}，让它成为“首选”的那个。
 */
@Component
// TODO 3.1 加上 @Primary
public class AliyunSmsSender implements SmsSender {

    @Override
    public String name() {
        return "aliyun";
    }

    @Override
    public String send(String phone, String content) {
        if (phone.startsWith("170")) {
            throw new IllegalStateException("阿里云：不支持虚拟号段 " + phone);
        }
        return "[aliyun] " + phone + "：" + content;
    }
}
