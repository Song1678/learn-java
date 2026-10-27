package com.learn.web.x01_takeout.controller;

import com.learn.web.common.AddGroup;
import com.learn.web.common.EditGroup;
import com.learn.web.common.PageQuery;
import com.learn.web.common.R;
import com.learn.web.common.TableDataInfo;
import com.learn.web.x01_takeout.domain.bo.DishBo;
import com.learn.web.x01_takeout.domain.vo.DishVo;
import com.learn.web.x01_takeout.service.IDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * x01 练习 x.4 ~ x.8：菜品管理接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    private final IDishService dishService;

    /** GET /admin/dish/list?name=&amp;categoryId=&amp;status=&amp;pageNum=&amp;pageSize= → TableDataInfo&lt;DishVo&gt; */
    // TODO 加注解
    public TableDataInfo<DishVo> list(DishBo bo, PageQuery pageQuery) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** GET /admin/dish/{id} → R&lt;DishVo&gt;（带口味） */
    // TODO 加注解
    public R<DishVo> getInfo(Long id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** POST /admin/dish，JSON 请求体，按新增分组校验 → R&lt;Long&gt; 新 id */
    // TODO 加注解
    public R<Long> add(DishBo bo) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** PUT /admin/dish，JSON 请求体，按修改分组校验 → R&lt;Void&gt; */
    // TODO 加注解
    public R<Void> edit(DishBo bo) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** DELETE /admin/dish/{ids}，例如 /admin/dish/10,18 → R&lt;Void&gt; */
    // TODO 加注解
    public R<Void> remove(List<Long> ids) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** PUT /admin/dish/{id}/status/{status} → R&lt;Void&gt; */
    // TODO 加注解
    public R<Void> changeStatus(Long id, Integer status) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
