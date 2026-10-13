package com.learn.ch03_exception;

/**
 * 练习 2：捕获并转换异常（对应「异常处理 - Java的异常 / 捕获异常 / 抛出异常」）
 */
public class AgeParser {

    private AgeParser() {
    }

    /**
     * 把用户输入的字符串解析成年龄。规则：
     * <ul>
     *   <li>null 或空白 -> 抛 IllegalArgumentException，消息为 "年龄不能为空"</li>
     *   <li>前后空格要去掉（" 18 " 是合法的）</li>
     *   <li>不是整数 -> 抛 IllegalArgumentException，消息为 "年龄格式不正确: xxx"（xxx 是去掉空格后的输入），
     *       并且把原来的 NumberFormatException 作为 cause 传进去</li>
     *   <li>不在 0~150 之间 -> 抛 IllegalArgumentException，消息为 "年龄超出范围: xxx"</li>
     * </ul>
     * 提示：Integer.parseInt 在格式不对时会抛 NumberFormatException。
     */
    public static int parseAge(String input) {
        // @answer
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("年龄不能为空");
        }
        String s = input.trim();
        int age;
        try {
            age = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            // 把底层异常作为 cause 传进去，排查问题时能看到完整的异常链
            throw new IllegalArgumentException("年龄格式不正确: " + s, e);
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("年龄超出范围: " + age);
        }
        return age;
        // @end
    }

    /**
     * 解析失败时返回默认值，不抛异常。
     * <pre>
     * parseAgeOrDefault("18", 0)  -> 18
     * parseAgeOrDefault("abc", 0) -> 0
     * </pre>
     */
    public static int parseAgeOrDefault(String input, int defaultValue) {
        // @answer 提示：复用 parseAge，catch 住异常
        try {
            return parseAge(input);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
        // @end
    }
}
