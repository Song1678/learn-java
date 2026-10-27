package com.learn.web.b04_tx;

import com.learn.web.common.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * b04 练习 4.3：用户下单（苍穹外卖最核心的一个事务）
 */
@Service
public class OrderTxService {

    private static final DateTimeFormatter NO_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final TxMapper txMapper;

    public OrderTxService(TxMapper txMapper) {
        this.txMapper = txMapper;
    }

    /**
     * 4.3 把用户的购物车变成一个订单，返回插入后的订单（带 id）。步骤：
     * <ol>
     *   <li>地址不存在或不属于这个用户：ServiceException("收货地址不存在")</li>
     *   <li>购物车为空：ServiceException("购物车为空，不能下单")</li>
     *   <li>插入订单：status=1（待付款），orderTime=现在，number=下单时间 yyyyMMddHHmmss + 用户id，
     *       amount=购物车里每一项 amount × number 之和，remark=参数 remark</li>
     *   <li>每个购物车项插入一条订单明细（dish_id、setmeal_id、name、number、amount 原样复制）</li>
     *   <li>清空这个用户的购物车</li>
     * </ol>
     * 第 4 步插明细时，如果购物车里某个菜品已经停售（用 selectDishStatus 查，套餐不用检查）：
     * ServiceException(菜品名 + "已停售，请重新选择")，订单不能留在数据库里，购物车也不能被清空。
     * <p>提示：selectCart 返回的 Map 里，dish_id 可能是 null（套餐），数字类型是 Long / BigDecimal / Integer，
     * 可以用 ((Number) row.get("dish_id")).longValue() 这种方式转换。
     */
    // TODO 4.3 加上注解
    public TxOrder submit(Long userId, Long addressBookId, String remark) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
