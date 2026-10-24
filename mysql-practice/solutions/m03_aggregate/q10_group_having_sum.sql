-- ================================================================
-- m03-10 分组求和 + HAVING
-- ================================================================
-- 题目：统计每个用户【已完成】（status = 5）订单的数量和消费总额，只显示消费总额大于等于 100 元的用户。
-- 返回列：user_id, order_count, total_amount
-- 排序：按 total_amount 降序，相同按 user_id 升序（结果顺序必须一致）
--
-- 期望结果：
-- | user_id | order_count | total_amount |
-- | 5 | 4 | 250.00 |
-- | 4 | 3 | 176.00 |
-- | 1 | 4 | 175.00 |
-- | 2 | 3 | 147.00 |
-- | 6 | 2 | 126.00 |
-- （5 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT user_id, COUNT(*) AS order_count, SUM(amount) AS total_amount
FROM orders
WHERE status = 5
GROUP BY user_id
HAVING SUM(amount) >= 100
ORDER BY total_amount DESC, user_id;

-- 【讲解】
-- HAVING 里可以直接写聚合函数；ORDER BY 可以用 SELECT 里的别名（因为 ORDER BY 在 SELECT 之后执行）。
