package com.learn.web.x01_takeout.service;

import com.learn.web.common.PageQuery;
import com.learn.web.common.TableDataInfo;
import com.learn.web.x01_takeout.domain.bo.DishBo;
import com.learn.web.x01_takeout.domain.vo.DishVo;

import java.util.List;

/**
 * 菜品管理（接口已写好，实现见 DishServiceImpl）
 */
public interface IDishService {

    TableDataInfo<DishVo> queryPageList(DishBo bo, PageQuery pageQuery);

    DishVo queryById(Long id);

    Long insertByBo(DishBo bo);

    void updateByBo(DishBo bo);

    void deleteByIds(List<Long> ids);

    void changeStatus(Long id, Integer status);
}
