package com.learn.ch06_generics;

/**
 * 练习 1：泛型类 —— 统一返回结果（仿照 RuoYi-Vue-Plus 的 org.dromara.common.core.domain.R）
 *
 * <p>后端所有接口都返回同样结构的 JSON：
 * <pre>
 * { "code": 200, "msg": "操作成功", "data": ... }
 * </pre>
 * data 的类型每个接口都不同（可能是用户、列表、分页……），所以用泛型 T 表示。
 * 相当于 TypeScript 里的 interface R&lt;T&gt; { code: number; msg: string; data: T }
 *
 * @param <T> data 的类型
 */
public class R<T> {

    public static final int SUCCESS = 200;
    public static final int FAIL = 500;

    private int code;
    private String msg;
    private T data;

    /** 构造方法私有，只能用下面的静态工厂方法创建 */
    private R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功，无数据：code=200, msg="操作成功", data=null
     * 提示：静态方法不能使用类上的 T，要自己声明 &lt;T&gt;：public static &lt;T&gt; R&lt;T&gt; ok()
     */
    public static <T> R<T> ok() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 成功，带数据：code=200, msg="操作成功" */
    public static <T> R<T> ok(T data) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 成功，自定义消息并带数据 */
    public static <T> R<T> ok(String msg, T data) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 失败：code=500 */
    public static <T> R<T> fail(String msg) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 失败：自定义 code */
    public static <T> R<T> fail(int code, String msg) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** code == 200 时为 true */
    public boolean isSuccess() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
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
}
