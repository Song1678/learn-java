package com.learn.ch06_generics;

/**
 * 有 id 的东西（已写好）。
 *
 * @param <ID> id 的类型，可能是 Long，也可能是 String
 */
public interface Identifiable<ID> {

    ID getId();
}
