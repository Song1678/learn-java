-- ================================================================
-- m03-12 按月统计 DATE_FORMAT
-- ================================================================
-- 题目：统计每个月【已完成】订单的营业额。月份显示成 2024-05 这种格式。
-- 返回列：month, turnover
-- 排序：按 month 升序（结果顺序必须一致）
-- 提示：DATE_FORMAT(order_time, '%Y-%m')
--
-- 期望结果：
-- | month | turnover |
-- | 2024-04 | 92.00 |
-- | 2024-05 | 948.00 |
-- （2 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT DATE_FORMAT(order_time, '%Y-%m') AS month, SUM(amount) AS turnover
FROM orders
WHERE status = 5
GROUP BY DATE_FORMAT(order_time, '%Y-%m')
ORDER BY month;

-- 【讲解】
-- 格式符：%Y 四位年份，%m 两位月份，%d 两位日期，%H 小时（24 小时制），%i 分钟，%s 秒。注意分钟是 %i 不是 %m。
