-- ================================================================
-- m01-03 外键约束
-- ================================================================
-- 题目：创建两张表，并用外键保证“订单明细必须属于一个存在的订单”：
-- orders：
-- id     BIGINT，主键，自增
-- number VARCHAR(50)，不能为空
-- order_detail：
-- id       BIGINT，主键，自增
-- order_id BIGINT，不能为空，外键，引用 orders(id)
-- name     VARCHAR(32)，不能为空
-- number   INT，不能为空，默认值 1
--
-- 判题方式：测试会在一个全新的空数据库 learn_sql_ddl 里执行你的 SQL，然后插入各种数据，检查约束是否生效。
-- 检查点：明细的 order_id 指向不存在的订单时插入报错；订单下还有明细时，删除订单报错。
--
-- 提示：CONSTRAINT 约束名 FOREIGN KEY (order_id) REFERENCES orders (id)；注意要先建被引用的 orders 表
-- ================================================================

-- 在下面写你的 SQL：
CREATE TABLE orders (
         id     BIGINT      NOT NULL AUTO_INCREMENT,
         number VARCHAR(50) NOT NULL,
         PRIMARY KEY (id)
     ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

     CREATE TABLE order_detail (
         id       BIGINT      NOT NULL AUTO_INCREMENT,
         order_id BIGINT      NOT NULL,
         name     VARCHAR(32) NOT NULL,
         number   INT         NOT NULL DEFAULT 1,
         PRIMARY KEY (id),
         CONSTRAINT fk_detail_order FOREIGN KEY (order_id) REFERENCES orders (id)
     ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 【讲解】
-- 外键能在数据库层面保证数据一致，但会影响插入/删除性能，分库分表时也很麻烦，
-- 所以互联网项目（包括苍穹外卖、RuoYi-Vue-Plus）通常【不建外键】，由 Java 代码保证一致性
-- （比如 learn-java ch09 里“分类下有菜品时不能删除”的检查）。
-- 但外键的概念必须懂：它描述的就是表和表之间的关联关系，写 JOIN 时用的就是这些字段。
