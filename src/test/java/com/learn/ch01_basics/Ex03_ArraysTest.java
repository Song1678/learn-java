package com.learn.ch01_basics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch01 练习3：数组与流程控制")
class Ex03_ArraysTest {

    @Test
    @DisplayName("3.1 最大值")
    void max() {
        assertEquals(9, Ex03_Arrays.max(new int[]{3, 9, -1, 4}));
        assertEquals(-1, Ex03_Arrays.max(new int[]{-5, -1, -3}));
        assertThrows(IllegalArgumentException.class, () -> Ex03_Arrays.max(new int[0]));
        assertThrows(IllegalArgumentException.class, () -> Ex03_Arrays.max(null));
    }

    @Test
    @DisplayName("3.2 平均值")
    void average() {
        assertEquals(1.5, Ex03_Arrays.average(new int[]{1, 2}), 1e-9);
        assertEquals(0.0, Ex03_Arrays.average(new int[0]), 1e-9);
        assertEquals(Integer.MAX_VALUE, Ex03_Arrays.average(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE}), 1e-9,
                "求和时注意 int 溢出");
    }

    @Test
    @DisplayName("3.3 冒泡排序")
    void bubbleSort() {
        int[] arr = {5, 2, 9, 1, 5, 6};
        Ex03_Arrays.bubbleSort(arr);
        assertArrayEquals(new int[]{1, 2, 5, 5, 6, 9}, arr);

        int[] empty = {};
        Ex03_Arrays.bubbleSort(empty);
        assertArrayEquals(new int[]{}, empty);
    }

    @Test
    @DisplayName("3.4 FizzBuzz")
    void fizzBuzz() {
        assertArrayEquals(new String[]{"1", "2", "Fizz", "4", "Buzz"}, Ex03_Arrays.fizzBuzz(5));
        assertEquals("FizzBuzz", Ex03_Arrays.fizzBuzz(15)[14]);
    }

    @Test
    @DisplayName("3.5 成绩等级（switch 表达式）")
    void grade() {
        assertEquals("A", Ex03_Arrays.grade(100));
        assertEquals("A", Ex03_Arrays.grade(90));
        assertEquals("B", Ex03_Arrays.grade(89));
        assertEquals("C", Ex03_Arrays.grade(70));
        assertEquals("D", Ex03_Arrays.grade(60));
        assertEquals("E", Ex03_Arrays.grade(0));
        assertThrows(IllegalArgumentException.class, () -> Ex03_Arrays.grade(101));
        assertThrows(IllegalArgumentException.class, () -> Ex03_Arrays.grade(-1));
    }

    @Test
    @DisplayName("3.6 矩阵转置")
    void transpose() {
        int[][] m = {{1, 2, 3}, {4, 5, 6}};
        assertArrayEquals(new int[][]{{1, 4}, {2, 5}, {3, 6}}, Ex03_Arrays.transpose(m));
    }
}
