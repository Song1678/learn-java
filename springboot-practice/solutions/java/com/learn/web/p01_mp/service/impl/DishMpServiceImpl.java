package com.learn.web.p01_mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.web.common.StatusConstant;
import com.learn.web.p01_mp.domain.Category;
import com.learn.web.p01_mp.domain.Dish;
import com.learn.web.p01_mp.mapper.CategoryMapper;
import com.learn.web.p01_mp.mapper.DishMapper;
import com.learn.web.p01_mp.service.IDishMpService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * p01 练习 1.9 ~ 1.11：IService / ServiceImpl
 *
 * <p>继承 ServiceImpl&lt;DishMapper, Dish&gt;：父类里已经注入好了 DishMapper（叫 baseMapper），
 * 并实现了 IService 的所有方法。
 */
@Service
public class DishMpServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishMpService {

    private final CategoryMapper categoryMapper;

    public DishMpServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 1.9 提示：IService 自带 lambdaQuery()，可以链式写条件，最后 .list() 执行：
     * <pre>lambdaQuery().eq(Dish::getCategoryId, categoryId).eq(...).orderByAsc(...).list()</pre>
     * Dish::getCategoryId 是方法引用，MyBatis-Plus 用它推断出列名 category_id —— 写错字段名编译就会报错，比写字符串安全。
     * 条件构造器下一章（p02）详细练。价格相同按 id 升序。
     */
    @Override
    public List<Dish> listOnSaleByCategory(Long categoryId) {
        // @answer
        return lambdaQuery()
                .eq(Dish::getCategoryId, categoryId)
                .eq(Dish::getStatus, StatusConstant.ENABLE)
                .orderByAsc(Dish::getPrice)
                .orderByAsc(Dish::getId)
                .list();
        // @end
    }

    /** 1.10 提示：lambdaQuery().eq(...).one() */
    @Override
    public Dish getByName(String name) {
        // @answer
        return lambdaQuery().eq(Dish::getName, name).one();
        // @end
    }

    /**
     * 1.11 根据多个 id 查菜品（按 id 升序），再给每个菜品填上 categoryName。
     * <p>要求：分类只能查【一次】数据库 —— 先收集所有 categoryId，用 categoryMapper.selectByIds 一次查出来，
     * 转成 Map&lt;分类id, 分类名&gt;，再逐个 set。不要在循环里一个个 selectById（这叫 N+1 问题，菜品一多就很慢）。
     * <p>提示：listByIds(ids) 是 IService 自带的方法；查出来的顺序不一定，要自己排序。
     */
    @Override
    public List<Dish> listWithCategoryName(List<Long> dishIds) {
        // @answer
        List<Dish> dishes = listByIds(dishIds).stream()
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
        if (dishes.isEmpty()) {
            return dishes;
        }
        List<Long> categoryIds = dishes.stream().map(Dish::getCategoryId).distinct().toList();
        Map<Long, String> names = categoryMapper.selectByIds(categoryIds).stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        dishes.forEach(d -> d.setCategoryName(names.get(d.getCategoryId())));
        return dishes;
        // @end
    }
}
