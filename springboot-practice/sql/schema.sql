-- =====================================================================
-- 迷你外卖数据库表结构（苍穹外卖简化版），和 mysql-practice 的 sql/schema.sql 基本一样，
-- 只给 category / dish / setmeal 加了 update_time，给 dish 加了逻辑删除标志 del_flag（MyBatis-Plus 章节要用）。
--
-- 什么时候会执行本文件：
--   1. 启动项目时（application.yml 里 spring.sql.init.mode=always）：重置 learn_web 库，所以每次重启数据都会恢复成示例数据
--   2. 跑测试时：每个测试方法之前重置 learn_web_test 库
--
-- 和苍穹外卖一样，表之间【没有】建外键约束（互联网项目的常见做法，关联关系由代码保证），
-- 外键的写法在 m01 建表练习里单独练。
-- =====================================================================

DROP TABLE IF EXISTS order_detail;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS shopping_cart;
DROP TABLE IF EXISTS address_book;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS setmeal_dish;
DROP TABLE IF EXISTS setmeal;
DROP TABLE IF EXISTS dish_flavor;
DROP TABLE IF EXISTS dish;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS employee;

-- 员工（后台管理系统的登录账号）
CREATE TABLE employee (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(32) NOT NULL COMMENT '姓名',
    username    VARCHAR(32) NOT NULL COMMENT '登录账号',
    password    VARCHAR(64) NOT NULL COMMENT '密码（真实项目里存的是加密后的值）',
    phone       VARCHAR(11)          COMMENT '手机号',
    status      TINYINT     NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
    create_time DATETIME             COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工';

-- 分类：type=1 菜品分类，type=2 套餐分类
CREATE TABLE category (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    type        TINYINT     NOT NULL COMMENT '类型 1菜品分类 2套餐分类',
    name        VARCHAR(32) NOT NULL COMMENT '分类名称',
    sort        INT         NOT NULL DEFAULT 0 COMMENT '排序，越小越靠前',
    status      TINYINT     NOT NULL DEFAULT 1 COMMENT '状态 1启用 0禁用',
    create_time DATETIME             COMMENT '创建时间',
    update_time DATETIME             COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分类';

-- 菜品
CREATE TABLE dish (
    id          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(32)   NOT NULL COMMENT '菜品名称',
    category_id BIGINT        NOT NULL COMMENT '分类id',
    price       DECIMAL(10, 2) NOT NULL COMMENT '价格',
    status      TINYINT       NOT NULL DEFAULT 1 COMMENT '状态 1起售 0停售',
    description VARCHAR(255)           COMMENT '描述',
    create_time DATETIME               COMMENT '创建时间',
    update_time DATETIME               COMMENT '更新时间',
    del_flag    TINYINT       NOT NULL DEFAULT 0 COMMENT '删除标志 0正常 1已删除（逻辑删除）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜品';

-- 菜品口味：一道菜可以有多种口味（一对多）
CREATE TABLE dish_flavor (
    id      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    dish_id BIGINT       NOT NULL COMMENT '菜品id',
    name    VARCHAR(32)  NOT NULL COMMENT '口味名称，例如 辣度',
    value   VARCHAR(255) NOT NULL COMMENT '可选值，例如 不辣,微辣,中辣',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜品口味';

-- 套餐
CREATE TABLE setmeal (
    id          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    category_id BIGINT         NOT NULL COMMENT '分类id',
    name        VARCHAR(32)    NOT NULL COMMENT '套餐名称',
    price       DECIMAL(10, 2) NOT NULL COMMENT '套餐价格',
    status      TINYINT        NOT NULL DEFAULT 1 COMMENT '状态 1起售 0停售',
    create_time DATETIME                COMMENT '创建时间',
    update_time DATETIME                COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '套餐';

-- 套餐包含哪些菜品（套餐 和 菜品 是多对多关系，这是中间表）
CREATE TABLE setmeal_dish (
    id         BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    setmeal_id BIGINT NOT NULL COMMENT '套餐id',
    dish_id    BIGINT NOT NULL COMMENT '菜品id',
    copies     INT    NOT NULL COMMENT '份数',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '套餐菜品关系';

-- C 端用户
CREATE TABLE user (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(32)          COMMENT '姓名',
    phone       VARCHAR(11)          COMMENT '手机号',
    sex         CHAR(1)              COMMENT '性别 1男 0女',
    create_time DATETIME             COMMENT '注册时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户';

-- 收货地址
CREATE TABLE address_book (
    id         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id    BIGINT       NOT NULL COMMENT '用户id',
    consignee  VARCHAR(50)  NOT NULL COMMENT '收货人',
    phone      VARCHAR(11)  NOT NULL COMMENT '手机号',
    detail     VARCHAR(255) NOT NULL COMMENT '详细地址',
    is_default TINYINT      NOT NULL DEFAULT 0 COMMENT '是否默认地址 1是 0否',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '地址簿';

-- 购物车：dish_id 和 setmeal_id 二选一
CREATE TABLE shopping_cart (
    id          BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id     BIGINT         NOT NULL COMMENT '用户id',
    dish_id     BIGINT                  COMMENT '菜品id',
    setmeal_id  BIGINT                  COMMENT '套餐id',
    name        VARCHAR(32)    NOT NULL COMMENT '菜品/套餐名称（冗余字段）',
    amount      DECIMAL(10, 2) NOT NULL COMMENT '单价',
    number      INT            NOT NULL DEFAULT 1 COMMENT '数量',
    create_time DATETIME                COMMENT '加入时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车';

-- 订单（order 是 SQL 关键字，所以表名叫 orders）
-- 状态：1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
CREATE TABLE orders (
    id              BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    number          VARCHAR(50)    NOT NULL COMMENT '订单号',
    status          INT            NOT NULL DEFAULT 1 COMMENT '订单状态',
    user_id         BIGINT         NOT NULL COMMENT '下单用户',
    address_book_id BIGINT                  COMMENT '收货地址id',
    order_time      DATETIME       NOT NULL COMMENT '下单时间',
    checkout_time   DATETIME                COMMENT '付款时间',
    amount          DECIMAL(10, 2) NOT NULL COMMENT '实收金额',
    remark          VARCHAR(100)            COMMENT '备注',
    cancel_reason   VARCHAR(255)            COMMENT '取消原因',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单';

-- 订单明细：dish_id 和 setmeal_id 二选一
CREATE TABLE order_detail (
    id         BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_id   BIGINT         NOT NULL COMMENT '订单id',
    dish_id    BIGINT                  COMMENT '菜品id',
    setmeal_id BIGINT                  COMMENT '套餐id',
    name       VARCHAR(32)    NOT NULL COMMENT '菜品/套餐名称',
    number     INT            NOT NULL COMMENT '数量',
    amount     DECIMAL(10, 2) NOT NULL COMMENT '单价',
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单明细';
