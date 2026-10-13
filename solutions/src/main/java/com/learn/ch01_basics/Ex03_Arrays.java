package com.learn.ch01_basics;

/**
 * 练习 3：数组与流程控制（对应「数组操作」「流程控制」）
 *
 * <p>Java 数组长度固定，没有 push/pop。需要可变长度时用 ArrayList（第 7 章）。
 */
public class Ex03_Arrays {

    /**
     * 3.1 求最大值。数组为 null 或空时抛出 IllegalArgumentException。
     * <pre>
     * max(new int[]{3, 9, -1, 4}) -> 9
     * </pre>
     */
    public static int max(int[] arr) {
        // @answer
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        int max = arr[0];
        for (int n : arr) {
            if (n > max) {
                max = n;
            }
        }
        return max;
        // @end
    }

    /**
     * 3.2 求平均值（double）。空数组返回 0。
     * <pre>
     * average(new int[]{1, 2}) -> 1.5
     * </pre>
     */
    public static double average(int[] arr) {
        // @answer
        if (arr.length == 0) {
            return 0;
        }
        long sum = 0;
        for (int n : arr) {
            sum += n;
        }
        return (double) sum / arr.length;
        // @end
    }

    /**
     * 3.3 冒泡排序：原地把数组从小到大排好（不要用 Arrays.sort）。
     */
    public static void bubbleSort(int[] arr) {
        // @answer
        for (int i = 0; i < arr.length - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                    swapped = true;
                }
            }
            // 一趟下来没有交换，说明已经有序，可以提前结束
            if (!swapped) {
                break;
            }
        }
        // @end
    }

    /**
     * 3.4 FizzBuzz：返回长度为 n 的数组，第 i 个元素（从 1 开始数）是：
     * 能被 15 整除 -> "FizzBuzz"，能被 3 整除 -> "Fizz"，能被 5 整除 -> "Buzz"，否则是数字本身。
     * <pre>
     * fizzBuzz(5) -> ["1", "2", "Fizz", "4", "Buzz"]
     * </pre>
     */
    public static String[] fizzBuzz(int n) {
        // @answer
        String[] result = new String[n];
        for (int i = 1; i <= n; i++) {
            if (i % 15 == 0) {
                result[i - 1] = "FizzBuzz";
            } else if (i % 3 == 0) {
                result[i - 1] = "Fizz";
            } else if (i % 5 == 0) {
                result[i - 1] = "Buzz";
            } else {
                result[i - 1] = String.valueOf(i);
            }
        }
        return result;
        // @end
    }

    /**
     * 3.5 成绩等级：用 Java 14+ 的 switch 表达式（箭头语法）实现。
     * 90~100 -> "A"，80~89 -> "B"，70~79 -> "C"，60~69 -> "D"，0~59 -> "E"，
     * 其他分数抛出 IllegalArgumentException。
     * 提示：score / 10 可以把分数变成 0~10 的整数再 switch。
     */
    public static String grade(int score) {
        // @answer
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("分数不合法: " + score);
        }
        return switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7 -> "C";
            case 6 -> "D";
            default -> "E";
        };
        // @end
    }

    /**
     * 3.6 矩阵转置（二维数组）。
     * <pre>
     * [[1, 2, 3],        [[1, 4],
     *  [4, 5, 6]]   ->    [2, 5],
     *                     [3, 6]]
     * </pre>
     */
    public static int[][] transpose(int[][] matrix) {
        // @answer
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
        // @end
    }
}
