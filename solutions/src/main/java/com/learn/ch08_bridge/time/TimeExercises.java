package com.learn.ch08_bridge.time;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 练习 3：日期时间 API（廖雪峰「日期与时间 - LocalDateTime / Duration / DateTimeFormatter」）
 *
 * <p>新项目一律用 java.time 包（LocalDate / LocalDateTime / Duration），不要再用老的 Date / Calendar。
 * 这些类都是不可变的：plusMinutes 之类的方法返回新对象，原对象不变（和 JS 的 Date 不一样）。
 */
public class TimeExercises {

    /** 项目里最常用的日期时间格式 */
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private TimeExercises() {
    }

    /**
     * 3.1 是否在营业时间：09:00（含）到 22:00（不含）。
     * 提示：isBefore / isAfter / equals，或者 compareTo
     */
    public static boolean isBusinessHours(LocalTime time) {
        // @answer
        return !time.isBefore(LocalTime.of(9, 0)) && time.isBefore(LocalTime.of(22, 0));
        // @end
    }

    /**
     * 3.2 格式化成 "yyyy-MM-dd HH:mm:ss"
     */
    public static String format(LocalDateTime time) {
        // @answer
        return time.format(DateTimeFormatter.ofPattern(PATTERN));
        // @end
    }

    /**
     * 3.3 把 "yyyy-MM-dd HH:mm:ss" 格式的字符串解析成 LocalDateTime
     */
    public static LocalDateTime parse(String text) {
        // @answer
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(PATTERN));
        // @end
    }

    /**
     * 3.4 生成订单号：下单时间 "yyyyMMddHHmmss" + 4 位序号（不足 4 位左边补 0）。
     * <pre>
     * 2024-05-01 12:30:05, seq=7 -> "202405011230050007"
     * </pre>
     * 提示：String.format("%04d", seq)
     */
    public static String orderNo(LocalDateTime time, int seq) {
        // @answer
        return time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + String.format("%04d", seq);
        // @end
    }

    /**
     * 3.5 预计送达时间 = 下单时间 + 配送分钟数。
     */
    public static LocalDateTime estimatedDelivery(LocalDateTime orderTime, int minutes) {
        // @answer
        return orderTime.plusMinutes(minutes);
        // @end
    }

    /**
     * 3.6 待付款订单是否超时：下单超过 15 分钟（严格大于）还没付款就算超时。
     * 苍穹外卖里有个定时任务每分钟检查一次超时订单并自动取消，用的就是这个判断。
     * 提示：Duration.between(start, end).toMinutes()，或者 orderTime.plusMinutes(15).isBefore(now)
     */
    public static boolean isPaymentTimeout(LocalDateTime orderTime, LocalDateTime now) {
        // @answer
        return Duration.between(orderTime, now).compareTo(Duration.ofMinutes(15)) > 0;
        // @end
    }

    /**
     * 3.7 两个日期之间相差多少天（end - begin，可以为负数）。
     */
    public static long daysBetween(LocalDate begin, LocalDate end) {
        // @answer
        return ChronoUnit.DAYS.between(begin, end);
        // @end
    }

    /**
     * 3.8 列出 [begin, end] 之间的每一天（包含两端）。begin 晚于 end 时抛 IllegalArgumentException。
     * 苍穹外卖的统计报表接口第一步就是这个：根据开始和结束日期生成横坐标。
     */
    public static List<LocalDate> datesBetween(LocalDate begin, LocalDate end) {
        // @answer
        if (begin.isAfter(end)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate d = begin; !d.isAfter(end); d = d.plusDays(1)) {
            dates.add(d);
        }
        return dates;
        // @end
    }
}
