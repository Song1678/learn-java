-- ================================================================
-- m04-04 三表连接
-- ================================================================
-- 题目：查询 id = 3 的订单的明细：订单号、下单用户姓名、菜品名称、数量、小计（单价 × 数量）。
-- 返回列：order_number, user_name, dish_name, number, subtotal
-- 排序：按订单明细 id 升序（结果顺序必须一致）
-- 提示：orders 连 user（谁下的单），orders 连 order_detail（买了什么）
--
-- 期望结果：
-- | order_number | user_name | dish_name | number | subtotal |
-- | 2024050111300003 | 张三 | 宫保鸡丁 | 2 | 56.00 |
-- | 2024050111300003 | 张三 | 米饭 | 2 | 4.00 |
-- | 2024050111300003 | 张三 | 可乐 | 2 | 10.00 |
-- （3 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT o.number AS order_number,
       u.name AS user_name,
       od.name AS dish_name,
       od.number,
       od.amount * od.number AS subtotal
FROM orders o
         JOIN user u ON o.user_id = u.id
         JOIN order_detail od ON od.order_id = o.id
WHERE o.id = 3
ORDER BY od.id;

-- 【讲解】
-- 多表连接就是一个 JOIN 接一个 JOIN。写之前先想清楚每两张表之间靠哪个字段关联（画一下 ER 图，见 docs/表结构说明.md）。
