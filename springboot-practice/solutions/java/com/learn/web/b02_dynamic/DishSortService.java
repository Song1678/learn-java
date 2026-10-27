package com.learn.web.b02_dynamic;

import com.learn.web.common.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * b02 练习 2.8：防止 ${} 的 SQL 注入
 *
 * <p>#{} 是预编译参数（相当于 ?），安全；${} 是直接拼接字符串，用户传什么就拼什么，会被 SQL 注入。
 * 但 ORDER BY 后面的列名不能用 #{}（会变成 ORDER BY 'price'，按一个常量排序，等于没排序），只能用 ${}。
 * 所以列名必须由我们自己决定 —— 用白名单把前端传来的字段名翻译成列名。
 */
@Service
public class DishSortService {

    /** 前端字段名 → 数据库列名 */
    private static final Map<String, String> SORTABLE = Map.of(
            "id", "id",
            "price", "price",
            "createTime", "create_time");

    private final DishMapper dishMapper;

    public DishSortService(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    /**
     * 按前端传来的字段排序。field 只能是 id / price / createTime，
     * 其他任何值都抛 ServiceException("不支持的排序字段：" + field)，【不能】把它传给 Mapper。
     */
    public List<Dish> listSorted(String field, boolean asc) {
        // @answer
        String column = SORTABLE.get(field);
        if (column == null) {
            throw new ServiceException("不支持的排序字段：" + field);
        }
        return dishMapper.selectOrderBy(column, asc);
        // @end
    }
}
