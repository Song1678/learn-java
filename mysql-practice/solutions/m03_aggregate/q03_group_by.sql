-- ================================================================
-- m03-03 分组 GROUP BY
-- ================================================================
-- 题目：统计每个分类下有多少道菜品（包括停售的）。
-- 返回列：category_id, dish_count
-- 排序：按 category_id 升序（结果顺序必须一致）
--
-- 期望结果：
-- | category_id | dish_count |
-- | 1 | 7 |
-- | 2 | 5 |
-- | 3 | 4 |
-- | 4 | 4 |
-- （4 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT category_id, COUNT(*) AS dish_count
FROM dish
GROUP BY category_id
ORDER BY category_id;

-- 【讲解】
-- GROUP BY 把 category_id 相同的行分成一组，每组算一次 COUNT(*)，每组输出一行。
-- 规则（ONLY_FULL_GROUP_BY）：SELECT 里出现的列，要么写在 GROUP BY 里，要么放在聚合函数里。
-- 比如 SELECT category_id, name, COUNT(*) ... GROUP BY category_id 会报错 —— 一组里有好几个 name，MySQL 不知道显示哪个。
