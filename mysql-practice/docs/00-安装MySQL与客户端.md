# 00 安装 MySQL 与客户端

## 1. 安装 MySQL 8.0

### Windows

1. 打开 <https://dev.mysql.com/downloads/installer/>，下载 **MySQL Installer for Windows**（选体积大的那个离线安装包即可，不需要登录，点 “No thanks, just start my download”）
2. 安装类型选 **Server only**（或 Custom 只勾 MySQL Server 8.0）
3. 一路 Next，到 **Authentication Method** 选推荐的 “Use Strong Password Encryption”
4. **Accounts and Roles**：设置 root 密码。练习用的话设成 `123456` 最省事（和 db.properties 默认值一致），设别的就记得改 db.properties
5. **Windows Service**：保持勾选 “Start the MySQL Server at System Startup”，服务名默认 `MySQL80`
6. 安装完成后，在开始菜单打开 **MySQL 8.0 Command Line Client**，输入密码，看到 `mysql>` 就成功了

> 以后 MySQL 没启动：Win + R 输入 `services.msc`，找到 `MySQL80`，右键“启动”。

### Mac

```bash
brew install mysql@8.0
brew services start mysql@8.0
# 第一次需要设置 root 密码（按提示操作，密码设为 123456 或者改 db.properties）
$(brew --prefix mysql@8.0)/bin/mysql_secure_installation
```

### 验证

```bash
mysql -uroot -p
# 输入密码后执行：
SELECT VERSION();          -- 应该是 8.0.x
SHOW VARIABLES LIKE 'character_set_server';   -- 应该是 utf8mb4
```

> 本项目只用 MySQL 8 的语法（窗口函数、递归 CTE 等），**不支持 MySQL 5.7**。

## 2. 安装图形客户端（选一个）

| 客户端 | 说明 |
|---|---|
| **DBeaver Community** | 免费，功能够用，推荐 <https://dbeaver.io/download/> |
| **IDEA 旗舰版自带的 Database 面板** | 公司如果给了 IDEA Ultimate，直接用它，写 SQL 有补全 |
| DataGrip | JetBrains 出品，收费（学生免费） |
| Navicat | 很多公司在用，收费 |

### 用 DBeaver 连接

1. 左上角 “新建连接” → 选 MySQL → 下一步
2. 主机 `localhost`，端口 `3306`，用户名 `root`，密码 `123456`
3. 第一次会提示下载驱动，点“下载”
4. 如果报错 `Public Key Retrieval is not allowed`：在“驱动属性”里把 `allowPublicKeyRetrieval` 改成 `true`
5. 点“测试连接”，成功后“完成”

## 3. 导入示例数据，自己玩

测试会自动建库导数据，但**写 SQL 时建议在客户端里先跑通**。在客户端里执行：

```sql
CREATE DATABASE takeout DEFAULT CHARSET utf8mb4;
USE takeout;
```

然后打开 `sql/schema.sql` 全部执行，再打开 `sql/data.sql` 全部执行（DBeaver：Alt + X 执行整个脚本）。
之后你就可以在 `takeout` 库里随便查、随便改，改乱了重新执行这两个文件即可。

## 4. 让测试连上你的 MySQL

修改 `mysql-practice/db.properties`：

```properties
db.host=localhost
db.port=3306
db.username=root
db.password=你的密码
db.database=learn_sql_test
```

然后运行 `mvn test -Psolutions`（用参考答案跑一遍），全部通过说明环境 OK。
连不上的话，测试会打印“连接不上 MySQL……请检查”的提示，按提示排查。

## 5. 命令行常用操作（了解即可）

```sql
SHOW DATABASES;            -- 有哪些库
USE takeout;               -- 切换库
SHOW TABLES;               -- 当前库有哪些表
DESC dish;                 -- 看表结构
SHOW CREATE TABLE dish;    -- 看建表语句
SOURCE /path/to/data.sql;  -- 执行一个 SQL 文件
```
