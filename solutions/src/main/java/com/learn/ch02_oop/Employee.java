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

    // @hide 声明 static 计数器、id、name、baseSalary 字段
    private static int nextId = 1;

    private final int id;
    private final String name;
    private final double baseSalary;
    // @end

    public Employee(String name, double baseSalary) {
        // @answer
        this.id = nextId++;
        this.name = name;
        this.baseSalary = baseSalary;
        // @end
    }

    /** 重置工号计数器（测试用） */
    public static void resetIdCounter() {
        // @answer
        nextId = 1;
        // @end
    }

    public int getId() {
        // @answer
        return id;
        // @end
    }

    public String getName() {
        // @answer
        return name;
        // @end
    }

    public double getBaseSalary() {
        // @answer
        return baseSalary;
        // @end
    }

    /** 月薪：普通员工就是基本工资 */
    public double monthlySalary() {
        // @answer
        return baseSalary;
        // @end
    }

    /**
     * 返回形如 "#1 张三 月薪:8000.0" 的字符串。
     * 注意：子类重写了 monthlySalary() 后，这里应该自动显示子类的月薪。
     */
    @Override
    public String toString() {
        // @answer
        return "#" + id + " " + name + " 月薪:" + monthlySalary();
        // @end
    }
}
