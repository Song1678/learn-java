package com.learn.ch06_generics;

import java.util.Objects;

/**
 * 练习 2：多个类型参数
 *
 * <p>一个装两个值的不可变“二元组”，两个值的类型可以不同。
 *
 * @param <A> 第一个值的类型
 * @param <B> 第二个值的类型
 */
public class Pair<A, B> {

    // TODO 声明 first、second 两个字段

    public Pair(A first, B second) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 静态工厂方法：Pair.of("a", 1) 比 new Pair&lt;String, Integer&gt;("a", 1) 更简洁（编译器会推断类型） */
    public static <A, B> Pair<A, B> of(A first, B second) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public A getFirst() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public B getSecond() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 交换两个值，返回一个新的 Pair&lt;B, A&gt;。
     * <pre>
     * Pair.of("age", 18).swap() -> Pair(18, "age")
     * </pre>
     */
    public Pair<B, A> swap() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 两个 Pair 的 first 和 second 都相等时为 true。
     * 提示：Objects.equals(a, b) 可以安全处理 null。
     */
    @Override
    public boolean equals(Object o) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 重写 equals 必须同时重写 hashCode（第 7 章会讲为什么）。
     * 提示：Objects.hash(first, second)
     */
    @Override
    public int hashCode() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 形如 "(age, 18)" */
    @Override
    public String toString() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
