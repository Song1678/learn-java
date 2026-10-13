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

    // @hide 声明存储数据的 Map 和自增 id 计数器
    private final Map<Long, T> table = new TreeMap<>();
    private long sequence = 0;
    // @end

    @Override
    public int insert(T entity) {
        // @answer
        entity.setId(++sequence);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        table.put(entity.getId(), entity);
        return 1;
        // @end
    }

    @Override
    public T selectById(Long id) {
        // @answer
        return table.get(id);
        // @end
    }

    @Override
    public List<T> selectList() {
        // @answer
        return new ArrayList<>(table.values());
        // @end
    }

    @Override
    public List<T> selectList(Predicate<T> where) {
        // @answer
        return table.values().stream().filter(where).toList();
        // @end
    }

    @Override
    public long selectCount(Predicate<T> where) {
        // @answer
        return table.values().stream().filter(where).count();
        // @end
    }

    @Override
    public int updateById(T entity) {
        // @answer
        if (entity.getId() == null || !table.containsKey(entity.getId())) {
            return 0;
        }
        entity.setUpdateTime(LocalDateTime.now());
        table.put(entity.getId(), entity);
        return 1;
        // @end
    }

    @Override
    public int deleteById(Long id) {
        // @answer
        return table.remove(id) != null ? 1 : 0;
        // @end
    }
}
