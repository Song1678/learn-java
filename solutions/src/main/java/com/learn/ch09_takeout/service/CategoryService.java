package com.learn.ch09_takeout.service;

import com.learn.ch09_takeout.common.ServiceException;
import com.learn.ch09_takeout.common.StatusConstant;
import com.learn.ch09_takeout.domain.Category;
import com.learn.ch09_takeout.mapper.CategoryMapper;
import com.learn.ch09_takeout.mapper.DishMapper;

import java.util.Comparator;
import java.util.List;

/**
 * 练习 9.2：分类管理
 *
 * <p>依赖通过构造方法传进来（构造器注入）。在 Spring 里，容器会自动帮你传 —— 就是第 8 章的 MiniContainer 做的事。
 */
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final DishMapper dishMapper;

    public CategoryService(CategoryMapper categoryMapper, DishMapper dishMapper) {
        this.categoryMapper = categoryMapper;
        this.dishMapper = dishMapper;
    }

    /**
     * 新增分类，默认启用，返回新分类的 id。
     * <ul>
     *   <li>name 为 null 或空白：ServiceException("分类名称不能为空")</li>
     *   <li>name 已存在：ServiceException("分类名称已存在")</li>
     * </ul>
     */
    public Long add(String name, int sort) {
        // @answer
        if (name == null || name.isBlank()) {
            throw new ServiceException("分类名称不能为空");
        }
        if (categoryMapper.selectCount(c -> c.getName().equals(name)) > 0) {
            throw new ServiceException("分类名称已存在");
        }
        Category category = new Category(name, sort, StatusConstant.ENABLE);
        categoryMapper.insert(category);
        return category.getId();
        // @end
    }

    /**
     * 删除分类。
     * <ul>
     *   <li>分类不存在：ServiceException("分类不存在")</li>
     *   <li>分类下还有菜品（不管起售停售）：ServiceException("当前分类关联了菜品，不能删除")</li>
     * </ul>
     */
    public void delete(Long id) {
        // @answer
        if (categoryMapper.selectById(id) == null) {
            throw new ServiceException("分类不存在");
        }
        if (dishMapper.selectCount(d -> id.equals(d.getCategoryId())) > 0) {
            throw new ServiceException("当前分类关联了菜品，不能删除");
        }
        categoryMapper.deleteById(id);
        // @end
    }

    /**
     * 查询所有启用的分类，按 sort 升序，sort 相同按 id 升序。
     */
    public List<Category> listEnabled() {
        // @answer
        return categoryMapper.selectList(c -> c.getStatus() == StatusConstant.ENABLE).stream()
                .sorted(Comparator.comparing(Category::getSort).thenComparing(Category::getId))
                .toList();
        // @end
    }
}
