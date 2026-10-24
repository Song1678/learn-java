-- ================================================================
-- m06-02 创建索引
-- ================================================================
-- 题目：在 orders 表上创建两个索引：
-- 1. 订单号 number 的唯一索引，名字叫 uk_number
-- 2. user_id 和 order_time 的联合索引（user_id 在前），名字叫 idx_user_time
--
-- 判题方式：测试检查两个索引存在、列的顺序正确、uk_number 是唯一索引，并用 EXPLAIN 确认按订单号查询时用上了 uk_number。
--
-- 提示：CREATE UNIQUE INDEX 名字 ON 表 (列)；CREATE INDEX 名字 ON 表 (列1, 列2)
-- ================================================================

-- 在下面写你的 SQL：
CREATE UNIQUE INDEX uk_number ON orders (number);
CREATE INDEX idx_user_time ON orders (user_id, order_time);

-- 【讲解】
-- 索引就像书的目录：没有索引时查一条数据要从头到尾扫描整张表（全表扫描），有了索引可以快速定位。
-- 代价：占用空间，并且插入/修改数据时要同时维护索引，所以不是越多越好。
-- 一般给 经常出现在 WHERE、JOIN ON、ORDER BY 里的列 建索引。
-- 主键自动就有索引（聚簇索引）；UNIQUE 约束也会自动创建唯一索引。
-- 也可以写成：ALTER TABLE orders ADD UNIQUE INDEX uk_number (number);
