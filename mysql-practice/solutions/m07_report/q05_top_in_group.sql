-- ================================================================
-- m07-05 窗口函数：每个分类最贵的菜
-- ================================================================
-- 题目：查询每个菜品分类中价格最高的【起售中】菜品（每个分类只取 1 道，本题数据中没有并列）。
-- 返回列：category_name, dish_name, price
-- 排序：按分类 id 升序（结果顺序必须一致）
-- 提示：ROW_NUMBER() OVER (PARTITION BY category_id ORDER BY price DESC) 给每个分类内部的菜排名
--
-- 期望结果：
-- | category_name | dish_name | price |
-- | 热菜 | 水煮鱼 | 58.00 |
-- | 凉菜 | 夫妻肺片 | 36.00 |
-- | 主食 | 担担面 | 16.00 |
-- | 饮品 | 鲜榨橙汁 | 18.00 |
-- （4 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT c.name AS category_name, t.name AS dish_name, t.price
FROM (SELECT d.*,
             ROW_NUMBER() OVER (PARTITION BY d.category_id ORDER BY d.price DESC) AS rn
      FROM dish d
      WHERE d.status = 1) t
         JOIN category c ON c.id = t.category_id
WHERE t.rn = 1
ORDER BY c.id;

-- 【讲解】
-- “每组取前 N 名”是窗口函数最典型的用法：PARTITION BY 分组（但不合并行），ORDER BY 决定组内顺序。
-- ROW_NUMBER 并列时也会排出 1、2；RANK 并列时都是 1，下一个是 3；DENSE_RANK 并列时都是 1，下一个是 2。
-- 窗口函数是 MySQL 8 才有的功能，5.7 不支持。
