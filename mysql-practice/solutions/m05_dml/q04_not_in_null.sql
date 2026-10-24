-- ================================================================
-- m05-04 NOT IN 遇到 NULL 的坑
-- ================================================================
-- 题目：把“在【已完成】订单里从来没有被单点过”的【起售中】菜品全部停售（status 改为 0）。
-- “单点过”指：已完成订单的 order_detail 里有这道菜的记录（套餐明细的 dish_id 是 NULL，不算单点）。
--
-- 判题方式：测试会在全新导入的示例数据上执行你的 SQL，然后查询表里的数据，检查是否修改正确、有没有误伤其他数据。检查点：只有“夫妻肺片”被停售（它只出现在已取消和未完成的订单里）；其他菜的状态都不变。
--
-- 提示：先试试 WHERE id NOT IN (SELECT od.dish_id FROM order_detail od JOIN orders o ... WHERE o.status = 5)，
-- 看看为什么一行都没改。提示：套餐明细的 dish_id 是 NULL
-- ================================================================

-- 在下面写你的 SQL：
UPDATE dish
SET status = 0
WHERE status = 1
  AND id NOT IN (SELECT od.dish_id
                 FROM order_detail od
                          JOIN orders o ON o.id = od.order_id
                 WHERE o.status = 5
                   AND od.dish_id IS NOT NULL);

-- 【讲解】
-- x NOT IN (1, 2, NULL) 等价于 x <> 1 AND x <> 2 AND x <> NULL，而 x <> NULL 永远是 NULL（不是真），
-- 所以整个条件永远不成立，一行都匹配不上！
-- 两种修正方法：子查询里排除 NULL（答案的写法），或者改用 NOT EXISTS（不受 NULL 影响，更推荐）：
-- WHERE NOT EXISTS (SELECT 1 FROM order_detail od JOIN orders o ON o.id = od.order_id
-- WHERE o.status = 5 AND od.dish_id = dish.id)
