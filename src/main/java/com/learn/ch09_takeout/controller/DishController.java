package com.learn.ch09_takeout.controller;

import com.learn.ch09_takeout.common.GlobalExceptionHandler;
import com.learn.ch09_takeout.common.R;
import com.learn.ch09_takeout.domain.Dish;
import com.learn.ch09_takeout.dto.DishDTO;
import com.learn.ch09_takeout.service.DishService;

import java.util.List;

/**
 * 菜品接口（已写好）。
 *
 * <p>Controller 只做三件事：接收参数、调用 Service、包装成 R 返回。业务逻辑全部放在 Service。
 * 到了 Spring Boot 里，它长这样：
 * <pre>
 * &#64;RestController
 * &#64;RequestMapping("/admin/dish")
 * public class DishController {
 *     &#64;Autowired private DishService dishService;
 *
 *     &#64;PostMapping
 *     public R&lt;Long&gt; add(&#64;RequestBody DishDTO dto) { return R.ok(dishService.add(dto)); }
 * }
 * </pre>
 */
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /** POST /admin/dish */
    public R<Long> add(DishDTO dto) {
        return GlobalExceptionHandler.execute(() -> R.ok(dishService.add(dto)));
    }

    /** POST /admin/dish/status/{status}?id=xx */
    public R<Void> startOrStop(int status, Long id) {
        return GlobalExceptionHandler.execute(() -> {
            dishService.startOrStop(id, status);
            return R.ok();
        });
    }

    /** GET /user/dish/list?categoryId=xx */
    public R<List<Dish>> list(Long categoryId) {
        return GlobalExceptionHandler.execute(() -> R.ok(dishService.listByCategory(categoryId)));
    }
}
