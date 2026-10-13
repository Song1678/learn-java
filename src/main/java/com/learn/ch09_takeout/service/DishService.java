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
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 根据 id 查询，不存在抛 ServiceException("菜品不存在")。
     */
    public Dish getById(Long id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 起售 / 停售。
     * status 不是 1 或 0：ServiceException("状态不合法")；菜品不存在：ServiceException("菜品不存在")。
     */
    public void startOrStop(Long id, int status) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 批量删除。先全部检查，再删除（要么全删，要么一个都不删）：
     * <ul>
     *   <li>有任何一个不存在：ServiceException("菜品不存在")</li>
     *   <li>有任何一个是起售状态：ServiceException("起售中的菜品不能删除")</li>
     * </ul>
     */
    public void deleteBatch(List<Long> ids) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /**
     * 用户端：查询某个分类下【起售中】的菜品，按价格升序，价格相同按 id 升序。
     */
    public List<Dish> listByCategory(Long categoryId) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
