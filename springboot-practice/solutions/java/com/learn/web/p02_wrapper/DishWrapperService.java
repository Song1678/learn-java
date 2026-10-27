package com.learn.web.p02_wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.learn.web.common.StatusConstant;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * p02 练习 2.1 ~ 2.8：条件构造器 Wrapper
 *
 * <p>Wrapper 就是“用 Java 代码拼 WHERE 条件”：
 * <pre>
 * new LambdaQueryWrapper&lt;Dish&gt;()
 *         .eq(Dish::getCategoryId, 1)          // category_id = 1
 *         .like(Dish::getName, "鸡")            // AND name LIKE '%鸡%'
 *         .ge(Dish::getPrice, 20)              // AND price &gt;= 20
 *         .orderByAsc(Dish::getPrice);         // ORDER BY price ASC
 * </pre>
 * 常用方法：eq ne gt ge lt le between like in isNull orderByAsc orderByDesc select groupBy or and last。
 * 每个条件方法都有一个“第一个参数是 boolean”的重载：{@code .eq(categoryId != null, Dish::getCategoryId, categoryId)}，
 * boolean 为 false 时这个条件不拼 —— 相当于 b02 XML 里的 &lt;if&gt;。RuoYi-Vue-Plus 的 buildQueryWrapper 全是这么写的。
 *
 * <p>调试技巧：application-test.yml 里把 com.learn.web 的日志级别改成 debug，就能看到生成的 SQL。
 */
@Service
public class DishWrapperService {

    private final DishMapper dishMapper;

    public DishWrapperService(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    /**
     * 2.1 动态条件查询（和 b02 的 selectList 一样的需求，对比一下两种写法）：
     * name 模糊、categoryId 相等、status 相等、price &gt;= minPrice、price &lt;= maxPrice，为 null（name 为空白）的条件不拼。
     * 按价格升序，价格相同按 id 升序。
     * <p>提示：判断字符串非空白可以用 Spring 的 StringUtils.hasText(name)
     */
    public List<Dish> search(DishQuery q) {
        // @answer
        LambdaQueryWrapper<Dish> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(q.getName()), Dish::getName, q.getName())
                .eq(q.getCategoryId() != null, Dish::getCategoryId, q.getCategoryId())
                .eq(q.getStatus() != null, Dish::getStatus, q.getStatus())
                .ge(q.getMinPrice() != null, Dish::getPrice, q.getMinPrice())
                .le(q.getMaxPrice() != null, Dish::getPrice, q.getMaxPrice())
                .orderByAsc(Dish::getPrice)
                .orderByAsc(Dish::getId);
        return dishMapper.selectList(lqw);
        // @end
    }

    /**
     * 2.2 起售中、并且名称或描述包含 keyword 的菜品，按 id 升序。
     * <p>要生成的 SQL：{@code WHERE status = 1 AND (name LIKE ? OR description LIKE ?)}
     * <p>注意括号！直接写 .eq(...).like(...).or().like(...) 生成的是
     * {@code status = 1 AND name LIKE ? OR description LIKE ?}，AND 优先级比 OR 高，停售的菜品也会被查出来。
     * 要用 {@code .and(w -> w.like(...).or().like(...))} 生成括号。
     */
    public List<Dish> searchOnSale(String keyword) {
        // @answer
        return dishMapper.selectList(Wrappers.<Dish>lambdaQuery()
                .eq(Dish::getStatus, StatusConstant.ENABLE)
                .and(w -> w.like(Dish::getName, keyword).or().like(Dish::getDescription, keyword))
                .orderByAsc(Dish::getId));
        // @end
    }

    /**
     * 2.3 某个分类下菜品的名称列表，按 id 升序。只查 id 和 name 两列（select 方法），不要 SELECT *。
     */
    public List<String> listNames(Long categoryId) {
        // @answer
        return dishMapper.selectList(Wrappers.<Dish>lambdaQuery()
                        .select(Dish::getId, Dish::getName)
                        .eq(Dish::getCategoryId, categoryId)
                        .orderByAsc(Dish::getId))
                .stream().map(Dish::getName).toList();
        // @end
    }

    /**
     * 2.4 最便宜的 n 个起售中的菜品（价格升序，相同按 id），用 last("LIMIT " + n)。
     * <p>last 会把字符串原样拼到 SQL 最后，有 SQL 注入风险，只能拼自己控制的内容（这里 n 是 int，安全）。
     * 分页查询的正规做法见 p03。
     */
    public List<Dish> cheapest(int n) {
        // @answer
        return dishMapper.selectList(Wrappers.<Dish>lambdaQuery()
                .eq(Dish::getStatus, StatusConstant.ENABLE)
                .orderByAsc(Dish::getPrice)
                .orderByAsc(Dish::getId)
                .last("LIMIT " + n));
        // @end
    }

    /**
     * 2.5 名称是否已存在（exists 方法）。新增菜品时检查重名就用它。
     */
    public boolean existsByName(String name) {
        // @answer
        return dishMapper.exists(Wrappers.<Dish>lambdaQuery().eq(Dish::getName, name));
        // @end
    }

    /**
     * 2.6 统计每个分类有多少道菜：返回 {分类id: 菜品数}，按分类 id 排序（TreeMap）。
     * <p>分组统计 Lambda 写法不方便，用普通的 QueryWrapper 写列名：
     * <pre>new QueryWrapper&lt;Dish&gt;().select("category_id", "COUNT(*) AS cnt").groupBy("category_id")</pre>
     * 再用 selectMaps 查询，每一行是一个 Map（key 是列名）。数字可以先转成 Number 再 .longValue()。
     */
    public Map<Long, Long> countByCategory() {
        // @answer
        List<Map<String, Object>> rows = dishMapper.selectMaps(new QueryWrapper<Dish>()
                .select("category_id", "COUNT(*) AS cnt")
                .groupBy("category_id"));
        Map<Long, Long> result = new TreeMap<>();
        for (Map<String, Object> row : rows) {
            result.put(((Number) row.get("category_id")).longValue(), ((Number) row.get("cnt")).longValue());
        }
        return result;
        // @end
    }

    /**
     * 2.7 把某个分类下所有【起售中】的菜品改成停售，返回修改的行数。
     * <p>用 LambdaUpdateWrapper：.set(Dish::getStatus, 0).eq(...).eq(...)，再 dishMapper.update(wrapper)。
     * 苍穹外卖里“停售分类时把分类下的菜品都停售”就是这个需求。
     */
    public int stopSaleByCategory(Long categoryId) {
        // @answer
        return dishMapper.update(Wrappers.<Dish>lambdaUpdate()
                .set(Dish::getStatus, StatusConstant.DISABLE)
                .eq(Dish::getCategoryId, categoryId)
                .eq(Dish::getStatus, StatusConstant.ENABLE));
        // @end
    }

    /**
     * 2.8 某个分类下所有菜品涨价 delta 元，返回修改的行数。
     * <p>要生成 {@code SET price = price + ?}，用 LambdaUpdateWrapper 的 setIncrBy(Dish::getPrice, delta)。
     */
    public int raisePrice(Long categoryId, BigDecimal delta) {
        // @answer
        LambdaUpdateWrapper<Dish> luw = Wrappers.<Dish>lambdaUpdate()
                .setIncrBy(Dish::getPrice, delta)
                .eq(Dish::getCategoryId, categoryId);
        return dishMapper.update(luw);
        // @end
    }
}
