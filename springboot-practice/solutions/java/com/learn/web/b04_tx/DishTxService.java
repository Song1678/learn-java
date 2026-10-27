package com.learn.web.b04_tx;

import com.learn.web.common.ServiceException;
import com.learn.web.common.StatusConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * b04 练习 4.1 ~ 4.2：@Transactional
 *
 * <p>对照 mysql-practice m08 的 OrderSubmitService：以前要自己 setAutoCommit(false) / commit / rollback，
 * 现在只要在方法上加 {@code @Transactional}：方法正常结束 → 提交；方法抛出 RuntimeException → 回滚。
 * （Spring 在你的对象外面包了一层“代理”，在调用方法前后帮你开启、提交、回滚事务。）
 */
@Service
public class DishTxService {

    private final TxMapper txMapper;

    public DishTxService(TxMapper txMapper) {
        this.txMapper = txMapper;
    }

    /**
     * 4.1 新增菜品和它的口味，返回新菜品 id。新菜品默认停售。
     * <p>按这个顺序做：先 insertDish，再逐个 insertFlavor。插入口味时发现口味名称为空白：
     * ServiceException("口味名称不能为空")，这时菜品已经插进去了 —— 要靠事务回滚把它撤销。
     */
    // @hide 4.1 加上注解
    @Transactional
    // @end
    public Long saveWithFlavors(TxDish dish) {
        // @answer
        dish.setStatus(StatusConstant.DISABLE);
        txMapper.insertDish(dish);
        for (TxDish.Flavor f : dish.getFlavors()) {
            if (f.name() == null || f.name().isBlank()) {
                throw new ServiceException("口味名称不能为空");
            }
            txMapper.insertFlavor(dish.getId(), f.name(), f.value());
        }
        return dish.getId();
        // @end
    }

    /**
     * 4.2 批量删除菜品（连同口味）。按 ids 的顺序【逐个】检查并删除：
     * <ul>
     *   <li>菜品不存在：ServiceException("菜品不存在")</li>
     *   <li>起售中：ServiceException("起售中的菜品不能删除")</li>
     *   <li>被套餐包含：ServiceException("当前菜品关联了套餐，不能删除")</li>
     *   <li>否则删除它的口味和它本身，继续下一个</li>
     * </ul>
     * 中途任何一个不能删，前面已经删掉的也要恢复 —— 要么全删，要么都不删。
     * （ch09 里你是“先全部检查、再删除”来保证的，有了事务就可以边检查边删。）
     */
    // @hide 4.2 加上注解
    @Transactional
    // @end
    public void deleteBatch(List<Long> ids) {
        // @answer
        for (Long id : ids) {
            Integer status = txMapper.selectDishStatus(id);
            if (status == null) {
                throw new ServiceException("菜品不存在");
            }
            if (status == StatusConstant.ENABLE) {
                throw new ServiceException("起售中的菜品不能删除");
            }
            if (txMapper.countSetmealByDish(id) > 0) {
                throw new ServiceException("当前菜品关联了套餐，不能删除");
            }
            txMapper.deleteFlavorsByDish(id);
            txMapper.deleteDish(id);
        }
        // @end
    }
}
