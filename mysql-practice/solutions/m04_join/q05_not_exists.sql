-- ================================================================
-- m04-05 子查询 NOT EXISTS
-- ================================================================
-- 题目：查询从来没有下过单的用户。
-- 返回列：id, name
-- 排序：按 id 升序（结果顺序必须一致）
-- 提示：NOT EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id)
--
-- 期望结果：
-- | id | name |
-- | 8 | 郑十 |
-- （1 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT u.id, u.name
FROM user u
WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id)
ORDER BY u.id;

-- 【讲解】
-- EXISTS (子查询)：子查询能查到至少一行就为真。子查询里引用了外层的 u.id，叫“相关子查询”。
-- 等价写法：
-- 1. WHERE u.id NOT IN (SELECT user_id FROM orders) —— 注意子查询结果里有 NULL 时 NOT IN 会出问题（m05-04）
-- 2. LEFT JOIN orders o ON o.user_id = u.id WHERE o.id IS NULL
