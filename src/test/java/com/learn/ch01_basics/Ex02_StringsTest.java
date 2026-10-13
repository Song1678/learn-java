package com.learn.ch01_basics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch01 练习2：字符串")
class Ex02_StringsTest {

    @Test
    @DisplayName("2.1 反转字符串")
    void reverse() {
        assertEquals("olleh", Ex02_Strings.reverse("hello"));
        assertEquals("", Ex02_Strings.reverse(""));
        assertEquals("界世好你", Ex02_Strings.reverse("你好世界"));
        assertNull(Ex02_Strings.reverse(null));
    }

    @Test
    @DisplayName("2.2 回文判断")
    void isPalindrome() {
        assertTrue(Ex02_Strings.isPalindrome("A man, a plan, a canal: Panama"));
        assertFalse(Ex02_Strings.isPalindrome("race a car"));
        assertTrue(Ex02_Strings.isPalindrome(""));
        assertTrue(Ex02_Strings.isPalindrome("Abba"));
        assertTrue(Ex02_Strings.isPalindrome("12321"));
    }

    @Test
    @DisplayName("2.3 统计元音")
    void countVowels() {
        assertEquals(3, Ex02_Strings.countVowels("Hello World"));
        assertEquals(5, Ex02_Strings.countVowels("AEIOU"));
        assertEquals(0, Ex02_Strings.countVowels("xyz"));
    }

    @Test
    @DisplayName("2.4 下划线转驼峰")
    void toCamelCase() {
        assertEquals("userName", Ex02_Strings.toCamelCase("user_name"));
        assertEquals("createTime", Ex02_Strings.toCamelCase("create_time"));
        assertEquals("id", Ex02_Strings.toCamelCase("id"));
        assertEquals("orderDetailId", Ex02_Strings.toCamelCase("order_detail_id"));
    }

    @Test
    @DisplayName("2.5 手机号脱敏")
    void maskPhone() {
        assertEquals("138****5678", Ex02_Strings.maskPhone("13812345678"));
        assertEquals("123", Ex02_Strings.maskPhone("123"));
        assertEquals("1381234567a", Ex02_Strings.maskPhone("1381234567a"));
        assertNull(Ex02_Strings.maskPhone(null));
    }
}
