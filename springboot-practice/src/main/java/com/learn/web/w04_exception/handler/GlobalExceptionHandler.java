package com.learn.web.w04_exception.handler;

import com.learn.web.common.R;
import com.learn.web.common.ServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * w04 练习 4.1：全局异常处理器
 *
 * <p>对照 ch09 的 GlobalExceptionHandler.execute(...)：以前要在每个 Controller 方法里手动包一层，
 * 现在只要写一个 {@code @RestControllerAdvice} 类，Spring 会把 Controller 抛出的异常自动交给这里，
 * 按异常类型找到对应的 {@code @ExceptionHandler} 方法，它的返回值就是返回给前端的 JSON。
 *
 * <p>注意：{@code basePackages = "com.learn.web.w04_exception"} 让它只处理本章的 Controller，
 * 免得影响其他章节（真实项目里不写 basePackages，处理所有 Controller）。
 *
 * <p>要求（HTTP 状态码都是 200，错误码放在 R 的 code 里，RuoYi-Vue-Plus 也是这么做的）：
 * <table>
 *   <tr><th>异常</th><th>什么时候发生</th><th>返回</th></tr>
 *   <tr><td>ServiceException</td><td>Service 里主动抛的业务异常</td><td>R.fail(e.getCode(), e.getMessage())</td></tr>
 *   <tr><td>BindException</td><td>@Validated 校验 @RequestBody / 查询对象失败（MethodArgumentNotValidException 是它的子类）</td>
 *       <td>R.fail(400, 所有字段错误的 message 用 ", " 连接)</td></tr>
 *   <tr><td>ConstraintViolationException</td><td>直接写在方法参数上的 @Min 等校验失败</td>
 *       <td>R.fail(400, 所有错误的 message 用 ", " 连接)</td></tr>
 *   <tr><td>MissingServletRequestParameterException</td><td>必传的 @RequestParam 没传</td>
 *       <td>R.fail(400, "缺少必需的请求参数: " + 参数名)</td></tr>
 *   <tr><td>HttpMessageNotReadableException</td><td>请求体不是合法 JSON</td><td>R.fail(400, "请求体格式错误")</td></tr>
 *   <tr><td>Exception</td><td>其他所有异常（程序 bug）</td>
 *       <td>记录日志 log.error(...)，返回 R.fail(500, "系统繁忙，请稍后再试")。不要把异常信息返回给前端！</td></tr>
 * </table>
 * （RuoYi-Vue-Plus 里还处理了请求方式不对 HttpRequestMethodNotSupportedException 等异常，
 * 但那种异常发生时还没找到 Controller，加了 basePackages 的处理器管不到，所以本练习不做。）
 * <p>多个 @ExceptionHandler 都能匹配时，Spring 选“最具体”的那个，所以 Exception 兜底不会抢走前面的异常。
 */
// TODO 4.1 加上 @RestControllerAdvice(basePackages = "com.learn.web.w04_exception")，然后按上面的表格写 6 个方法
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // TODO 示例：第一个方法这样写，其余的照着写
    //   @ExceptionHandler(ServiceException.class)
    //   public R<Void> handleServiceException(ServiceException e) {
    //       return R.fail(e.getCode(), e.getMessage());
    //   }
}
