package com.learn.w03_ioc.sms;

/**
 * 短信发送（已写好）。有两个实现：阿里云、腾讯云。
 */
public interface SmsSender {

    /** 服务商名字 */
    String name();

    /** 发送短信，返回发送记录 */
    String send(String phone, String content);
}
