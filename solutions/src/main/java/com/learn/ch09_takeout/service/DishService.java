package com.learn.ch09_takeout.service;

import com.learn.ch09_takeout.common.ServiceException;
import com.learn.ch09_takeout.common.StatusConstant;
import com.learn.ch09_takeout.domain.Dish;
import com.learn.ch09_takeout.dto.DishDTO;
import com.learn.ch09_takeout.mapper.CategoryMapper;
import com.learn.ch09_takeout.mapper.DishMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 练习 9.3：菜品管理
 */
public class DishService {

    private final DishMapper dishMapper;
    private final CategoryMapper categoryMapper;

    public DishService(DishMapper dishMapper, CategoryMapper categoryMapper) {
        this.dishMapper = dishMapper;
        this.categoryMapper = categoryMapper;
    }

    /**
     * 新增菜品，新菜品默认【停售】，返回新菜品 id。按顺序校验：
     * <ol>
     *   <li>名称为 null 或空白：ServiceException("菜品名称不能为空")</li>
     *   <li>价格为 null 或 &lt;= 0：ServiceException("菜品价格必须大于0")</li>
     *   <li>分类不存在：ServiceException("分类不存在")</li>
     *   <li>名称已存在：ServiceException("菜品名称已存在")</li>
     * </ol>
     * 记得把 dto 里的 description 也设置上。
     */
    public Long add(DishDTO dto) {
        // @answer
        if (dto.name() == null || dto.name().isBlank()) {
            throw new ServiceException("菜品名称不能为空");
        }
        if (dto.price() == null || dto.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("菜品价格必须大于0");
        }
        if (dto.categoryId() == null || categoryMapper.selectById(dto.categoryId()) == null) {
            throw new ServiceException("分类不存在");
        }
        if (dishMapper.selectCount(d -> d.getName().equals(dto.name())) > 0) {
            throw new ServiceException("菜品名称已存在");
        }
        Dish dish = new Dish(dto.name(), dto.categoryId(), dto.price(), StatusConstant.DISABLE);
        dish.setDescription(dto.description());
        dishMapper.insert(dish);
        return dish.getId();
        // @end
    }

    /**
     * 根据 id 查询，不存在抛 ServiceException("菜品不存在")。
     */
    public Dish getById(Long id) {
        // @answer
        Dish dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new ServiceException("菜品不存在");
        }
        return dish;
        // @end
    }

    /**
     * 起售 / 停售。
     * status 不是 1 或 0：ServiceException("状态不合法")；菜品不存在：ServiceException("菜品不存在")。
     */
    public void startOrStop(Long id, int status) {
        // @answer
        if (status != StatusConstant.ENABLE && status != StatusConstant.DISABLE) {
            throw new ServiceException("状态不合法");
        }
        Dish dish = getById(id);
        dish.setStatus(status);
        dishMapper.updateById(dish);
        // @end
    }

    /**
     * 批量删除。先全部检查，再删除（要么全删，要么一个都不删）：
     * <ul>
     *   <li>有任何一个不存在：ServiceException("菜品不存在")</li>
     *   <li>有任何一个是起售状态：ServiceException("起售中的菜品不能删除")</li>
     * </ul>
     */
    public void deleteBatch(List<Long> ids) {
        // @answer
        List<Dish> dishes = new ArrayList<>();
        for (Long id : ids) {
            dishes.add(getById(id));
        }
        for (Dish d : dishes) {
            if (d.getStatus() == StatusConstant.ENABLE) {
                throw new ServiceException("起售中的菜品不能删除");
            }
        }
        for (Long id : ids) {
            dishMapper.deleteById(id);
        }
        // @end
    }

    /**
     * 用户端：查询某个分类下【起售中】的菜品，按价格升序，价格相同按 id 升序。
     */
    public List<Dish> listByCategory(Long categoryId) {
        // @answer
        return dishMapper.selectList(d -> d.getCategoryId().equals(categoryId)
                        && d.getStatus() == StatusConstant.ENABLE).stream()
                .sorted(Comparator.comparing(Dish::getPrice).thenComparing(Dish::getId))
                .toList();
        // @end
    }
}
