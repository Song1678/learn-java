package com.learn.ch09_takeout;

import com.learn.ch09_takeout.common.StatusConstant;
import com.learn.ch09_takeout.domain.Category;
import com.learn.ch09_takeout.domain.Dish;
import com.learn.ch09_takeout.dto.DishDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch09 练习9.2~9.3：分类与菜品")
class CategoryAndDishTest extends TakeoutTestBase {

    @Test
    @DisplayName("9.2 分类新增 / 删除 / 列表")
    void category() {
        Long a = categoryService.add("主食", 2);
        Long b = categoryService.add("川菜", 1);
        Long c = categoryService.add("饮品", 2);
        assertServiceError("分类名称不能为空", () -> categoryService.add(" ", 1));
        assertServiceError("分类名称已存在", () -> categoryService.add("川菜", 9));

        Category cat = categoryMapper.selectById(c);
        cat.setStatus(StatusConstant.DISABLE);
        categoryMapper.updateById(cat);
        assertEquals(List.of(b, a), categoryService.listEnabled().stream().map(Category::getId).toList());

        dishService.add(new DishDTO("宫保鸡丁", b, bd("28"), null));
        assertServiceError("当前分类关联了菜品，不能删除", () -> categoryService.delete(b));
        assertServiceError("分类不存在", () -> categoryService.delete(99L));
        categoryService.delete(a);
        assertNull(categoryMapper.selectById(a));
    }

    @Test
    @DisplayName("9.3 新增菜品校验")
    void addDish() {
        Long cat = categoryService.add("川菜", 1);
        Long id = dishService.add(new DishDTO("宫保鸡丁", cat, bd("28"), "好吃"));
        Dish dish = dishService.getById(id);
        assertEquals("宫保鸡丁", dish.getName());
        assertEquals("好吃", dish.getDescription());
        assertEquals(StatusConstant.DISABLE, dish.getStatus(), "新菜品默认停售");

        assertServiceError("菜品名称不能为空", () -> dishService.add(new DishDTO("", cat, bd("1"), null)));
        assertServiceError("菜品价格必须大于0", () -> dishService.add(new DishDTO("凉面", cat, bd("0"), null)));
        assertServiceError("菜品价格必须大于0", () -> dishService.add(new DishDTO("凉面", cat, null, null)));
        assertServiceError("分类不存在", () -> dishService.add(new DishDTO("凉面", 99L, bd("1"), null)));
        assertServiceError("菜品名称已存在", () -> dishService.add(new DishDTO("宫保鸡丁", cat, bd("1"), null)));
        assertServiceError("菜品不存在", () -> dishService.getById(99L));
    }

    @Test
    @DisplayName("9.3 起售停售 / 批量删除 / 用户端列表")
    void dishOps() {
        Long cat = categoryService.add("川菜", 1);
        Long other = categoryService.add("主食", 2);
        Long a = enabledDish(cat, "水煮鱼", "58");
        Long b = enabledDish(cat, "宫保鸡丁", "28");
        Long c = enabledDish(cat, "鱼香肉丝", "28");
        Long d = dishService.add(new DishDTO("回锅肉", cat, bd("30"), null)); // 停售
        enabledDish(other, "米饭", "2");

        assertEquals(List.of(b, c, a), dishService.listByCategory(cat).stream().map(Dish::getId).toList());

        assertServiceError("状态不合法", () -> dishService.startOrStop(a, 2));
        assertServiceError("菜品不存在", () -> dishService.startOrStop(99L, 1));

        assertServiceError("起售中的菜品不能删除", () -> dishService.deleteBatch(List.of(d, a)));
        assertNotNull(dishMapper.selectById(d), "校验失败时一个都不能删");
        assertServiceError("菜品不存在", () -> dishService.deleteBatch(List.of(d, 99L)));
        assertNotNull(dishMapper.selectById(d));

        dishService.startOrStop(a, StatusConstant.DISABLE);
        dishService.deleteBatch(List.of(a, d));
        assertNull(dishMapper.selectById(a));
        assertNull(dishMapper.selectById(d));
        assertEquals(List.of(b, c), dishService.listByCategory(cat).stream().map(Dish::getId).toList());
    }
}
