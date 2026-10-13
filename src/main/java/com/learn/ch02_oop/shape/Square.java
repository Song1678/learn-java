package com.learn.ch02_oop.shape;

/**
 * 正方形：继承 Rectangle，名称为 "正方形"。
 * 思考：正方形“是一个”矩形，所以用继承；它不需要重写 area()。
 */
public class Square extends Rectangle {

    public Square(double side) {
        // 调用父类 protected Rectangle(String, double, double) 构造方法即可，正方形就是宽高相等的矩形
        super("正方形", side, side);
    }
}
