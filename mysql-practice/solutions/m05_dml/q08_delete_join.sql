-- ================================================================
-- m05-08 多表关联删除
-- ================================================================
-- 题目：删除所有【停售】菜品的口味数据（dish_flavor 表）。
--
-- 判题方式：测试会在全新导入的示例数据上执行你的 SQL，然后查询表里的数据，检查是否修改正确、有没有误伤其他数据。检查点：停售菜品（回锅肉、柠檬茶）的口味被删掉，其他菜的口味还在。
--
-- 提示：可以用子查询 WHERE dish_id IN (...)，也可以用 MySQL 的多表 DELETE：DELETE f FROM dish_flavor f JOIN dish d ON ... WHERE ...
-- ================================================================

-- 在下面写你的 SQL：
DELETE f
FROM dish_flavor f
         JOIN dish d ON d.id = f.dish_id
WHERE d.status = 0;

-- 【讲解】
-- 多表 DELETE 时，DELETE 后面写的是要删除数据的那张表的别名（这里只删 f，不删 d）。
-- 子查询写法：DELETE FROM dish_flavor WHERE dish_id IN (SELECT id FROM dish WHERE status = 0);
-- 同理还有多表 UPDATE：UPDATE a JOIN b ON ... SET a.x = b.y WHERE ...
