package com.learn.web.p01_mp.service;

import com.learn.web.common.StatusConstant;
import com.learn.web.p01_mp.domain.Category;
import com.learn.web.p01_mp.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * p01 练习 1.3 ~ 1.8：BaseMapper 的增删改查。每个方法基本都是一两行，熟悉一下 API。
 */
@Service
public class CategoryMpService {

    private final CategoryMapper categoryMapper;

    public CategoryMpService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 1.3 新增分类：status=1，createTime 和 updateTime 都是现在，返回新 id。
     * <p>insert 之后 MyBatis-Plus 会自动把自增 id 回填到对象里（前提是 1.1 做对了）。
     */
    public Long add(String name, int type, int sort) {
        // @answer
        Category c = new Category();
        c.setName(name);
        c.setType(type);
        c.setSort(sort);
        c.setStatus(StatusConstant.ENABLE);
        c.setCreateTime(LocalDateTime.now());
        c.setUpdateTime(LocalDateTime.now());
        categoryMapper.insert(c);
        return c.getId();
        // @end
    }

    /** 1.4 根据 id 查询，不存在返回 null */
    public Category getById(Long id) {
        // @answer
        return categoryMapper.selectById(id);
        // @end
    }

    /** 1.5 根据多个 id 查询（selectByIds） */
    public List<Category> listByIds(List<Long> ids) {
        // @answer
        return categoryMapper.selectByIds(ids);
        // @end
    }

    /**
     * 1.6 只修改名称，返回是否修改成功。
     * <p>new 一个 Category，只 set id 和 name，再 updateById —— 值为 null 的字段 MyBatis-Plus 不会更新，
     * 所以 sort、status 等不会被改成 null。（对照 b02 你手写的 updateSelective）
     */
    public boolean rename(Long id, String newName) {
        // @answer
        Category c = new Category();
        c.setId(id);
        c.setName(newName);
        return categoryMapper.updateById(c) == 1;
        // @end
    }

    /** 1.7 删除，返回是否删除成功 */
    public boolean remove(Long id) {
        // @answer
        return categoryMapper.deleteById(id) == 1;
        // @end
    }

    /** 1.8 统计分类总数（selectCount 的参数传 null 表示没有条件） */
    public long countAll() {
        // @answer
        return categoryMapper.selectCount(null);
        // @end
    }
}
