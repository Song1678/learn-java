package com.learn.ch01_basics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch01 练习1：变量与数据类型")
class Ex01_TypesTest {

    @Test
    @DisplayName("1.1 摄氏度转华氏度")
    void celsiusToFahrenheit() {
        assertEquals(32.0, Ex01_Types.celsiusToFahrenheit(0), 1e-9);
        assertEquals(212.0, Ex01_Types.celsiusToFahrenheit(100), 1e-9);
        assertEquals(99.5, Ex01_Types.celsiusToFahrenheit(37.5), 1e-9);
        assertEquals(-40.0, Ex01_Types.celsiusToFahrenheit(-40), 1e-9);
    }

    @Test
    @DisplayName("1.2 百分比（整数除法陷阱）")
    void percent() {
        assertEquals(33.33, Ex01_Types.percent(1, 3), 1e-9);
        assertEquals(66.67, Ex01_Types.percent(2, 3), 1e-9);
        assertEquals(50.0, Ex01_Types.percent(1, 2), 1e-9);
        assertEquals(0.0, Ex01_Types.percent(5, 0), 1e-9);
    }

    @Test
    @DisplayName("1.3 字符转数字")
    void charToDigit() {
        assertEquals(0, Ex01_Types.charToDigit('0'));
        assertEquals(7, Ex01_Types.charToDigit('7'));
        assertEquals(9, Ex01_Types.charToDigit('9'));
        assertEquals(-1, Ex01_Types.charToDigit('a'));
        assertEquals(-1, Ex01_Types.charToDigit(' '));
    }

    @Test
    @DisplayName("1.4 int 溢出")
    void addAsLong() {
        assertEquals(2147483648L, Ex01_Types.addAsLong(Integer.MAX_VALUE, 1));
        assertEquals(-4294967296L, Ex01_Types.addAsLong(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertEquals(3L, Ex01_Types.addAsLong(1, 2));
    }

    @Test
    @DisplayName("1.5 闰年")
    void isLeapYear() {
        assertTrue(Ex01_Types.isLeapYear(2024));
        assertTrue(Ex01_Types.isLeapYear(2000));
        assertFalse(Ex01_Types.isLeapYear(1900));
        assertFalse(Ex01_Types.isLeapYear(2023));
    }
}
