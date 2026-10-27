package com.learn.web.x01_takeout.service;

import com.learn.web.common.PageQuery;
import com.learn.web.common.TableDataInfo;
import com.learn.web.x01_takeout.domain.Category;
import com.learn.web.x01_takeout.domain.bo.CategoryBo;

import java.util.List;

/**
 * 分类管理（接口已写好，实现见 CategoryServiceImpl）。
 * 方法命名和 RuoYi-Vue-Plus 代码生成器生成的一样：queryPageList、queryById、insertByBo、updateByBo ...
 */
public interface ICategoryService {

    TableDataInfo<Category> queryPageList(CategoryBo bo, PageQuery pageQuery);

    List<Category> listEnabledByType(Integer type);

    Category queryById(Long id);

    Long insertByBo(CategoryBo bo);

    void updateByBo(CategoryBo bo);

    void deleteById(Long id);

    void changeStatus(Long id, Integer status);
}
