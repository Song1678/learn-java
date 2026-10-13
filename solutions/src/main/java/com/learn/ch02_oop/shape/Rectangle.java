package com.learn.ch02_oop.shape;

/**
 * 矩形。名称为 "矩形"。宽高必须大于 0，否则抛出 IllegalArgumentException。
 */
public class Rectangle extends Shape {

    // @hide 声明 width 和 height 字段（protected，让子类 Square 也能用）
    protected final double width;
    protected final double height;
    // @end

    public Rectangle(double width, double height) {
        this("矩形", width, height);
    }

    /**
     * 给子类用的构造方法，可以指定名称。
     */
    protected Rectangle(String name, double width, double height) {
        super(name);
        // @answer 校验宽高，然后给字段赋值
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("宽高必须大于0");
        }
        this.width = width;
        this.height = height;
        // @end
    }

    @Override
    public double area() {
        // @answer
        return width * height;
        // @end
    }

    @Override
    public double perimeter() {
        // @answer
        return 2 * (width + height);
        // @end
    }
}
