-- ================================================================
-- m01-02 创建菜品表：金额与时间类型
-- ================================================================
-- 题目：创建表 dish，字段如下：
-- id          BIGINT，主键，自增
-- name        VARCHAR(32)，不能为空，不能重复
-- category_id BIGINT，不能为空
-- price       DECIMAL(10, 2)，不能为空（金额：最多 10 位数字，其中 2 位小数）
-- status      TINYINT，不能为空，默认值 1
-- description VARCHAR(255)，可以为空
-- create_time DATETIME，不能为空，默认值为当前时间
--
-- 判题方式：测试会在一个全新的空数据库 learn_sql_ddl 里执行你的 SQL，然后插入各种数据，检查约束是否生效。
-- 检查点：price 存 28.5 读出来是 28.50，存 12.345 会四舍五入为 12.35，超过 8 位整数会报错；
-- 不填 create_time 时自动填入当前时间；description 可以不填。
--
-- 提示：当前时间默认值：DEFAULT CURRENT_TIMESTAMP
-- ================================================================

-- 在下面写你的 SQL：
CREATE TABLE dish (
         id          BIGINT         NOT NULL AUTO_INCREMENT,
         name        VARCHAR(32)    NOT NULL,
         category_id BIGINT         NOT NULL,
         price       DECIMAL(10, 2) NOT NULL,
         status      TINYINT        NOT NULL DEFAULT 1,
         description VARCHAR(255),
         create_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
         PRIMARY KEY (id),
         UNIQUE KEY uk_name (name)
     ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 【讲解】
-- 金额一律用 DECIMAL，不要用 FLOAT / DOUBLE（和 Java 里用 BigDecimal 不用 double 是同一个道理）。
-- DECIMAL(10, 2)：总共 10 位数字，小数占 2 位，所以整数部分最多 8 位。
-- 常用类型选择：
-- 主键 / 外键 id → BIGINT（对应 Java 的 Long）
-- 状态、类型这种小整数 → TINYINT（对应 Integer）
-- 短文本 → VARCHAR(n)，长文本 → TEXT
-- 日期时间 → DATETIME（对应 LocalDateTime），只有日期 → DATE（对应 LocalDate）
-- 真实项目里 create_time / update_time 一般由程序自动填充（MyBatis-Plus 的自动填充功能），这里练的是数据库层面的默认值。
