-- ================================================================
-- m04-13 连接 + 分组：各分类销量
-- ================================================================
-- 题目：统计每个菜品分类在【已完成】订单中卖出了多少份（只统计单点的菜品，不统计套餐，即 order_detail.dish_id 不为 NULL）。
-- 没卖出过的分类不用显示。
-- 返回列：category_name, sold_count
-- 排序：按 sold_count 降序，相同按分类 id 升序（结果顺序必须一致）
-- 提示：orders → order_detail → dish → category，四张表
--
-- 期望结果：
-- | category_name | sold_count |
-- | 主食 | 28 |
-- | 热菜 | 17 |
-- | 饮品 | 8 |
-- | 凉菜 | 3 |
-- （4 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT c.name AS category_name, SUM(od.number) AS sold_count
FROM orders o
         JOIN order_detail od ON od.order_id = o.id
         JOIN dish d ON d.id = od.dish_id
         JOIN category c ON c.id = d.category_id
WHERE o.status = 5
GROUP BY c.id, c.name
ORDER BY sold_count DESC, c.id;

-- 【讲解】
-- JOIN dish d ON d.id = od.dish_id 时，套餐明细的 dish_id 是 NULL，匹配不上任何菜品，自然被内连接过滤掉了，
-- 所以不需要再写 od.dish_id IS NOT NULL。
