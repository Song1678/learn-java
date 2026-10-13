package com.learn.ch02_oop.shape;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch02 练习1：抽象类与多态")
class ShapeTest {

    @Test
    @DisplayName("圆形")
    void circle() {
        Circle c = new Circle(1);
        assertEquals("圆形", c.getName());
        assertEquals(Math.PI, c.area(), 1e-9);
        assertEquals(2 * Math.PI, c.perimeter(), 1e-9);
        assertEquals("圆形(面积=3.14)", c.describe());
        assertThrows(IllegalArgumentException.class, () -> new Circle(0));
    }

    @Test
    @DisplayName("矩形与正方形")
    void rectangleAndSquare() {
        Rectangle r = new Rectangle(2, 3);
        assertEquals("矩形", r.getName());
        assertEquals(6, r.area(), 1e-9);
        assertEquals(10, r.perimeter(), 1e-9);
        assertThrows(IllegalArgumentException.class, () -> new Rectangle(-1, 3));

        Square s = new Square(4);
        assertEquals("正方形", s.getName());
        assertEquals(16, s.area(), 1e-9);
        assertInstanceOf(Rectangle.class, s, "正方形应该继承矩形");
    }

    @Test
    @DisplayName("1.1~1.3 ShapeUtils 多态处理")
    void shapeUtils() {
        Shape[] shapes = {new Circle(1), new Rectangle(2, 3), new Square(1), new Circle(2)};
        assertEquals(Math.PI + 6 + 1 + 4 * Math.PI, ShapeUtils.totalArea(shapes), 1e-9);
        assertSame(shapes[3], ShapeUtils.largest(shapes));
        assertNull(ShapeUtils.largest(new Shape[0]));
        assertEquals(2, ShapeUtils.countCircles(shapes));
    }
}
