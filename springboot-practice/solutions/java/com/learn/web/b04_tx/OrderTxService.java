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
    // @hide 4.3 加上注解
    @Transactional
    // @end
    public TxOrder submit(Long userId, Long addressBookId, String remark) {
        // @answer
        if (txMapper.selectAddressId(addressBookId, userId) == null) {
            throw new ServiceException("收货地址不存在");
        }
        List<Map<String, Object>> cart = txMapper.selectCart(userId);
        if (cart.isEmpty()) {
            throw new ServiceException("购物车为空，不能下单");
        }
        BigDecimal amount = BigDecimal.ZERO;
        for (Map<String, Object> item : cart) {
            BigDecimal price = (BigDecimal) item.get("amount");
            int number = ((Number) item.get("number")).intValue();
            amount = amount.add(price.multiply(BigDecimal.valueOf(number)));
        }
        LocalDateTime now = LocalDateTime.now().withNano(0);
        TxOrder order = new TxOrder();
        order.setNumber(now.format(NO_FORMAT) + userId);
        order.setStatus(1);
        order.setUserId(userId);
        order.setAddressBookId(addressBookId);
        order.setOrderTime(now);
        order.setAmount(amount);
        order.setRemark(remark);
        txMapper.insertOrder(order);

        for (Map<String, Object> item : cart) {
            Long dishId = item.get("dish_id") == null ? null : ((Number) item.get("dish_id")).longValue();
            Long setmealId = item.get("setmeal_id") == null ? null : ((Number) item.get("setmeal_id")).longValue();
            String name = (String) item.get("name");
            if (dishId != null) {
                Integer status = txMapper.selectDishStatus(dishId);
                if (status == null || status == 0) {
                    throw new ServiceException(name + "已停售，请重新选择");
                }
            }
            txMapper.insertOrderDetail(order.getId(), dishId, setmealId, name,
                    ((Number) item.get("number")).intValue(), (BigDecimal) item.get("amount"));
        }
        txMapper.clearCart(userId);
        return order;
        // @end
    }
}
