package com.learn.ch02_oop.shape;

/**
 * 圆形。名称固定为 "圆形"。
 * 面积 = π * r * r（用 Math.PI），周长 = 2 * π * r
 */
public class Circle extends Shape {

    // TODO 声明一个 private double radius 字段

    /**
     * 半径必须大于 0，否则抛出 IllegalArgumentException。
     */
    public Circle(double radius) {
        // 构造子类时必须先调用父类构造方法，而且必须写在第一行
        super("圆形");
        // TODO 校验 radius，然后给字段赋值
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public double area() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public double perimeter() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
