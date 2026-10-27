package com.learn.web.p01_mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.web.p01_mp.domain.Dish;

import java.util.List;

/**
 * 菜品 Service 接口（已写好）。
 *
 * <p>继承 IService&lt;Dish&gt;，自动拥有 save、saveBatch、getById、list、count、removeByIds、lambdaQuery ... 等方法。
 * （RuoYi-Vue-Plus 的 Service 一般不继承 IService，而是直接注入 Mapper 用，两种写法你都要认识。）
 */
public interface IDishMpService extends IService<Dish> {

    /** 1.9 某个分类下起售中的菜品，按价格升序 */
    List<Dish> listOnSaleByCategory(Long categoryId);

    /** 1.10 根据名称精确查询，不存在返回 null */
    Dish getByName(String name);

    /** 1.11 根据多个 id 查询菜品，并填好 categoryName */
    List<Dish> listWithCategoryName(List<Long> dishIds);
}
