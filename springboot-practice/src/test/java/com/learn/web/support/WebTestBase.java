package com.learn.web.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * 接口测试的基类（判题工具，不用改）。
 *
 * <p>{@code @SpringBootTest} 会启动完整的 Spring 容器（和运行 main 方法一样），
 * {@code MockMvc} 可以在不真正启动 Tomcat 的情况下“模拟”发送 HTTP 请求，速度很快。
 * 所有测试类共用同一个容器，第一个测试会慢一点（在启动容器），后面就快了。
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class WebTestBase {

    @Autowired
    protected MockMvc mvc;

    /** 解析 JSON 时小数用 BigDecimal，免得 28.00 变成 28.0 */
    protected static final ObjectMapper JSON = new ObjectMapper()
            .findAndRegisterModules()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    // ------------------------------------------------------------------ 发请求

    /** 发请求，要求 HTTP 状态码是 200，返回响应体文本 */
    protected String text(MockHttpServletRequestBuilder request) {
        try {
            MockHttpServletResponse resp = mvc.perform(request).andReturn().getResponse();
            String body = resp.getContentAsString(StandardCharsets.UTF_8);
            if (resp.getStatus() != 200) {
                String hint = switch (resp.getStatus()) {
                    case 404 -> "（404：找不到这个接口。检查 Controller 类上有没有 @RestController、路径注解写对没有）";
                    case 405 -> "（405：请求方式不对。检查用的是 @GetMapping / @PostMapping / @PutMapping / @DeleteMapping 中的哪一个）";
                    case 400 -> "（400：参数转换失败或缺少参数。检查参数注解，比如 @RequestParam、@PathVariable、@RequestBody、@DateTimeFormat）";
                    case 415 -> "（415：不支持的请求体类型。接收 JSON 请求体要用 @RequestBody）";
                    default -> "";
                };
                fail("期望 HTTP 状态码 200，实际是 " + resp.getStatus() + hint
                        + "\n请求：" + describe(request) + "\n响应：" + body);
            }
            return body;
        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            // 没被全局异常处理器处理的异常会从这里抛出来
            throw new AssertionError("请求 " + describe(request) + " 时抛出了异常（没有被处理）：" + rootCause(e), e);
        }
    }

    /** 发请求，返回解析后的 JSON */
    protected JsonNode json(MockHttpServletRequestBuilder request) {
        String body = text(request);
        if (body.isEmpty()) {
            fail("响应体是空的，期望返回 JSON。请求：" + describe(request));
        }
        try {
            return JSON.readTree(body);
        } catch (Exception e) {
            return fail("响应不是合法的 JSON：" + body);
        }
    }

    protected JsonNode getJson(String url, Object... uriVars) {
        return json(get(url, uriVars));
    }

    protected JsonNode postJson(String url, Object body) {
        return json(post(url).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
    }

    protected JsonNode putJson(String url, Object body) {
        return json(put(url).contentType(MediaType.APPLICATION_JSON).content(toJson(body)));
    }

    protected JsonNode putJson(String url) {
        return json(put(url));
    }

    protected JsonNode deleteJson(String url) {
        return json(delete(url));
    }

    /** 字符串原样当作 JSON，其他对象用 Jackson 转成 JSON */
    protected static String toJson(Object body) {
        if (body instanceof String s) {
            return s;
        }
        try {
            return JSON.writeValueAsString(body);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    // ------------------------------------------------------------------ 断言

    /** 断言 R 是成功的（code=200），返回 data */
    protected static JsonNode assertOk(JsonNode r) {
        return assertOk(r, "");
    }

    protected static JsonNode assertOk(JsonNode r, String hint) {
        assertNotNull(r.get("code"), "返回值应该是 R 对象，有 code 字段：" + r);
        assertEquals(200, r.get("code").asInt(), hint + " 期望 code=200（成功），实际返回：" + r);
        return r.get("data");
    }

    /** 断言 R 是失败的，code 和 msg 都要对上 */
    protected static void assertFail(int code, String msg, JsonNode r) {
        assertFail(code, msg, r, "");
    }

    protected static void assertFail(int code, String msg, JsonNode r, String hint) {
        assertNotNull(r.get("code"), "返回值应该是 R 对象，有 code 字段：" + r);
        assertEquals(code, r.get("code").asInt(), hint + " code 不对，实际返回：" + r);
        assertEquals(msg, r.get("msg").asText(), hint + " msg 不对，实际返回：" + r);
    }

    /** 比较金额：28 和 28.00 算相等 */
    protected static void assertDecimal(String expected, JsonNode node) {
        assertNotNull(node, "金额字段不存在");
        assertEquals(0, new BigDecimal(expected).compareTo(node.decimalValue()),
                "期望 " + expected + "，实际 " + node);
    }

    protected static void assertDecimal(String expected, BigDecimal actual) {
        assertNotNull(actual, "金额是 null，期望 " + expected);
        assertEquals(0, new BigDecimal(expected).compareTo(actual), "期望 " + expected + "，实际 " + actual);
    }

    private static String describe(MockHttpServletRequestBuilder request) {
        var req = request.buildRequest(new org.springframework.mock.web.MockServletContext());
        String q = req.getQueryString();
        return req.getMethod() + " " + req.getRequestURI() + (q == null ? "" : "?" + q);
    }

    private static String rootCause(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null && t.getCause() != t) {
            t = t.getCause();
        }
        return t.getClass().getSimpleName() + ": " + t.getMessage();
    }
}
