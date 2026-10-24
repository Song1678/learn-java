-- ================================================================
-- m04-02 左连接 + 分组计数
-- ================================================================
-- 题目：统计每个【菜品分类】（category.type = 1）下的菜品数量，没有菜品的分类也要显示，数量为 0。
-- 返回列：category_name, dish_count
-- 排序：按分类的 sort 升序（结果顺序必须一致）
-- 提示：LEFT JOIN 保留左表全部行；COUNT(*) 和 COUNT(d.id) 在这里结果不一样
--
-- 期望结果：
-- | category_name | dish_count |
-- | 热菜 | 7 |
-- | 凉菜 | 5 |
-- | 主食 | 4 |
-- | 饮品 | 4 |
-- | 汤羹 | 0 |
-- | 甜品 | 0 |
-- （6 行）
-- ================================================================

-- 在下面写你的 SQL：
SELECT c.name AS category_name, COUNT(d.id) AS dish_count
FROM category c
         LEFT JOIN dish d ON d.category_id = c.id
WHERE c.type = 1
GROUP BY c.id, c.name, c.sort
ORDER BY c.sort;

-- 【讲解】
-- LEFT JOIN：左表（category）的每一行都保留，右表（dish）匹配不上时，右表的列全部是 NULL。
-- 没有菜品的分类连出来是一行“全 NULL 的 dish”，COUNT(*) 会算成 1，COUNT(d.id) 不统计 NULL，才是正确的 0。
-- ORDER BY c.sort 用到了 sort，所以 GROUP BY 里也要带上（c.id 是主键，其实 MySQL 能推断出来，写上更清楚）。
