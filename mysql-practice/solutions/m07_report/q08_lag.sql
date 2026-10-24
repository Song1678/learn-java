-- ================================================================
-- m07-08 窗口函数 LAG：和前一天比
-- ================================================================
-- 题目：在 m07-01 的基础上，再显示前一天的营业额和“当天 - 前一天”的差额。
-- 第一天（05-01）没有前一天，这两列显示 NULL。
-- 返回列：order_date, turnover, prev_turnover, diff
-- 排序：按 order_date 升序（结果顺序必须一致）
-- 提示：LAG(turnover) OVER (ORDER BY order_date) 取上一行的值；可以把 m07-01 的查询放进一个 CTE
--
-- 期望结果：
-- | order_date | turnover | prev_turnover | diff |
-- | 2024-05-01 | 150.00 | NULL | NULL |
-- | 2024-05-02 | 185.00 | 150.00 | 35.00 |
-- | 2024-05-03 | 99.00 | 185.00 | -86.00 |
-- | 2024-05-04 | 0.00 | 99.00 | -99.00 |
-- | 2024-05-05 | 182.00 | 0.00 | 182.00 |
-- | 2024-05-06 | 170.00 | 182.00 | -12.00 |
-- | 2024-05-07 | 40.00 | 170.00 | -130.00 |
-- （7 行）
-- ================================================================

-- 在下面写你的 SQL：
WITH RECURSIVE dates AS (
    SELECT DATE '2024-05-01' AS d
    UNION ALL
    SELECT d + INTERVAL 1 DAY FROM dates WHERE d < '2024-05-07'
),
daily AS (
    SELECT dates.d AS order_date, IFNULL(SUM(o.amount), 0) AS turnover
    FROM dates
             LEFT JOIN orders o
                       ON o.order_time >= dates.d
                           AND o.order_time < dates.d + INTERVAL 1 DAY
                           AND o.status = 5
    GROUP BY dates.d
)
SELECT order_date,
       turnover,
       LAG(turnover) OVER (ORDER BY order_date) AS prev_turnover,
       turnover - LAG(turnover) OVER (ORDER BY order_date) AS diff
FROM daily
ORDER BY order_date;

-- 【讲解】
-- LAG(列, n) 取前 n 行的值（默认 1），LEAD 取后 n 行。做“环比”“同比”报表很方便。
-- NULL 参与加减运算，结果还是 NULL，所以第一天的 diff 自然是 NULL。
