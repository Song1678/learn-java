package com.learn.ch02_oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch02 练习2：封装、static、继承")
class EmployeeTest {

    @BeforeEach
    void reset() {
        Employee.resetIdCounter();
    }

    @Test
    @DisplayName("工号自动递增")
    void autoIncrementId() {
        Employee a = new Employee("张三", 8000);
        Employee b = new Employee("李四", 9000);
        Manager c = new Manager("王五", 10000, 5000);
        assertEquals(1, a.getId());
        assertEquals(2, b.getId());
        assertEquals(3, c.getId(), "子类也共用同一个计数器");
    }

    @Test
    @DisplayName("月薪与方法重写")
    void salary() {
        Employee e = new Employee("张三", 8000);
        Employee m = new Manager("王五", 10000, 5000); // 父类引用指向子类对象
        assertEquals("张三", e.getName());
        assertEquals(8000, e.getBaseSalary(), 1e-9);
        assertEquals(8000, e.monthlySalary(), 1e-9);
        assertEquals(15000, m.monthlySalary(), 1e-9);
        assertEquals(5000, ((Manager) m).getBonus(), 1e-9);
    }

    @Test
    @DisplayName("toString 使用多态")
    void testToString() {
        Employee e = new Employee("张三", 8000);
        Employee m = new Manager("王五", 10000, 5000);
        assertEquals("#1 张三 月薪:8000.0", e.toString());
        assertEquals("#2 王五 月薪:15000.0", m.toString());
    }
}
