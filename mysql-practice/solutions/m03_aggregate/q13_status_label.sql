-- ================================================================
-- m03-13 状态分布（分组 + CASE）
-- ================================================================
-- 题目：统计每种订单状态有多少个订单，并显示状态名称：
-- 1 待付款，2 待接单，3 已接单，4 派送中，5 已完成，6 已取消。
-- 返回列：status, status_label, order_count
-- 排序：按 status 升序（结果顺序必须一致）
--
-- 期望结果：
-- | status | status_label | order_count |
-- | 1 | 待付款 | 2 |
-- | 2 | 待接单 | 1 |
-- | 3 | 已接单 | 1 |
-- | 4 | 派送中 | 1 |
-- | 5 | 已完成 | 19 |
-- | 6 | 已取消 | 4 |
-- （6 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT status,
       CASE status
           WHEN 1 THEN '待付款'
           WHEN 2 THEN '待接单'
           WHEN 3 THEN '已接单'
           WHEN 4 THEN '派送中'
           WHEN 5 THEN '已完成'
           WHEN 6 THEN '已取消'
       END AS status_label,
       COUNT(*) AS order_count
FROM orders
GROUP BY status
ORDER BY status;

-- 【讲解】
-- CASE 列 WHEN 值 THEN ... 是“简单 CASE”写法，适合按某一列的值逐个匹配。
-- 真实项目里状态名称一般不写在 SQL 里，而是放在 Java 枚举或 RuoYi 的“字典管理”里，由前端翻译。
