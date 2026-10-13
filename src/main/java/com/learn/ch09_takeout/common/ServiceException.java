package com.learn.ch09_takeout.common;

/**
 * 业务异常（已写好，和第 3 章你写的一样）。
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
