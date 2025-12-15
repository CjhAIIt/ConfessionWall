# ConfessionWall 项目 README

# ConfessionWall 表白墙 Web 应用

ConfessionWall 是一个基于 Spring Boot 和 MyBatis 构建的轻量级表白墙 Web 应用，为用户提供表白内容的发布、查看与管理功能，旨在搭建一个简单高效的社交互动平台。

## 📋 项目概述

本项目聚焦于核心社交需求，用户可在平台上自由发布表白内容，同时支持对已发布内容的查看与删除操作。后端采用 Spring Boot 框架实现业务逻辑与 RESTful API 构建，通过 MyBatis 完成数据持久化操作，数据存储于数据库中，支持开发测试与生产环境的灵活适配。

## 📂 目录结构

```plaintext

ConfessionWall/
├── .idea
├── .mvn/
│   ├── maven-wrapper.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── confessionwall/
│   │   │   │   │   ├── Confession.java       // 表白实体类
│   │   │   │   │   ├── ConfessionController.java // 控制器类
│   │   │   │   │   └── ConfessionMapper.java    // MyBatis映射器
│   │   └── resources/                          // 资源文件目录
│   └── test/                                   // 测试代码目录
├── target/                                     // 构建输出目录
├── .gitattributes
└── README.md                                   // 项目说明文档
```

## 🔧 核心功能与关键类说明

### 1. 实体类：Confession.java

作为表白数据的载体，映射数据库表结构，存储表白相关核心信息，主要包含以下字段及对应的 getter/setter 方法：

- `id`：表白记录唯一标识（Long类型）

- `content`：表白内容（String类型）

- `createdAt`：发布时间戳（LocalDateTime类型）

该类是数据在业务层与持久层之间传输的核心模型。

### 2. 控制器：ConfessionController.java

Spring Boot 核心控制器，负责接收并处理前端 HTTP 请求，通过依赖注入 ConfessionMapper 完成与数据库的交互，提供三大核心接口：

- GET /confessions：查询所有表白记录并返回

- POST /confessions：接收前端提交的表白数据，完成新增操作

- DELETE /confessions/{id}：根据唯一标识 id，删除指定表白记录

### 3. 数据访问层：ConfessionMapper.java

MyBatis 映射器接口，定义了与数据库交互的核心 SQL 操作方法，由 MyBatis 自动生成实现类，支持表白数据的查询、插入与删除：

- `findAll()`：查询数据库中所有表白记录，返回 Confession 列表

- `insert(Confession confession)`：将表白实体数据插入数据库

- `delete(Long id)`：根据 id 删除对应表白记录

## ⚙️ 技术栈

- **后端框架**：Spring Boot（快速构建 RESTful API，简化配置）

- **数据访问**：MyBatis（轻量级 ORM 框架，优化数据库操作）

- **数据库**：H2 Database（嵌入式内存数据库，适用于开发与测试，支持无缝切换至 MySQL 等生产数据库）

- **构建工具**：Maven（依赖管理与项目构建自动化）

## 🚀 安装与使用步骤

### 1. 克隆项目

```bash

git clone https://github.com/yourusername/ConfessionWall.git
```

### 2. 进入项目目录并构建

```bash

cd ConfessionWall
mvn clean install
```

执行上述命令后，Maven 会自动下载依赖并完成项目编译构建，构建结果输出至 target 目录。

### 3. 运行 Spring Boot 应用

```bash

mvn spring-boot:run
```

### 4. 访问应用

打开浏览器，输入以下地址即可进入表白墙页面，开始使用各项功能：

```plaintext

http://localhost:8080
```

## ⚙️ 配置说明

项目核心配置文件位于 `src/main/resources/application.properties`，可根据实际需求修改数据库连接、服务端口等参数。以下是默认 H2 数据库配置示例：

```properties

# 数据库配置
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# 可选：修改服务端口
# server.port=8081
```

若需切换至 MySQL 等数据库，只需修改上述 `spring.datasource` 相关配置，并在 pom.xml 中添加对应数据库驱动依赖。

## �代码示例

### 1. Confession.java

```java

import java.time.LocalDateTime;

public class Confession {
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
```

### 2. ConfessionController.java

```java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/confessions")
public class ConfessionController {

    @Autowired
    private ConfessionMapper confessionMapper;

    /**
     * 获取所有表白记录
     */
    @GetMapping
    public List<Confession> getAllConfessions() {
        return confessionMapper.findAll();
    }

    /**
     * 新增表白记录
     */
    @PostMapping
    public void addConfession(@RequestBody Confession confession) {
        // 可在此处添加时间戳自动填充等逻辑
        confessionMapper.insert(confession);
    }

    /**
     * 根据ID删除表白记录
     */
    @DeleteMapping("/{id}")
    public void deleteConfession(@PathVariable Long id) {
        confessionMapper.delete(id);
    }
}
```

### 3. ConfessionMapper.java

```java

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ConfessionMapper {
    /**
     * 查询所有表白
     */
    List<Confession> findAll();

    /**
     * 插入表白记录
     */
    void insert(Confession confession);

    /**
     * 根据ID删除表白
     */
    void delete(Long id);
}
```

## 🤝 贡献指南

欢迎各位开发者参与项目优化，提交问题报告、功能建议或代码贡献，贡献步骤如下：

1. Fork 本仓库至你的 GitHub 账号

2. 克隆你 Fork 后的仓库到本地：`git clone https://github.com/your-username/ConfessionWall.git`

3. 创建新的开发分支：`git checkout -b feature/your-feature-name`

4. 完成代码修改并提交：`git commit -m "Add: 新增XX功能"`

5. 将分支推送到你的远程仓库：`git push origin feature/your-feature-name`

6. 在 GitHub 上发起 Pull Request，描述你的修改内容与目的，等待审核合并

## 📄 许可证

本项目采用 **MIT 许可证** 进行开源，允许自由使用、修改与分发，详情请查阅项目根目录下的 `LICENSE` 文件。
> （注：文档部分内容可能由 AI 生成）
