-- ================================================================
-- m06-03 联合索引的最左前缀原则
-- ================================================================
-- 题目：测试会先在 orders 上创建联合索引 idx_user_time (user_id, order_time)。
-- 请写一个查询：查出用户 1 在 2024-05-03 及之后下的所有订单的 id、number，按 order_time 升序，
-- 要求这个查询【能用上】索引 idx_user_time。
--
-- 判题方式：测试检查查询结果是否正确，并对你的查询执行 EXPLAIN，检查 possible_keys 里包含 idx_user_time。
--
-- 提示：想想如果 WHERE 里只有 order_time 条件、没有 user_id，还能用上这个索引吗？
-- ================================================================

-- 在下面写你的 SQL：
SELECT id, number
FROM orders
WHERE user_id = 1
  AND order_time >= '2024-05-03'
ORDER BY order_time;

-- 【讲解】
-- 联合索引 (user_id, order_time) 相当于先按 user_id 排序，user_id 相同再按 order_time 排序（像电话簿先按姓再按名）。
-- 所以：
-- WHERE user_id = 1                              ✅ 能用
-- WHERE user_id = 1 AND order_time >= ...        ✅ 能用（两列都用上）
-- WHERE order_time >= ...                        ❌ 用不上（跳过了最左边的 user_id）
-- 这就是“最左前缀原则”。可以用 EXPLAIN 在每条 SQL 前面看看 key 列和 type 列。
