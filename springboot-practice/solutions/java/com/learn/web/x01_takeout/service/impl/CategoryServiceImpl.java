package com.learn.web.x01_takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.web.common.PageQuery;
import com.learn.web.common.ServiceException;
import com.learn.web.common.StatusConstant;
import com.learn.web.common.TableDataInfo;
import com.learn.web.x01_takeout.domain.Category;
import com.learn.web.x01_takeout.domain.Dish;
import com.learn.web.x01_takeout.domain.Setmeal;
import com.learn.web.x01_takeout.domain.bo.CategoryBo;
import com.learn.web.x01_takeout.mapper.CategoryMapper;
import com.learn.web.x01_takeout.mapper.DishMapper;
import com.learn.web.x01_takeout.mapper.SetmealMapper;
import com.learn.web.x01_takeout.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * x01 练习 x.1 ~ x.3：分类管理
 *
 * <p>{@code @RequiredArgsConstructor}（Lombok）：给所有 final 字段生成一个构造方法，Spring 就用它注入依赖。
 * RuoYi-Vue-Plus 的 ServiceImpl 和 Controller 全是这个写法。
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryMapper baseMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;

    /**
     * x.1 分页查询：name 模糊、type 相等（为空的不拼），按 sort 升序、id 升序
     */
    @Override
    public TableDataInfo<Category> queryPageList(CategoryBo bo, PageQuery pageQuery) {
        // @answer
        LambdaQueryWrapper<Category> lqw = Wrappers.<Category>lambdaQuery()
                .like(StringUtils.hasText(bo.getName()), Category::getName, bo.getName())
                .eq(bo.getType() != null, Category::getType, bo.getType())
                .orderByAsc(Category::getSort)
                .orderByAsc(Category::getId);
        Page<Category> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
        // @end
    }

    /**
     * x.1 某个类型下【启用】的分类，按 sort 升序、id 升序（新增菜品时的“分类”下拉框用）
     */
    @Override
    public List<Category> listEnabledByType(Integer type) {
        // @answer
        return baseMapper.selectList(Wrappers.<Category>lambdaQuery()
                .eq(Category::getType, type)
                .eq(Category::getStatus, StatusConstant.ENABLE)
                .orderByAsc(Category::getSort)
                .orderByAsc(Category::getId));
        // @end
    }

    /** x.1 不存在：ServiceException("分类不存在") */
    @Override
    public Category queryById(Long id) {
        // @answer
        Category c = baseMapper.selectById(id);
        if (c == null) {
            throw new ServiceException("分类不存在");
        }
        return c;
        // @end
    }

    /**
     * x.2 新增，默认启用，返回新 id。名称已存在：ServiceException("分类名称已存在")
     */
    @Override
    public Long insertByBo(CategoryBo bo) {
        // @answer
        checkNameUnique(bo.getName(), null);
        Category c = new Category();
        c.setType(bo.getType());
        c.setName(bo.getName());
        c.setSort(bo.getSort());
        c.setStatus(StatusConstant.ENABLE);
        baseMapper.insert(c);
        return c.getId();
        // @end
    }

    /**
     * x.2 修改 type、name、sort。不存在：ServiceException("分类不存在")；
     * 名称和【别的】分类重复：ServiceException("分类名称已存在")（名字没改不算重复）
     */
    @Override
    public void updateByBo(CategoryBo bo) {
        // @answer
        queryById(bo.getId());
        checkNameUnique(bo.getName(), bo.getId());
        Category c = new Category();
        c.setId(bo.getId());
        c.setType(bo.getType());
        c.setName(bo.getName());
        c.setSort(bo.getSort());
        baseMapper.updateById(c);
        // @end
    }

    // @hide 可以抽一个私有方法 checkNameUnique(String name, Long excludeId)
    private void checkNameUnique(String name, Long excludeId) {
        boolean exists = baseMapper.exists(Wrappers.<Category>lambdaQuery()
                .eq(Category::getName, name)
                .ne(excludeId != null, Category::getId, excludeId));
        if (exists) {
            throw new ServiceException("分类名称已存在");
        }
    }
    // @end

    /**
     * x.3 删除，按顺序检查：
     * <ol>
     *   <li>不存在：ServiceException("分类不存在")</li>
     *   <li>分类下有菜品：ServiceException("当前分类关联了菜品，不能删除")</li>
     *   <li>分类下有套餐：ServiceException("当前分类关联了套餐，不能删除")</li>
     * </ol>
     */
    @Override
    public void deleteById(Long id) {
        // @answer
        queryById(id);
        if (dishMapper.exists(Wrappers.<Dish>lambdaQuery().eq(Dish::getCategoryId, id))) {
            throw new ServiceException("当前分类关联了菜品，不能删除");
        }
        if (setmealMapper.exists(Wrappers.<Setmeal>lambdaQuery().eq(Setmeal::getCategoryId, id))) {
            throw new ServiceException("当前分类关联了套餐，不能删除");
        }
        baseMapper.deleteById(id);
        // @end
    }

    /**
     * x.3 启用 / 禁用。status 不是 0 或 1：ServiceException("状态不合法")；不存在：ServiceException("分类不存在")
     */
    @Override
    public void changeStatus(Long id, Integer status) {
        // @answer
        if (status == null || (status != 0 && status != 1)) {
            throw new ServiceException("状态不合法");
        }
        queryById(id);
        Category c = new Category();
        c.setId(id);
        c.setStatus(status);
        baseMapper.updateById(c);
        // @end
    }
}
