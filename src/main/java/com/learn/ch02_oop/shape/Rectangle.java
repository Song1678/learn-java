package com.learn.ch02_oop.shape;

/**
 * 矩形。名称为 "矩形"。宽高必须大于 0，否则抛出 IllegalArgumentException。
 */
public class Rectangle extends Shape {

    // TODO 声明 width 和 height 字段（protected，让子类 Square 也能用）

    public Rectangle(double width, double height) {
        this("矩形", width, height);
    }

    /**
     * 给子类用的构造方法，可以指定名称。
     */
    protected Rectangle(String name, double width, double height) {
        super(name);
        // TODO 校验宽高，然后给字段赋值
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
