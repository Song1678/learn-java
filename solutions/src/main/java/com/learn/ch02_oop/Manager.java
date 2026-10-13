package com.learn.ch02_oop;

/**
 * 经理：继承 Employee，多一个固定奖金 bonus，月薪 = 基本工资 + 奖金。
 */
public class Manager extends Employee {

    // @hide 声明 bonus 字段
    private final double bonus;
    // @end

    public Manager(String name, double baseSalary, double bonus) {
        super(name, baseSalary);
        // @answer
        this.bonus = bonus;
        // @end
    }

    public double getBonus() {
        // @answer
        return bonus;
        // @end
    }

    @Override
    public double monthlySalary() {
        // @answer 提示：用 super.monthlySalary() 复用父类逻辑
        return super.monthlySalary() + bonus;
        // @end
    }
}
