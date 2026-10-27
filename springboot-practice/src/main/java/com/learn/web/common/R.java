package com.learn.web.common;

/**
 * 统一返回结果（已写好）：所有接口都返回 {"code": 200, "msg": "操作成功", "data": ...}。
 *
 * <p>和 ch06 / ch09 你写的 R 一样。Spring 用 Jackson 把它转成 JSON 时，靠的是 getXxx() 方法，
 * 所以 getCode() 对应 JSON 里的 "code"。
 */
public class R<T> {

    public static final int SUCCESS = 200;
    public static final int FAIL = 500;

    private final int code;
    private final String msg;
    private final T data;

    private R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<>(SUCCESS, "操作成功", null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS, "操作成功", data);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(FAIL, msg, null);
    }

    public static <T> R<T> fail(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "{code=" + code + ", msg=" + msg + ", data=" + data + "}";
    }
}
