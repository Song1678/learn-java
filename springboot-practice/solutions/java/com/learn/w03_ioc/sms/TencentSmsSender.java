package com.learn.w03_ioc.sms;

import org.springframework.stereotype.Component;

/**
 * 腾讯云短信（已写好）。没有写名字，所以 bean 名是默认的 tencentSmsSender。
 */
@Component
public class TencentSmsSender implements SmsSender {

    @Override
    public String name() {
        return "tencent";
    }

    @Override
    public String send(String phone, String content) {
        return "[tencent] " + phone + "：" + content;
    }
}
