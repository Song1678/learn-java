package com.learn.ch09_takeout.common;

import java.util.function.Supplier;

/**
 * 全局异常处理（已写好）。
 *
 * <p>Spring 项目里是一个 @RestControllerAdvice 类，框架会自动把 Controller 抛出的异常交给它，
 * 转换成 R.fail(...) 返回给前端。这里没有框架，所以 Controller 手动用 execute 包一下。
 */
public final class GlobalExceptionHandler {

    private GlobalExceptionHandler() {
    }

    public static <T> R<T> execute(Supplier<R<T>> action) {
        try {
            return action.get();
        } catch (ServiceException e) {
            // 业务异常：把错误信息原样告诉前端
            return R.fail(e.getCode(), e.getMessage());
        } catch (RuntimeException e) {
            // 未知异常：不要把内部细节暴露给前端，记录日志即可
            System.err.println("系统异常: " + e);
            return R.fail("系统繁忙，请稍后再试");
        }
    }
}
