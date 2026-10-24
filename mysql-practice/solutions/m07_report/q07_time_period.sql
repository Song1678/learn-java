-- ================================================================
-- m07-07 下单时段分布
-- ================================================================
-- 题目：统计【已完成】订单在各时段的数量（统计全部日期）：
-- 11:00 ~ 13:59 为“午餐”，17:00 ~ 20:59 为“晚餐”，其他时间为“其他”。没有订单的时段不用显示。
-- 返回列：period, order_count
-- 排序：按 order_count 降序（结果顺序必须一致）
-- 提示：HOUR(order_time) 取出小时，再用 CASE 划分
--
-- 期望结果：
-- | period | order_count |
-- | 午餐 | 13 |
-- | 晚餐 | 6 |
-- （2 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT CASE
           WHEN HOUR(order_time) BETWEEN 11 AND 13 THEN '午餐'
           WHEN HOUR(order_time) BETWEEN 17 AND 20 THEN '晚餐'
           ELSE '其他'
       END AS period,
       COUNT(*) AS order_count
FROM orders
WHERE status = 5
GROUP BY period
ORDER BY order_count DESC;

-- 【讲解】
-- GROUP BY 可以直接用 CASE 表达式的别名（MySQL 扩展），也可以把整个 CASE 表达式再写一遍。
