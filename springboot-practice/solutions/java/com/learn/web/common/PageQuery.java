package com.learn.web.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 分页参数（已写好），模仿 RuoYi-Vue-Plus 的 PageQuery。
 *
 * <p>前端传 ?pageNum=2&amp;pageSize=10，Controller 方法参数写 PageQuery pageQuery 就能自动接收。
 */
@Data
public class PageQuery {

    /** 第几页，从 1 开始 */
    private Integer pageNum = 1;

    /** 每页多少条 */
    private Integer pageSize = 10;

    /** 转换成 MyBatis-Plus 的分页对象 */
    public <T> Page<T> build() {
        int num = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int size = pageSize == null || pageSize < 1 ? 10 : pageSize;
        return new Page<>(num, size);
    }
}
