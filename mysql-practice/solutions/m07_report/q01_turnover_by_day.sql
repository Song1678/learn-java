-- ================================================================
-- m07-01 每日营业额（没有订单的日期显示 0）
-- ================================================================
-- 题目：统计 2024-05-01 到 2024-05-07 每一天【已完成】订单的营业额。
-- 没有订单的日期（比如 05-04）也要显示，营业额为 0。
-- 这是苍穹外卖“营业额统计”折线图接口的核心 SQL。
-- 返回列：order_date, turnover
-- 排序：按 order_date 升序（结果顺序必须一致）
-- 提示：先用递归 CTE 生成 7 个日期：
-- WITH RECURSIVE dates AS (
-- SELECT DATE '2024-05-01' AS d
-- UNION ALL
-- SELECT d + INTERVAL 1 DAY FROM dates WHERE d < '2024-05-07'
-- )
-- 再用 dates LEFT JOIN orders
--
-- 期望结果：
-- | order_date | turnover |
-- | 2024-05-01 | 150.00 |
-- | 2024-05-02 | 185.00 |
-- | 2024-05-03 | 99.00 |
-- | 2024-05-04 | 0.00 |
-- | 2024-05-05 | 182.00 |
-- | 2024-05-06 | 170.00 |
-- | 2024-05-07 | 40.00 |
-- （7 行）
-- ================================================================

-- 在下面写你的 SQL：
WITH RECURSIVE dates AS (
    SELECT DATE '2024-05-01' AS d
    UNION ALL
    SELECT d + INTERVAL 1 DAY FROM dates WHERE d < '2024-05-07'
)
SELECT dates.d AS order_date,
       IFNULL(SUM(o.amount), 0) AS turnover
FROM dates
         LEFT JOIN orders o
                   ON o.order_time >= dates.d
                       AND o.order_time < dates.d + INTERVAL 1 DAY
                       AND o.status = 5
GROUP BY dates.d
ORDER BY dates.d;

-- 【讲解】
-- WITH 定义一个临时结果集（CTE，公共表表达式），后面可以像表一样使用；RECURSIVE 表示它可以引用自己，
-- 从 05-01 开始一直 +1 天直到 05-07。
-- 苍穹外卖原版是在 Java 里循环日期、每天查一次 SQL；用一条 SQL 查出来效率更高。
-- 两种做法都要会：在 Java 里生成日期列表（learn-java ch08 的 datesBetween），或在 SQL 里生成。
