package com.learn.ch02_oop;

/**
 * 经理：继承 Employee，多一个固定奖金 bonus，月薪 = 基本工资 + 奖金。
 */
public class Manager extends Employee {

    // TODO 声明 bonus 字段

    public Manager(String name, double baseSalary, double bonus) {
        super(name, baseSalary);
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public double getBonus() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public double monthlySalary() {
        // TODO 提示：用 super.monthlySalary() 复用父类逻辑
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
