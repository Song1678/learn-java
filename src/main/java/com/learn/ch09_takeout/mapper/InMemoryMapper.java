package com.learn.ch09_takeout.mapper;

import com.learn.ch09_takeout.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

/**
 * 练习 9.1：用内存实现 BaseMapper —— 所有具体 Mapper 的父类。
 *
 * <p>要求见 {@link BaseMapper} 每个方法的注释。
 * 提示：用 TreeMap&lt;Long, T&gt; 存数据，遍历时天然按 id 升序；用一个 long 字段做自增 id（从 1 开始）。
 *
 * <p>注意：真实数据库查出来的每次都是新对象，这里为了简单直接返回存进去的对象。
 */
public abstract class InMemoryMapper<T extends BaseEntity> implements BaseMapper<T> {

    // TODO 声明存储数据的 Map 和自增 id 计数器

    @Override
    public int insert(T entity) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public T selectById(Long id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public List<T> selectList() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public List<T> selectList(Predicate<T> where) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public long selectCount(Predicate<T> where) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public int updateById(T entity) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    @Override
    public int deleteById(Long id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
