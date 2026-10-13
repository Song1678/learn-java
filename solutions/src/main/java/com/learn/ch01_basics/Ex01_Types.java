package com.learn.ch01_basics;

/**
 * 练习 1：变量与数据类型（对应廖雪峰「Java程序基础 - 变量和数据类型 / 整数运算 / 浮点数运算」）
 *
 * <p>JS 里只有一个 number 类型，Java 里 int / long / double 是不同的类型，
 * 而且整数相除会直接丢掉小数部分 —— 这是从 JS 转过来最容易踩的坑。
 */
public class Ex01_Types {

    /**
     * 1.1 摄氏度转华氏度：F = C * 9 / 5 + 32
     * <pre>
     * celsiusToFahrenheit(0)    -> 32.0
     * celsiusToFahrenheit(37.5) -> 99.5
     * </pre>
     */
    public static double celsiusToFahrenheit(double celsius) {
        // @answer
        // 注意写成 9.0 / 5 或者 celsius * 9 / 5，不能写成 9 / 5 * celsius（9 / 5 == 1）
        return celsius * 9 / 5 + 32;
        // @end
    }

    /**
     * 1.2 百分比：计算 part 占 total 的百分比，保留两位小数（四舍五入）。
     * total 为 0 时返回 0。
     * <pre>
     * percent(1, 3)  -> 33.33
     * percent(2, 3)  -> 66.67
     * percent(5, 0)  -> 0.0
     * </pre>
     * 提示：int / int 结果还是 int；Math.round(x * 100) / 100.0 可以保留两位小数。
     */
    public static double percent(int part, int total) {
        // @answer 提示：先把 part 转成 double 再除
        if (total == 0) {
            return 0;
        }
        double p = (double) part / total * 100;
        return Math.round(p * 100) / 100.0;
        // @end
    }

    /**
     * 1.3 把字符 '0'~'9' 转成对应的整数 0~9，其他字符返回 -1。
     * <pre>
     * charToDigit('7') -> 7
     * charToDigit('a') -> -1
     * </pre>
     * 提示：char 本质是一个整数（Unicode 编码），'7' - '0' == 7
     */
    public static int charToDigit(char c) {
        // @answer
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        return -1;
        // @end
    }

    /**
     * 1.4 安全地把两个 int 相加：结果超出 int 范围时不要溢出，所以返回 long。
     * <pre>
     * addAsLong(Integer.MAX_VALUE, 1) -> 2147483648L
     * </pre>
     * 思考：如果直接 return a + b 会得到什么？（试试看，会变成负数）
     */
    public static long addAsLong(int a, int b) {
        // @answer
        // 先把其中一个转成 long，整个表达式就会按 long 计算
        return (long) a + b;
        // @end
    }

    /**
     * 1.5 判断闰年：能被 4 整除但不能被 100 整除，或者能被 400 整除。
     * <pre>
     * isLeapYear(2024) -> true
     * isLeapYear(1900) -> false
     * isLeapYear(2000) -> true
     * </pre>
     */
    public static boolean isLeapYear(int year) {
        // @answer
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
        // @end
    }
}
