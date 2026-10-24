-- ================================================================
-- m04-10 EXISTS 子查询
-- ================================================================
-- 题目：查询包含【停售】菜品的套餐（苍穹外卖规定这种套餐不能起售）。
-- 返回列：id, name
-- 排序：按 id 升序（结果顺序必须一致）
--
-- 期望结果：
-- | id | name |
-- | 3 | 回锅肉套餐 |
-- （1 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT s.id, s.name
FROM setmeal s
WHERE EXISTS (SELECT 1
              FROM setmeal_dish sd
                       JOIN dish d ON d.id = sd.dish_id
              WHERE sd.setmeal_id = s.id
                AND d.status = 0)
ORDER BY s.id;

-- 【讲解】
-- EXISTS 只关心“有没有”，所以子查询里习惯写 SELECT 1，写 SELECT * 结果也一样。
