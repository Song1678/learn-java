package com.learn.web.p01_mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.web.p01_mp.domain.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 继承 BaseMapper&lt;Category&gt; 就自动拥有了 insert、selectById、updateById、deleteById、selectList ... 一个 SQL 都不用写。
 * 对照 ch09：你写的 InMemoryMapper&lt;T&gt; 就是在模仿它。按住 Ctrl 点 BaseMapper 进去看看有哪些方法。
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
