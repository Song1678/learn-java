package com.learn.web.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * 分页查询的返回结果（已写好），和 RuoYi-Vue-Plus 的 TableDataInfo 一样：
 * <pre>
 * {"code": 200, "msg": "查询成功", "total": 20, "rows": [ ... ]}
 * </pre>
 * 注意它【不】包在 R 里面，列表接口直接返回它。
 */
@Data
public class TableDataInfo<T> {

    private int code = R.SUCCESS;
    private String msg = "查询成功";
    /** 总记录数（不是当前页的条数） */
    private long total;
    /** 当前页的数据 */
    private List<T> rows;

    public static <T> TableDataInfo<T> build(List<T> rows, long total) {
        TableDataInfo<T> t = new TableDataInfo<>();
        t.setRows(rows);
        t.setTotal(total);
        return t;
    }

    /** 从 MyBatis-Plus 的分页结果转换 */
    public static <T> TableDataInfo<T> build(IPage<T> page) {
        return build(page.getRecords(), page.getTotal());
    }
}
