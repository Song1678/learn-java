package com.learn.ch01_basics;

/**
 * 练习 2：字符串（对应「字符和字符串」以及「核心类 - 字符串和编码 / StringBuilder」）
 *
 * <p>和 JS 最大的区别：比较字符串内容一定要用 equals()，不要用 ==。
 * == 比较的是“是不是同一个对象”，相当于 JS 里比较两个对象引用。
 */
public class Ex02_Strings {

    /**
     * 2.1 反转字符串。null 返回 null。
     * <pre>
     * reverse("hello") -> "olleh"
     * </pre>
     * 提示：可以用 StringBuilder，也可以自己用 char[] 循环。
     */
    public static String reverse(String s) {
        // @answer
        if (s == null) {
            return null;
        }
        return new StringBuilder(s).reverse().toString();
        // @end
    }

    /**
     * 2.2 判断回文：忽略大小写，并且只看字母和数字（跳过空格、标点）。
     * <pre>
     * isPalindrome("A man, a plan, a canal: Panama") -> true
     * isPalindrome("race a car")                     -> false
     * isPalindrome("")                               -> true
     * </pre>
     * 提示：Character.isLetterOrDigit(c)、Character.toLowerCase(c)，双指针。
     */
    public static boolean isPalindrome(String s) {
        // @answer
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            char l = s.charAt(left);
            char r = s.charAt(right);
            if (!Character.isLetterOrDigit(l)) {
                left++;
            } else if (!Character.isLetterOrDigit(r)) {
                right--;
            } else {
                if (Character.toLowerCase(l) != Character.toLowerCase(r)) {
                    return false;
                }
                left++;
                right--;
            }
        }
        return true;
        // @end
    }

    /**
     * 2.3 统计元音字母（a e i o u，不区分大小写）的个数。
     * <pre>
     * countVowels("Hello World") -> 3
     * </pre>
     */
    public static int countVowels(String s) {
        // @answer
        int count = 0;
        for (char c : s.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(c) >= 0) {
                count++;
            }
        }
        return count;
        // @end
    }

    /**
     * 2.4 把下划线命名转成驼峰命名（数据库字段名 -> Java 字段名，MyBatis 里天天见）。
     * <pre>
     * toCamelCase("user_name")       -> "userName"
     * toCamelCase("create_time")     -> "createTime"
     * toCamelCase("id")              -> "id"
     * toCamelCase("order_detail_id") -> "orderDetailId"
     * </pre>
     * 提示：String.split("_")，substring，toUpperCase。
     */
    public static String toCamelCase(String snake) {
        // @answer
        String[] parts = snake.split("_");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            String p = parts[i];
            if (p.isEmpty()) {
                continue;
            }
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
        }
        return sb.toString();
        // @end
    }

    /**
     * 2.5 手机号脱敏：保留前 3 位和后 4 位，中间用 * 代替。不是 11 位数字时原样返回。
     * <pre>
     * maskPhone("13812345678") -> "138****5678"
     * maskPhone("123")         -> "123"
     * </pre>
     * 提示：String.matches("\\d{11}") 可以判断是不是 11 位数字。
     * （RuoYi-Vue-Plus 里有现成的 @Sensitive 脱敏注解，原理就是这个）
     */
    public static String maskPhone(String phone) {
        // @answer
        if (phone == null || !phone.matches("\\d{11}")) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
        // @end
    }
}
