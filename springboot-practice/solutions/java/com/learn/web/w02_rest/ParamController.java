package com.learn.web.w02_rest;

import com.learn.web.common.R;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * w02 练习 2.9 ~ 2.11：其他几种接收参数的方式
 */
@RestController
@RequestMapping("/w02/params")
public class ParamController {

    /**
     * 2.9 GET /w02/params/token → 读取请求头 Authorization 的值，返回 R.ok(值)；
     * 没有这个请求头时返回 R.fail(401, "未登录")。
     * <p>提示：@RequestHeader(value = "...", required = false)。登录鉴权（阶段三-B）就是从这个请求头里取 token。
     */
    // @swap
    @GetMapping("/token")
    public R<String> token(@RequestHeader(value = "Authorization", required = false) String token) {
    //> // TODO 2.9 加上映射注解和参数注解
    //> public R<String> token(String token) {
    // @end
        // @answer
        if (token == null || token.isBlank()) {
            return R.fail(401, "未登录");
        }
        return R.ok(token);
        // @end
    }

    /**
     * 2.10 GET /w02/params/dates?begin=2024-05-01&amp;end=2024-05-03 → 返回这段日期里的每一天（包含首尾）：
     * <pre>R.ok(["2024-05-01", "2024-05-02", "2024-05-03"])</pre>
     * begin 晚于 end 时返回 R.fail("开始日期不能晚于结束日期")。
     * <p>提示：用 @DateTimeFormat(pattern = "yyyy-MM-dd") 告诉 Spring 日期格式。
     * 外卖项目的“营业额统计”接口就是这样接收日期范围的。
     */
    // @swap
    @GetMapping("/dates")
    public R<List<String>> dates(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
    //> // TODO 2.10 加上映射注解和参数注解
    //> public R<List<String>> dates(LocalDate begin, LocalDate end) {
    // @end
        // @answer
        if (begin.isAfter(end)) {
            return R.fail("开始日期不能晚于结束日期");
        }
        List<String> days = new ArrayList<>();
        for (LocalDate d = begin; !d.isAfter(end); d = d.plusDays(1)) {
            days.add(d.toString());
        }
        return R.ok(days);
        // @end
    }

    /**
     * 2.11 POST /w02/params/keys，请求体是任意 JSON 对象，例如 {"b": 1, "a": {"x": 2}} →
     * 返回顶层所有 key，按字母排序：R.ok(["a", "b"])
     * <p>提示：不想专门写一个类的时候，可以用 Map&lt;String, Object&gt; 接收 JSON。
     */
    // @swap
    @PostMapping("/keys")
    public R<List<String>> keys(@RequestBody Map<String, Object> body) {
    //> // TODO 2.11 加上映射注解和参数注解
    //> public R<List<String>> keys(Map<String, Object> body) {
    // @end
        // @answer
        return R.ok(new ArrayList<>(new TreeSet<>(body.keySet())));
        // @end
    }
}
