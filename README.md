# ConfessionWall 表白墙

基于 Spring Boot 4.0、MyBatis 和 MySQL 的简单表白墙，支持匿名发布与查看最新内容。前端由后端同源提供，默认访问地址为 `http://localhost:8080/`。

## 运行

需要 JDK 17+、Maven 和 MySQL。先创建 `confession_db` 数据库，在该数据库中手动执行 [database/schema.sql](database/schema.sql)。脚本面向新部署，不会自动迁移已有表；已有部署请检查用户名字段长度和字符集是否满足要求。

通过环境变量 `DB_PASSWORD` 配置数据库密码。需要调整数据库主机或用户时，设置 `SPRING_DATASOURCE_URL` 和 `SPRING_DATASOURCE_USERNAME`。仓库不再提供默认数据库密码，已有部署应轮换历史口令。

```bash
mvn spring-boot:run
```

前端使用 `/confession` 相对路径请求同源接口。反向代理应同时转发页面和 `/confession/**`；若部署在 URL 子目录下，需额外调整请求前缀。

## 实际接口

| 方法 | 路径 | 参数 |
| --- | --- | --- |
| POST | `/confession/add` | 表单 `username`、`message` |
| GET | `/confession/recent` | 查询参数 `limit`，默认 10，范围 1–100 |

用户名和内容均不能为空，用户名最多 100 个字符，内容最多 2000 个字符；长度按 Java 字符串长度计算。返回 JSON 使用 `code`、`msg`，列表结果额外包含 `data`。发布成功返回 HTTP 200，参数错误为 400，存储错误为 500，客户端不会收到数据库异常详情。

数据库字段为 `id`、`username`、`message`、`create_time`。MyBatis 已启用下划线到驼峰映射，用于向前端返回 `createTime`。当前没有删除接口、管理后台或 H2 配置，尚未声明开源许可证。

## 回归检查

```bash
mvn -Dfile.encoding=UTF-8 -Dtest=ConfessionControllerTest test
node --test tests/frontend.test.cjs
mvn -Dfile.encoding=UTF-8 -DskipTests package
```

控制器测试模拟业务服务，不依赖数据库。原有 `ConfessionWallApplicationTests` 会启动完整应用，需要配置可用的 MySQL；以上定向测试不能替代真实数据库联调。前端测试覆盖非法条数回退、错误展示和用户内容转义。

## 部署限制

当前允许匿名发布，且接口仍保留开放 CORS。公开部署前需要补充限流、内容审核、滥用防护和管理流程；本轮没有擅自增加账号系统。生产数据库使用专用账号并通过 HTTPS 提供服务。新增建表脚本尚未在真实 MySQL 上执行验证。
