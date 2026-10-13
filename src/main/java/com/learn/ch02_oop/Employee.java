package com.learn.ch02_oop;

/**
 * 练习 2：封装、static 字段、继承与方法重写（对应「方法 / 构造方法 / 继承 / 静态字段和静态方法」）
 *
 * <p>员工类。要求：
 * <ul>
 *   <li>字段全部 private，通过 getter 访问（JavaBean 风格）</li>
 *   <li>每创建一个员工，自动分配一个递增的工号 id：1, 2, 3 ...（用 static 计数器）</li>
 *   <li>月薪 = 基本工资 baseSalary</li>
 * </ul>
 */
public class Employee {

    // TODO 声明 static 计数器、id、name、baseSalary 字段

    public Employee(String name, double baseSalary) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 重置工号计数器（测试用） */
    public static void resetIdCounter() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public int getId() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public String getName() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public double getBaseSalary() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 月薪：普通员工就是基本工资 */
    public double monthlySalary() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 返回形如 "#1 张三 月薪:8000.0" 的字符串。
     * 注意：子类重写了 monthlySalary() 后，这里应该自动显示子类的月薪。
     */
    @Override
    public String toString() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
