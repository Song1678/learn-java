package com.learn.web.common;

/**
 * 业务异常（已写好，和 ch03 / ch09 的一样）。
 *
 * <p>Service 里遇到“业务上不允许”的情况就 throw new ServiceException("xxx")，
 * 由全局异常处理器（w04 会写）统一转换成 R.fail(code, msg) 返回给前端。
 */
public class ServiceException extends RuntimeException {

    private final int code;

    public ServiceException(String message) {
        this(message, R.FAIL);
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
