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
    // @swap
    @GetMapping("/list")
    public TableDataInfo<Category> list(CategoryBo bo, PageQuery pageQuery) {
    //> // TODO 加注解
    //> public TableDataInfo<Category> list(CategoryBo bo, PageQuery pageQuery) {
    // @end
        // @answer
        return categoryService.queryPageList(bo, pageQuery);
        // @end
    }

    /** GET /admin/category/listByType?type=1 → R&lt;List&lt;Category&gt;&gt; 启用的分类 */
    // @swap
    @GetMapping("/listByType")
    public R<List<Category>> listByType(@RequestParam Integer type) {
    //> // TODO 加注解
    //> public R<List<Category>> listByType(Integer type) {
    // @end
        // @answer
        return R.ok(categoryService.listEnabledByType(type));
        // @end
    }

    /** GET /admin/category/{id} → R&lt;Category&gt; */
    // @swap
    @GetMapping("/{id}")
    public R<Category> getInfo(@PathVariable Long id) {
    //> // TODO 加注解
    //> public R<Category> getInfo(Long id) {
    // @end
        // @answer
        return R.ok(categoryService.queryById(id));
        // @end
    }

    /** POST /admin/category，JSON 请求体，按新增分组校验 → R&lt;Long&gt; 新 id */
    // @swap
    @PostMapping
    public R<Long> add(@Validated(AddGroup.class) @RequestBody CategoryBo bo) {
    //> // TODO 加注解
    //> public R<Long> add(CategoryBo bo) {
    // @end
        // @answer
        return R.ok(categoryService.insertByBo(bo));
        // @end
    }

    /** PUT /admin/category，JSON 请求体，按修改分组校验 → R&lt;Void&gt; */
    // @swap
    @PutMapping
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CategoryBo bo) {
    //> // TODO 加注解
    //> public R<Void> edit(CategoryBo bo) {
    // @end
        // @answer
        categoryService.updateByBo(bo);
        return R.ok();
        // @end
    }

    /** DELETE /admin/category/{id} → R&lt;Void&gt; */
    // @swap
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
    //> // TODO 加注解
    //> public R<Void> remove(Long id) {
    // @end
        // @answer
        categoryService.deleteById(id);
        return R.ok();
        // @end
    }

    /** PUT /admin/category/{id}/status/{status} → R&lt;Void&gt; */
    // @swap
    @PutMapping("/{id}/status/{status}")
    public R<Void> changeStatus(@PathVariable Long id, @PathVariable Integer status) {
    //> // TODO 加注解
    //> public R<Void> changeStatus(Long id, Integer status) {
    // @end
        // @answer
        categoryService.changeStatus(id, status);
        return R.ok();
        // @end
    }
}
