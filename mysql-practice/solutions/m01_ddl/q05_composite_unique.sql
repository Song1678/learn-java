-- ================================================================
-- m01-05 联合唯一约束
-- ================================================================
-- 题目：创建购物车表 shopping_cart，要求同一个用户的购物车里同一道菜只能有一条记录（数量用 number 累加）：
-- id      BIGINT，主键，自增
-- user_id BIGINT，不能为空
-- dish_id BIGINT，不能为空
-- number  INT，不能为空，默认值 1
--
-- 判题方式：测试会在一个全新的空数据库 learn_sql_ddl 里执行你的 SQL，然后插入各种数据，检查约束是否生效。
-- 检查点：(用户1, 菜1)、(用户1, 菜2)、(用户2, 菜1) 都能插入；再插入 (用户1, 菜1) 报错。
--
-- 提示：联合唯一约束：UNIQUE KEY 约束名 (列1, 列2)，表示两列的【组合】不能重复
-- ================================================================

-- 在下面写你的 SQL：
CREATE TABLE shopping_cart (
         id      BIGINT NOT NULL AUTO_INCREMENT,
         user_id BIGINT NOT NULL,
         dish_id BIGINT NOT NULL,
         number  INT    NOT NULL DEFAULT 1,
         PRIMARY KEY (id),
         UNIQUE KEY uk_user_dish (user_id, dish_id)
     ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 【讲解】
-- 有了联合唯一约束，“加入购物车”可以写成一条 SQL：
-- INSERT INTO shopping_cart (user_id, dish_id) VALUES (1, 1) ON DUPLICATE KEY UPDATE number = number + 1;
-- 不存在就插入，已存在就数量 +1。同时也防止了并发时插入两条重复记录。
