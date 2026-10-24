-- ================================================================
-- m01-04 修改表结构 ALTER TABLE
-- ================================================================
-- 题目：测试会先创建这样一张表，并插入一行数据：
-- CREATE TABLE employee (
-- id       BIGINT AUTO_INCREMENT PRIMARY KEY,
-- name     VARCHAR(16),
-- username VARCHAR(32) NOT NULL,
-- age      INT
-- );
-- 请写 SQL 完成以下修改（已有数据不能丢）：
-- 1. 增加一列 phone VARCHAR(11)，可以为空
-- 2. 把 name 改成 VARCHAR(32)，并且不能为空
-- 3. 给 username 加唯一约束
-- 4. 删除 age 列
--
-- 判题方式：测试先建好上面的表，再执行你的 SQL，然后检查：原来的数据还在；phone 列可用；32 个字符的 name 能存下；name 为空报错；username 重复报错；age 列不存在了。
--
-- 提示：ALTER TABLE 表名 ADD COLUMN ... / MODIFY COLUMN ... / ADD UNIQUE KEY 名字 (列) / DROP COLUMN ...
-- ================================================================

-- 在下面写你的 SQL：
ALTER TABLE employee ADD COLUMN phone VARCHAR(11) COMMENT '手机号';
     ALTER TABLE employee MODIFY COLUMN name VARCHAR(32) NOT NULL COMMENT '姓名';
     ALTER TABLE employee ADD UNIQUE KEY uk_username (username);
     ALTER TABLE employee DROP COLUMN age;

-- 【讲解】
-- MODIFY COLUMN 要把列的完整定义重新写一遍（类型、NOT NULL、DEFAULT、COMMENT），漏写的会丢失。
-- 改列名用 CHANGE COLUMN 旧名 新名 类型...。
-- 线上的表改结构要格外小心：大表 ALTER 可能锁表很久；已有 NULL 数据时改成 NOT NULL 会失败。
-- 多个修改也可以写在一条语句里：ALTER TABLE employee ADD ..., MODIFY ..., DROP ...;
