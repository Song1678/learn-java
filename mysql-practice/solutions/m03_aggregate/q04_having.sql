-- ================================================================
-- m03-04 分组后过滤 HAVING
-- ================================================================
-- 题目：找出【起售中】菜品数量大于等于 4 的分类。
-- 返回列：category_id, dish_count
-- 排序：按 category_id 升序（结果顺序必须一致）
-- 提示：WHERE 在分组之前过滤行，HAVING 在分组之后过滤组
--
-- 期望结果：
-- | category_id | dish_count |
-- | 1 | 6 |
-- | 2 | 4 |
-- | 3 | 4 |
-- （3 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT category_id, COUNT(*) AS dish_count
FROM dish
WHERE status = 1
GROUP BY category_id
HAVING COUNT(*) >= 4
ORDER BY category_id;

-- 【讲解】
-- 执行顺序：FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT。
-- 所以“只统计起售菜品”写在 WHERE，“数量 >= 4”只能写在 HAVING（WHERE 执行时还没有分组，没法 COUNT）。
