package com.learn.ch02_oop.shape;

/**
 * 练习 1：抽象类与多态（对应「面向对象基础 - 继承 / 多态 / 抽象类」）
 *
 * <p>Shape 是抽象类：不能 new Shape()，只能 new 它的子类。
 * 子类必须实现 area() 和 perimeter()。
 *
 * <p>这个类已经写好了，你需要完成 {@link Circle}、{@link Rectangle}、{@link Square} 和 {@link ShapeUtils}。
 */
public abstract class Shape {

    /** 图形名称，例如 "圆形"。protected：子类可以直接访问 */
    protected final String name;

    protected Shape(String name) {
        this.name = name;
    }

    /** 面积 */
    public abstract double area();

    /** 周长 */
    public abstract double perimeter();

    public String getName() {
        return name;
    }

    /**
     * 普通方法也可以调用抽象方法 —— 运行时会调用子类的实现，这就是多态。
     * 例如：圆形(面积=3.14)
     */
    public String describe() {
        return String.format("%s(面积=%.2f)", name, area());
    }
}
