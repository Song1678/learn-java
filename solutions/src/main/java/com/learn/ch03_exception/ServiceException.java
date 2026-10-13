package com.learn.ch03_exception;

/**
 * 练习 1：自定义业务异常（仿照 RuoYi-Vue-Plus 的 org.dromara.common.core.exception.ServiceException）
 *
 * <p>业务代码里遇到“不该继续”的情况（比如用户名已存在），直接 throw new ServiceException("用户名已存在")，
 * 框架里的全局异常处理器会把它变成 {"code":500,"msg":"用户名已存在"} 返回给前端。
 *
 * <p>继承 RuntimeException（非受检异常）：调用方不需要强制 try/catch 或 throws 声明。
 * 企业项目里自定义业务异常基本都是这样。
 */
public class ServiceException extends RuntimeException {

    /** 默认错误码 */
    public static final int DEFAULT_CODE = 500;

    // @hide 声明 code 字段
    private final int code;
    // @end

    /**
     * 只有消息，错误码使用 DEFAULT_CODE。
     */
    public ServiceException(String message) {
        // @answer 提示：this(...) 调用另一个构造方法
        this(message, DEFAULT_CODE);
        // @end
    }

    public ServiceException(String message, int code) {
        // @answer 提示：super(message) 把消息交给父类保存，getMessage() 才能拿到
        super(message);
        this.code = code;
        // @end
    }

    /**
     * 带上原始异常（cause），用于“异常转换”：把底层异常包装成业务异常，同时不丢失原始堆栈。
     */
    public ServiceException(String message, int code, Throwable cause) {
        // @answer
        super(message, cause);
        this.code = code;
        // @end
    }

    public int getCode() {
        // @answer
        return code;
        // @end
    }
}
