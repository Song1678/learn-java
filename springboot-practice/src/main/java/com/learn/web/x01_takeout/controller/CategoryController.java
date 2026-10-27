package com.learn.web.x01_takeout.controller;

import com.learn.web.common.AddGroup;
import com.learn.web.common.EditGroup;
import com.learn.web.common.PageQuery;
import com.learn.web.common.R;
import com.learn.web.common.TableDataInfo;
import com.learn.web.x01_takeout.domain.Category;
import com.learn.web.x01_takeout.domain.bo.CategoryBo;
import com.learn.web.x01_takeout.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * x01 练习 x.1 ~ x.3：分类管理接口。接口文档见 docs/x01-综合项目.md，每个方法上也写了。
 *
 * <p>Controller 只做三件事：接收参数（加注解）、调用 Service、包装返回值。每个方法体基本只有一两行。
 * 和 RuoYi-Vue-Plus 代码生成器生成的 Controller 几乎一样（只是少了权限注解 @SaCheckPermission）。
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    private final ICategoryService categoryService;

    /** GET /admin/category/list?name=&amp;type=&amp;pageNum=&amp;pageSize= → TableDataInfo&lt;Category&gt; */
    // TODO 加注解
    public TableDataInfo<Category> list(CategoryBo bo, PageQuery pageQuery) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** GET /admin/category/listByType?type=1 → R&lt;List&lt;Category&gt;&gt; 启用的分类 */
    // TODO 加注解
    public R<List<Category>> listByType(Integer type) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** GET /admin/category/{id} → R&lt;Category&gt; */
    // TODO 加注解
    public R<Category> getInfo(Long id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** POST /admin/category，JSON 请求体，按新增分组校验 → R&lt;Long&gt; 新 id */
    // TODO 加注解
    public R<Long> add(CategoryBo bo) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** PUT /admin/category，JSON 请求体，按修改分组校验 → R&lt;Void&gt; */
    // TODO 加注解
    public R<Void> edit(CategoryBo bo) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** DELETE /admin/category/{id} → R&lt;Void&gt; */
    // TODO 加注解
    public R<Void> remove(Long id) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }

    /** PUT /admin/category/{id}/status/{status} → R&lt;Void&gt; */
    // TODO 加注解
    public R<Void> changeStatus(Long id, Integer status) {
        // TODO 在这里写你的代码
        throw new UnsupportedOperationException("TODO: 还没实现");
    }
}
