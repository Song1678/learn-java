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
import com.learn.web.x01_takeout.domain.DishFlavor;
import com.learn.web.x01_takeout.domain.Setmeal;
import com.learn.web.x01_takeout.domain.SetmealDish;
import com.learn.web.x01_takeout.domain.bo.DishBo;
import com.learn.web.x01_takeout.domain.vo.DishVo;
import com.learn.web.x01_takeout.mapper.CategoryMapper;
import com.learn.web.x01_takeout.mapper.DishFlavorMapper;
import com.learn.web.x01_takeout.mapper.DishMapper;
import com.learn.web.x01_takeout.mapper.SetmealDishMapper;
import com.learn.web.x01_takeout.mapper.SetmealMapper;
import com.learn.web.x01_takeout.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * x01 练习 x.4 ~ x.8：菜品管理（综合运用 p01 ~ p03、b04 的内容）
 */
@Service
@RequiredArgsConstructor
public class DishServiceImpl implements IDishService {

    private final DishMapper baseMapper;
    private final DishFlavorMapper dishFlavorMapper;
    private final CategoryMapper categoryMapper;
    private final SetmealDishMapper setmealDishMapper;
    private final SetmealMapper setmealMapper;

    /**
     * x.4 分页查询：name 模糊、categoryId 相等、status 相等（为空的不拼），按 id 升序。
     * 每条记录转换成 DishVo 并填上 categoryName（flavors 不用填）。
     * <p>分类名称只查一次数据库：收集这一页的 categoryId，selectByIds 一次查出来（见 p01 练习 1.11）。
     * <p>提示：Page 有个 convert 方法可以把 Page&lt;Dish&gt; 转成 Page&lt;DishVo&gt;（IPage.convert），也可以自己 new
     */
    @Override
    public TableDataInfo<DishVo> queryPageList(DishBo bo, PageQuery pageQuery) {
        // @answer
        LambdaQueryWrapper<Dish> lqw = Wrappers.<Dish>lambdaQuery()
                .like(StringUtils.hasText(bo.getName()), Dish::getName, bo.getName())
                .eq(bo.getCategoryId() != null, Dish::getCategoryId, bo.getCategoryId())
                .eq(bo.getStatus() != null, Dish::getStatus, bo.getStatus())
                .orderByAsc(Dish::getId);
        Page<Dish> page = baseMapper.selectPage(pageQuery.build(), lqw);
        Map<Long, String> categoryNames = categoryNames(page.getRecords());
        List<DishVo> rows = page.getRecords().stream().map(d -> toVo(d, categoryNames)).toList();
        return TableDataInfo.build(rows, page.getTotal());
        // @end
    }

    // @hide 可以抽几个私有方法：categoryNames(List<Dish>)、toVo(Dish, Map<Long, String>)、checkCategory、checkNameUnique、saveFlavors
    private Map<Long, String> categoryNames(List<Dish> dishes) {
        List<Long> ids = dishes.stream().map(Dish::getCategoryId).distinct().toList();
        if (ids.isEmpty()) {
            return Map.of();
        }
        return categoryMapper.selectByIds(ids).stream().collect(Collectors.toMap(Category::getId, Category::getName));
    }

    private static DishVo toVo(Dish d, Map<Long, String> categoryNames) {
        DishVo vo = new DishVo();
        vo.setId(d.getId());
        vo.setName(d.getName());
        vo.setCategoryId(d.getCategoryId());
        vo.setCategoryName(categoryNames.get(d.getCategoryId()));
        vo.setPrice(d.getPrice());
        vo.setStatus(d.getStatus());
        vo.setDescription(d.getDescription());
        vo.setUpdateTime(d.getUpdateTime());
        return vo;
    }

    private Dish getDish(Long id) {
        Dish dish = baseMapper.selectById(id);
        if (dish == null) {
            throw new ServiceException("菜品不存在");
        }
        return dish;
    }

    private void checkCategory(Long categoryId) {
        Category c = categoryMapper.selectById(categoryId);
        if (c == null || c.getType() != 1) {
            throw new ServiceException("分类不存在");
        }
    }

    private void checkNameUnique(String name, Long excludeId) {
        boolean exists = baseMapper.exists(Wrappers.<Dish>lambdaQuery()
                .eq(Dish::getName, name)
                .ne(excludeId != null, Dish::getId, excludeId));
        if (exists) {
            throw new ServiceException("菜品名称已存在");
        }
    }

    private void saveFlavors(Long dishId, List<DishBo.DishFlavorBo> flavors) {
        if (flavors == null) {
            return;
        }
        for (DishBo.DishFlavorBo f : flavors) {
            DishFlavor flavor = new DishFlavor();
            flavor.setDishId(dishId);
            flavor.setName(f.getName());
            flavor.setValue(f.getValue());
            dishFlavorMapper.insert(flavor);
        }
    }
    // @end

