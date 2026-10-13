package com.learn.ch02_oop.shape;

/**
 * 处理一组图形的工具类。方法参数是 Shape[]，实际传入的可以是任意子类 —— 多态。
 */
public class ShapeUtils {

    /** 工具类不需要实例化，构造方法设成 private */
    private ShapeUtils() {
    }

    /**
     * 1.1 计算所有图形的面积之和。
     */
    public static double totalArea(Shape[] shapes) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.2 找出面积最大的图形；数组为空返回 null。
     */
    public static Shape largest(Shape[] shapes) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 1.3 统计数组里有多少个圆形。
     * 提示：instanceof
     */
    public static int countCircles(Shape[] shapes) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
