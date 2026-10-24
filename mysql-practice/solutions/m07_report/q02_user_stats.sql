-- ================================================================
-- m07-02 用户统计：每日新增 + 累计总数
-- ================================================================
-- 题目：统计 2024-05-01 到 2024-05-07 每一天的新增用户数，以及截止到当天（含当天）的用户总数。
-- 注意总数要包含 05-01 之前注册的老用户。
-- 返回列：stat_date, new_users, total_users
-- 排序：按 stat_date 升序（结果顺序必须一致）
-- 提示：累计值可以用窗口函数 SUM(...) OVER (ORDER BY ...)，也可以用相关子查询
--
-- 期望结果：
-- | stat_date | new_users | total_users |
-- | 2024-05-01 | 2 | 4 |
-- | 2024-05-02 | 0 | 4 |
-- | 2024-05-03 | 1 | 5 |
-- | 2024-05-04 | 0 | 5 |
-- | 2024-05-05 | 1 | 6 |
-- | 2024-05-06 | 1 | 7 |
-- | 2024-05-07 | 0 | 7 |
-- （7 行）
-- ================================================================

-- 在下面写你的 SQL：
WITH RECURSIVE dates AS (
    SELECT DATE '2024-05-01' AS d
    UNION ALL
    SELECT d + INTERVAL 1 DAY FROM dates WHERE d < '2024-05-07'
),
daily AS (
    SELECT dates.d, COUNT(u.id) AS new_users
    FROM dates
             LEFT JOIN user u
                       ON u.create_time >= dates.d AND u.create_time < dates.d + INTERVAL 1 DAY
    GROUP BY dates.d
)
SELECT d AS stat_date,
       new_users,
       SUM(new_users) OVER (ORDER BY d)
           + (SELECT COUNT(*) FROM user WHERE create_time < '2024-05-01') AS total_users
FROM daily
ORDER BY d;

-- 【讲解】
-- 一个 WITH 里可以定义多个 CTE，用逗号分隔，后面的可以引用前面的。
-- 窗口函数 SUM(x) OVER (ORDER BY d)：按 d 排序后，每一行的值 = 从第一行加到当前行（累计和），
-- 和 GROUP BY 不同，它不会把多行合并成一行。
