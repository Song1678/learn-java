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
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    // TODO 可以抽几个私有方法：categoryNames(List<Dish>)、toVo(Dish, Map<Long, String>)、checkCategory、checkNameUnique、saveFlavors

    /**
     * x.5 详情：DishVo，填上 categoryName 和 flavors（按口味 id 升序）。不存在：ServiceException("菜品不存在")
     */
    @Override
    public DishVo queryById(Long id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * x.6 新增菜品和口味，默认停售，返回新 id。要用事务。按顺序检查：
     * <ol>
     *   <li>分类不存在、或者不是菜品分类（type 不是 1）：ServiceException("分类不存在")</li>
     *   <li>名称已存在：ServiceException("菜品名称已存在")</li>
     * </ol>
     */
    // TODO x.6 加上事务注解
    @Override
    public Long insertByBo(DishBo bo) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * x.7 修改菜品（name、categoryId、price、description）并【替换】口味：先删掉这个菜品的所有口味，再插入新的。要用事务。
     * 按顺序检查：菜品不存在 "菜品不存在"；分类 "分类不存在"；名称和别的菜品重复 "菜品名称已存在"。
     */
    // TODO x.7 加上事务注解
    @Override
    public void updateByBo(DishBo bo) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * x.8 批量删除菜品（连同口味）。要用事务，要么全删要么都不删。每个菜品检查：
     * 不存在 "菜品不存在"；起售中 "起售中的菜品不能删除"；被套餐包含 "当前菜品关联了套餐，不能删除"。
     */
    // TODO x.8 加上事务注解
    @Override
    public void deleteByIds(List<Long> ids) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * x.8 起售 / 停售。status 不是 0 或 1："状态不合法"；不存在："菜品不存在"。
     * 停售（0）时，包含这个菜品的套餐也要一起停售（苍穹外卖的业务规则：套餐里有菜卖不了，套餐也不能卖）。要用事务。
     */
    // TODO x.8 加上事务注解
    @Override
    public void changeStatus(Long id, Integer status) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
