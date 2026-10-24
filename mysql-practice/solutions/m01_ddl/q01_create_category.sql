-- ================================================================
-- m01-01 创建分类表
-- ================================================================
-- 题目：创建表 category，字段如下：
-- id          BIGINT，主键，自增
-- type        TINYINT，不能为空（1 菜品分类，2 套餐分类）
-- name        VARCHAR(32)，不能为空，不能重复
-- sort        INT，不能为空，默认值 0
-- status      TINYINT，不能为空，默认值 1
-- create_time DATETIME，可以为空
-- 字符集使用 utf8mb4。
--
-- 判题方式：测试会在一个全新的空数据库 learn_sql_ddl 里执行你的 SQL，然后插入各种数据，检查约束是否生效。
-- 检查点：插入时 id 自动生成；不填 sort、status 时取默认值；name 重复、name 为空、不填 type、name 超过 32 个字符都会报错。
--
-- 提示：可以参考 sql/schema.sql 里的写法，但请先自己写一遍
-- ================================================================

-- 在下面写你的 SQL：
CREATE TABLE category (
         id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
         type        TINYINT     NOT NULL COMMENT '类型 1菜品分类 2套餐分类',
         name        VARCHAR(32) NOT NULL COMMENT '分类名称',
         sort        INT         NOT NULL DEFAULT 0 COMMENT '排序',
         status      TINYINT     NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
         create_time DATETIME COMMENT '创建时间',
         PRIMARY KEY (id),
         UNIQUE KEY uk_name (name)
     ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '分类';

-- 【讲解】
-- PRIMARY KEY 主键：唯一标识一行，不能重复也不能为空；AUTO_INCREMENT 让 MySQL 自动生成 1、2、3……
-- UNIQUE KEY 唯一约束：这一列的值不能重复（可以有多个 NULL）。
-- NOT NULL + DEFAULT：插入时不给值就用默认值；没有默认值又不给值会报错（严格模式下）。
-- VARCHAR(32) 的 32 是“字符数”，不是字节数，32 个汉字也能存下。
-- COMMENT 是注释，强烈建议每个字段都写 —— RuoYi 的代码生成器会把它当成字段的中文名。
