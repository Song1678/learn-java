package com.learn.ch09_takeout.common;

/**
 * 统一返回结果（已写好，和第 6 章你写的一样）。
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

    public boolean isSuccess() {
        return code == SUCCESS;
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
