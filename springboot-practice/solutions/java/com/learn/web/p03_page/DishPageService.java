package com.learn.web.p03_page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.web.common.PageQuery;
import com.learn.web.common.TableDataInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * p03 练习 3.5 ~ 3.8：分页、自动填充、逻辑删除。先做完 3.1 ~ 3.4。
 */
@Service
public class DishPageService {

    private final DishMapper dishMapper;

    public DishPageService(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    /**
     * 3.5 分页条件查询，按 id 升序。和 RuoYi-Vue-Plus 的 queryPageList 一模一样：
     * <pre>
     * LambdaQueryWrapper&lt;Dish&gt; lqw = buildQueryWrapper(bo);
     * Page&lt;Dish&gt; page = dishMapper.selectPage(pageQuery.build(), lqw);
     * return TableDataInfo.build(page);
     * </pre>
     */
    public TableDataInfo<Dish> queryPageList(DishBo bo, PageQuery pageQuery) {
        // @answer
        Page<Dish> page = dishMapper.selectPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(page);
        // @end
    }

    /** 3.5 查询条件：name 模糊、categoryId 相等、status 相等（为空的不拼），按 id 升序 */
    private LambdaQueryWrapper<Dish> buildQueryWrapper(DishBo bo) {
        // @answer
        LambdaQueryWrapper<Dish> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(bo.getName()), Dish::getName, bo.getName());
        lqw.eq(bo.getCategoryId() != null, Dish::getCategoryId, bo.getCategoryId());
        lqw.eq(bo.getStatus() != null, Dish::getStatus, bo.getStatus());
        lqw.orderByAsc(Dish::getId);
        return lqw;
        // @end
    }

    /**
     * 3.6 新增菜品，返回 id。【不要】手动 set createTime / updateTime，让自动填充来做。
     */
    public Long add(Dish dish) {
        // @answer
        dishMapper.insert(dish);
        return dish.getId();
        // @end
    }

    /**
     * 3.7 修改价格，返回是否成功。同样不要手动 set updateTime。
     * <p>提示：new 一个只有 id 和 price 的 Dish 再 updateById（不要先 selectById 再改，原因见 MyMetaObjectHandler 的注释）。
     */
    public boolean changePrice(Long id, BigDecimal price) {
        // @answer
        Dish d = new Dish();
        d.setId(id);
        d.setPrice(price);
        return dishMapper.updateById(d) == 1;
        // @end
    }

    /**
     * 3.8 删除菜品，返回是否成功（做完 3.4，deleteById 就自动变成逻辑删除了）
     */
    public boolean remove(Long id) {
        // @answer
        return dishMapper.deleteById(id) == 1;
        // @end
    }

    /** 根据 id 查询（已写好）。逻辑删除的查不到 */
    public Dish getById(Long id) {
        return dishMapper.selectById(id);
    }
}
