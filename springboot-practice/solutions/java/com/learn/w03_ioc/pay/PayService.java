package com.learn.w03_ioc.pay;

import com.learn.web.common.ServiceException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * w03 练习 3.3：收银台
 *
 * <p>构造方法的参数是 Map&lt;String, PayStrategy&gt;：Spring 会把容器里【所有】PayStrategy 类型的 bean 都找出来，
 * 以 bean 名字为 key 放进这个 Map 传进来。做完 3.2 后，这个 Map 是：
 * <pre>{"normal": NormalPay, "discount": DiscountPay, "fullReduction": FullReductionPay}</pre>
 * 以后新增一种支付方式，只要再写一个 @Component 类，这里一行代码都不用改 —— 这就是“面向接口编程 + 依赖注入”的好处。
 */
@Service
public class PayService {

    private final Map<String, PayStrategy> strategies;

    public PayService(Map<String, PayStrategy> strategies) {
        this.strategies = strategies;
    }

    /**
     * 返回所有支付方式的名字，按字母排序：["discount", "fullReduction", "normal"]
     */
    public List<String> supportedTypes() {
        // @answer
        List<String> types = new ArrayList<>(strategies.keySet());
        types.sort(Comparator.naturalOrder());
        return types;
        // @end
    }

    /**
     * 用指定的支付方式计算金额。type 不存在：ServiceException("不支持的支付方式：" + type)
     */
    public BigDecimal pay(String type, BigDecimal amount) {
        // @answer
        PayStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new ServiceException("不支持的支付方式：" + type);
        }
        return strategy.pay(amount);
        // @end
    }

    /**
     * 返回最省钱的支付方式的名字。金额相同时取名字字母序靠前的。
     * <pre>100.00 → fullReduction(80.00)；50.00 → discount(44.00)</pre>
     */
    public String bestType(BigDecimal amount) {
        // @answer
        String best = null;
        BigDecimal bestPrice = null;
        for (String type : supportedTypes()) {
            BigDecimal price = strategies.get(type).pay(amount);
            if (bestPrice == null || price.compareTo(bestPrice) < 0) {
                best = type;
                bestPrice = price;
            }
        }
        return best;
        // @end
    }
}
