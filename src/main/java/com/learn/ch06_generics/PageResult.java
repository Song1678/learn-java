package com.learn.ch06_generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 练习 3：泛型 + 分页（仿照 RuoYi-Vue-Plus 的 TableDataInfo）
 *
 * <p>后台管理系统的表格都是分页查询：返回当前页的数据 rows 和总条数 total。
 *
 * @param <T> 每一行数据的类型
 */
public class PageResult<T> {

    private final long total;
    private final List<T> rows;

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    /**
     * 对内存里的完整列表做分页。pageNum 从 1 开始。
     * <ul>
     *   <li>pageNum &lt; 1 或 pageSize &lt; 1：抛 IllegalArgumentException</li>
     *   <li>total 永远是 all.size()</li>
     *   <li>超出范围的页码返回空的 rows</li>
     * </ul>
     * <pre>
     * all = [1..10], pageNum=2, pageSize=3 -> total=10, rows=[4, 5, 6]
     * all = [1..10], pageNum=4, pageSize=3 -> total=10, rows=[10]
     * all = [1..10], pageNum=5, pageSize=3 -> total=10, rows=[]
     * </pre>
     * 提示：list.subList(from, to)，返回前用 new ArrayList&lt;&gt;(...) 拷贝一份。
     */
    public static <T> PageResult<T> of(List<T> all, int pageNum, int pageSize) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    public long getTotal() {
        return total;
    }

    public List<T> getRows() {
        return rows;
    }
}
