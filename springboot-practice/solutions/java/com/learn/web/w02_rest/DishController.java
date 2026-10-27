package com.learn.web.w02_rest;

import com.learn.web.common.R;
import com.learn.web.common.TableDataInfo;
import com.learn.web.w02_rest.domain.Dish;
import com.learn.web.w02_rest.domain.DishBo;
import com.learn.web.w02_rest.domain.DishQuery;
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
 * w02 练习 2.1 ~ 2.7：菜品的增删改查接口（RESTful 风格）
 *
 * <p>和 ch09 的 DishController 对比：以前是 main 方法里手动调用 controller.add(dto)，
 * 现在是 Apifox / 浏览器发 HTTP 请求，Spring 根据 URL 和请求方式找到对应的方法，把参数转换好传进来，
 * 再把返回值转成 JSON 写回去。
 *
 * <p>所有接口都返回 R（除了 2.3 分页返回 TableDataInfo）。类上的两个注解已经写好了。
 */
@RestController
@RequestMapping("/w02/dishes")
public class DishController {

    private final DishStore dishStore;

    public DishController(DishStore dishStore) {
        this.dishStore = dishStore;
    }

    /**
     * 2.1 GET /w02/dishes/{id} → 查询单个菜品，返回 R.ok(dish)；不存在返回 R.fail("菜品不存在")
     */
    // @swap
    @GetMapping("/{id}")
    public R<Dish> getById(@PathVariable Long id) {
    //> // TODO 2.1 加上映射注解和参数注解
    //> public R<Dish> getById(Long id) {
    // @end
        // @answer
        Dish dish = dishStore.findById(id);
        return dish == null ? R.fail("菜品不存在") : R.ok(dish);
        // @end
    }

    /**
     * 2.2 GET /w02/dishes?name=鸡&amp;categoryId=2&amp;status=1 → 条件查询，返回 R.ok(list)
     * <p>三个条件都可以不传。参数 DishQuery 不用加注解，Spring 会把 URL 参数按名字 set 到它的字段里。
     * 查询逻辑 dishStore.query 已经写好了。
     */
    // @hide 2.2 加上映射注解（注意：路径就是 /w02/dishes 本身）
    @GetMapping
    // @end
    public R<List<Dish>> list(DishQuery query) {
        // @answer
        return R.ok(dishStore.query(query));
        // @end
    }

    /**
     * 2.3 GET /w02/dishes/page?pageNum=2&amp;pageSize=4 → 分页查询（按 id 升序）
     * <p>pageNum 不传默认 1，pageSize 不传默认 10。返回 TableDataInfo.build(当前页的数据, 总条数)。
     * <pre>共 6 条，pageNum=2, pageSize=4 → rows 是第 5、6 条，total = 6；页码超出范围 → rows 为空列表</pre>
     * <p>思考：为什么 /page 这个接口不会被 /{id} 匹配走？（Spring 优先匹配更具体的路径）
     */
    // @swap
    @GetMapping("/page")
    public TableDataInfo<Dish> page(@RequestParam(defaultValue = "1") int pageNum,
                                    @RequestParam(defaultValue = "10") int pageSize) {
    //> // TODO 2.3 加上映射注解和参数注解（带默认值）
    //> public TableDataInfo<Dish> page(int pageNum, int pageSize) {
    // @end
        // @answer 提示：先 findAll()，再用 subList 截取，注意 fromIndex / toIndex 不能越界
        List<Dish> all = dishStore.findAll();
        int from = Math.min((pageNum - 1) * pageSize, all.size());
        int to = Math.min(from + pageSize, all.size());
        return TableDataInfo.build(all.subList(from, to), all.size());
        // @end
    }

    /**
     * 2.4 POST /w02/dishes，请求体是 JSON（DishBo）→ 新增菜品，新菜品默认停售（status=0），返回 R.ok(新id)
     * <p>提示：JSON 请求体要用 @RequestBody 接收；把 DishBo 的字段复制到一个新的 Dish 对象里再 insert。
     */
    // @swap
    @PostMapping
    public R<Long> add(@RequestBody DishBo bo) {
    //> // TODO 2.4 加上映射注解和参数注解
    //> public R<Long> add(DishBo bo) {
    // @end
        // @answer
        Dish dish = new Dish();
        dish.setName(bo.getName());
        dish.setCategoryId(bo.getCategoryId());
        dish.setPrice(bo.getPrice());
        dish.setDescription(bo.getDescription());
        dish.setStatus(0);
        return R.ok(dishStore.insert(dish));
        // @end
    }

    /**
     * 2.5 PUT /w02/dishes，请求体是 JSON（DishBo，带 id）→ 修改菜品，只修改传了的字段
     * <p>成功返回 R.ok()；菜品不存在返回 R.fail("菜品不存在")。
     */
    // @swap
    @PutMapping
    public R<Void> edit(@RequestBody DishBo bo) {
    //> // TODO 2.5 加上映射注解和参数注解
    //> public R<Void> edit(DishBo bo) {
    // @end
        // @answer
        Dish dish = new Dish();
        dish.setId(bo.getId());
        dish.setName(bo.getName());
        dish.setCategoryId(bo.getCategoryId());
        dish.setPrice(bo.getPrice());
        dish.setDescription(bo.getDescription());
        return dishStore.update(dish) ? R.ok() : R.fail("菜品不存在");
        // @end
    }

    /**
     * 2.6 DELETE /w02/dishes/1,2,3 → 批量删除，返回 R.ok(实际删除的条数)
     * <p>提示：路径变量 "1,2,3" 可以直接用 List&lt;Long&gt; 接收，Spring 会按逗号拆开并转换类型。
     * RuoYi-Vue-Plus 的删除接口都是这么写的：{@code @DeleteMapping("/{ids}") remove(@PathVariable Long[] ids)}
     */
    // @swap
    @DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable List<Long> ids) {
    //> // TODO 2.6 加上映射注解和参数注解
    //> public R<Integer> remove(List<Long> ids) {
    // @end
        // @answer
        return R.ok(dishStore.deleteByIds(ids));
        // @end
    }

    /**
     * 2.7 PUT /w02/dishes/{id}/status/{status} → 起售(1) / 停售(0)
     * <p>status 不是 0 或 1：R.fail("状态不合法")；菜品不存在：R.fail("菜品不存在")；成功：R.ok()
     */
    // @swap
    @PutMapping("/{id}/status/{status}")
    public R<Void> changeStatus(@PathVariable Long id, @PathVariable Integer status) {
    //> // TODO 2.7 加上映射注解和参数注解（两个路径变量）
    //> public R<Void> changeStatus(Long id, Integer status) {
    // @end
        // @answer
        if (status != 0 && status != 1) {
            return R.fail("状态不合法");
        }
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        return dishStore.update(dish) ? R.ok() : R.fail("菜品不存在");
        // @end
    }
}