    /**
     * x.5 详情：DishVo，填上 categoryName 和 flavors（按口味 id 升序）。不存在：ServiceException("菜品不存在")
     */
    @Override
    public DishVo queryById(Long id) {
        // @answer
        Dish dish = getDish(id);
        DishVo vo = toVo(dish, categoryNames(List.of(dish)));
        vo.setFlavors(dishFlavorMapper.selectList(Wrappers.<DishFlavor>lambdaQuery()
                .eq(DishFlavor::getDishId, id)
                .orderByAsc(DishFlavor::getId)));
        return vo;
        // @end
    }

    /**
     * x.6 新增菜品和口味，默认停售，返回新 id。要用事务。按顺序检查：
     * <ol>
     *   <li>分类不存在、或者不是菜品分类（type 不是 1）：ServiceException("分类不存在")</li>
     *   <li>名称已存在：ServiceException("菜品名称已存在")</li>
     * </ol>
     */
    // @hide x.6 加上事务注解
    @Transactional(rollbackFor = Exception.class)
    // @end
    @Override
    public Long insertByBo(DishBo bo) {
        // @answer
        checkCategory(bo.getCategoryId());
        checkNameUnique(bo.getName(), null);
        Dish dish = new Dish();
        dish.setName(bo.getName());
        dish.setCategoryId(bo.getCategoryId());
        dish.setPrice(bo.getPrice());
        dish.setDescription(bo.getDescription());
        dish.setStatus(StatusConstant.DISABLE);
        baseMapper.insert(dish);
        saveFlavors(dish.getId(), bo.getFlavors());
        return dish.getId();
        // @end
    }

    /**
     * x.7 修改菜品（name、categoryId、price、description）并【替换】口味：先删掉这个菜品的所有口味，再插入新的。要用事务。
     * 按顺序检查：菜品不存在 "菜品不存在"；分类 "分类不存在"；名称和别的菜品重复 "菜品名称已存在"。
     */
    // @hide x.7 加上事务注解
    @Transactional(rollbackFor = Exception.class)
    // @end
    @Override
    public void updateByBo(DishBo bo) {
        // @answer
        getDish(bo.getId());
        checkCategory(bo.getCategoryId());
        checkNameUnique(bo.getName(), bo.getId());
        Dish dish = new Dish();
        dish.setId(bo.getId());
        dish.setName(bo.getName());
        dish.setCategoryId(bo.getCategoryId());
        dish.setPrice(bo.getPrice());
        dish.setDescription(bo.getDescription());
        baseMapper.updateById(dish);
        dishFlavorMapper.delete(Wrappers.<DishFlavor>lambdaQuery().eq(DishFlavor::getDishId, bo.getId()));
        saveFlavors(bo.getId(), bo.getFlavors());
        // @end
    }

    /**
     * x.8 批量删除菜品（连同口味）。要用事务，要么全删要么都不删。每个菜品检查：
     * 不存在 "菜品不存在"；起售中 "起售中的菜品不能删除"；被套餐包含 "当前菜品关联了套餐，不能删除"。
     */
    // @hide x.8 加上事务注解
    @Transactional(rollbackFor = Exception.class)
    // @end
    @Override
    public void deleteByIds(List<Long> ids) {
        // @answer
        for (Long id : ids) {
            Dish dish = getDish(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new ServiceException("起售中的菜品不能删除");
            }
            if (setmealDishMapper.exists(Wrappers.<SetmealDish>lambdaQuery().eq(SetmealDish::getDishId, id))) {
                throw new ServiceException("当前菜品关联了套餐，不能删除");
            }
        }
        baseMapper.deleteByIds(ids);
        dishFlavorMapper.delete(Wrappers.<DishFlavor>lambdaQuery().in(DishFlavor::getDishId, ids));
        // @end
    }

    /**
     * x.8 起售 / 停售。status 不是 0 或 1："状态不合法"；不存在："菜品不存在"。
     * 停售（0）时，包含这个菜品的套餐也要一起停售（苍穹外卖的业务规则：套餐里有菜卖不了，套餐也不能卖）。要用事务。
     */
    // @hide x.8 加上事务注解
    @Transactional(rollbackFor = Exception.class)
    // @end
    @Override
    public void changeStatus(Long id, Integer status) {
        // @answer
        if (status == null || (status != 0 && status != 1)) {
            throw new ServiceException("状态不合法");
        }
        getDish(id);
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        baseMapper.updateById(dish);
        if (status == StatusConstant.DISABLE) {
            List<Long> setmealIds = setmealDishMapper.selectList(Wrappers.<SetmealDish>lambdaQuery()
                            .eq(SetmealDish::getDishId, id))
                    .stream().map(SetmealDish::getSetmealId).distinct().toList();
            if (!setmealIds.isEmpty()) {
                setmealMapper.update(Wrappers.<Setmeal>lambdaUpdate()
                        .set(Setmeal::getStatus, StatusConstant.DISABLE)
                        .in(Setmeal::getId, setmealIds));
            }
        }
        // @end
    }
}
