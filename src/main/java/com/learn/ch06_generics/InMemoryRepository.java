package com.learn.ch06_generics;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 练习 5：有上界的泛型类 —— 一个通用的“内存数据库”
 *
 * <p>MyBatis-Plus 的 BaseMapper&lt;T&gt; 就是这种思路：写一次通用的增删改查，所有实体都能用。
 * 在第 9 章的外卖小项目里你会再写一个更完整的版本。
 *
 * @param <T>  实体类型，必须实现 Identifiable
 * @param <ID> 主键类型
 */
public class InMemoryRepository<T extends Identifiable<ID>, ID> {

    // TODO 用一个 Map<ID, T> 存数据（建议 LinkedHashMap，保持插入顺序）

    /**
     * 保存：id 已存在则覆盖。entity 或 entity.getId() 为 null 时抛 IllegalArgumentException。
     */
    public void save(T entity) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 根据 id 查询，不存在返回 null */
    public T findById(ID id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 按插入顺序返回所有数据（返回一个新 List，外部修改它不影响仓库） */
    public List<T> findAll() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** 删除，返回是否真的删掉了 */
    public boolean deleteById(ID id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public int count() {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
