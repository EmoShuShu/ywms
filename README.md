# YWMS - 工单管理系统后端 (Work Order Management System)

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JPA](https://img.shields.io/badge/JPA-Hibernate-blue.svg)](https://hibernate.org/)
[![MySQL](https://img.shields.io/badge/Database-MySQL%208-blue.svg)](https://www.mysql.com/)

这是一个基于 Spring Boot 实现的工单管理系统后端服务。它提供了一套完整的 RESTful API，用于处理用户认证、工单的创建、审批、流转、完成和统计等核心业务流程。

## 1. 主要功能

- **用户管理**: 支持多角色（申请者、审批人员、操作人员）用户登录、登出。
- **工单流转**:
  - **创建与修改**: 申请者可以提交、修改、撤回工单。
  - **多级审批**: 支持区、市、省三级审批流程。
  - **派送与执行**: 操作人员可以接收工单并派送到具体执行部门。
- **回单系统**: 操作人员可以提交工单处理结果的回单。
- **数据统计**: 提供按周、月、年维度的工单数据统计报告。

## 2. 技术栈

- **核心框架**: Spring Boot
- **数据访问**: Spring Data JPA / Hibernate
- **数据库**: MySQL 8.x
- **构建工具**: Maven
- **会话管理**: HTTP Session
- **API 文档**: Apifox

## 3. 运行项目前的准备工作

在运行本项目前，请确保你的开发环境已安装以下软件：

- **JDK 17** 或更高版本。
- **MySQL 8.x**。
- **Maven 3.6+**。
- **IDE**: 推荐使用 IntelliJ IDEA，并确保已安装和启用 **Lombok** 插件。

## 4. 数据库设置

1.  在你的本地 MySQL 服务中创建一个新的数据库。推荐使用 `utf8mb4` 字符集。
    ```sql
    CREATE DATABASE ywms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```
2.  **(重要)** 使用该数据库，并执行项目根目录下的 `database/schema.sql` 文件来创建所有必需的数据表。
3.  **(推荐)** 为了方便测试，可以执行 `database/data.sql` 文件来插入一些初始的测试数据（比如一个申请者、一个区级审批员、一个操作员等）。

## 5. 项目配置

1.  打开项目的核心配置文件：`src/main/resources/application.properties`。
2.  根据你本地的 MySQL 设置，**修改以下数据库连接信息**：

    ```properties
    # 数据库连接
    spring.datasource.url=jdbc:mysql://localhost:3306/ywms_db?useSSL=false&serverTimezone=Asia/Shanghai
    spring.datasource.username=[你的数据库用户名, 例如: root]
    spring.datasource.password=[你的数据库密码]
    ```

## 6. 如何运行项目

#### 方式一：使用 IDE (推荐)
1.  使用 IntelliJ IDEA 或 Eclipse 将项目作为 Maven 项目导入。
2.  等待 Maven 下载完所有依赖。
3.  找到主启动类 `src/main/java/com/ywms/YwmsApplication.java`。
4.  右键点击 -> `Run 'YwmsApplication'`.

#### 方式二：使用 Maven 命令行
1.  在项目根目录下打开终端。
2.  执行以下命令：
    ```bash
    mvn spring-boot:run
    ```

当你在控制台看到 `Tomcat started on port(s): 8080` 的日志时，代表后端服务已成功启动！服务地址为 `http://localhost:8080`。

## 7. API 文档

所有 API 的详细信息、请求/响应格式、参数说明和在线测试功能，请访问我们的 Apifox 在线文档：

https://apifox.com/apidoc/shared-ee9e492d-f45b-46e1-b6c7-c155e30333ee

> **测试流程提醒**:
> 1.  所有需要登录的接口，都必须先调用 `POST /api/users/login` 接口。
> 2.  Apifox 会自动处理 Cookie，登录一次后，其他接口即可正常调用。