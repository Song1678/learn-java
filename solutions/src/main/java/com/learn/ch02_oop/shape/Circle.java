package com.learn.ch02_oop.shape;

/**
 * 圆形。名称固定为 "圆形"。
 * 面积 = π * r * r（用 Math.PI），周长 = 2 * π * r
 */
public class Circle extends Shape {

    // @hide 声明一个 private double radius 字段
    private final double radius;
    // @end

    /**
     * 半径必须大于 0，否则抛出 IllegalArgumentException。
     */
    public Circle(double radius) {
        // 构造子类时必须先调用父类构造方法，而且必须写在第一行
        super("圆形");
        // @answer 校验 radius，然后给字段赋值
        if (radius <= 0) {
            throw new IllegalArgumentException("半径必须大于0");
        }
        this.radius = radius;
        // @end
    }

    @Override
    public double area() {
        // @answer
        return Math.PI * radius * radius;
        // @end
    }

    @Override
    public double perimeter() {
        // @answer
        return 2 * Math.PI * radius;
        // @end
    }
}
