package com.learn.web.b01_mybatis;

import com.learn.web.common.ServiceException;
import com.learn.web.common.StatusConstant;
import org.springframework.stereotype.Service;

/**
 * b01 练习 1.10：在 Service 里使用 Mapper
 *
 * <p>Mapper 是接口，但 Spring 容器里有 MyBatis 生成的实现类对象，所以可以直接注入。
 * 对照 ch09 的 CategoryService：结构完全一样，只是 Mapper 从内存版换成了数据库版。
 */
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 新增一个【菜品分类】（type=1），默认启用，返回新分类的 id。
     * <ul>
     *   <li>name 为 null 或空白：ServiceException("分类名称不能为空")</li>
     *   <li>已有同名分类（用 searchByName 找，再比较名称是否完全相等）：ServiceException("分类名称已存在")</li>
     * </ul>
     */
    public Long addDishCategory(String name, int sort) {
        // @answer
        if (name == null || name.isBlank()) {
            throw new ServiceException("分类名称不能为空");
        }
        boolean exists = categoryMapper.searchByName(name).stream().anyMatch(c -> c.getName().equals(name));
        if (exists) {
            throw new ServiceException("分类名称已存在");
        }
        Category c = new Category();
        c.setType(1);
        c.setName(name);
        c.setSort(sort);
        c.setStatus(StatusConstant.ENABLE);
        categoryMapper.insert(c);
        return c.getId();
        // @end
    }
}
