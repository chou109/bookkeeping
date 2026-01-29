# 大学生智能消费记账系统

> 一个基于 Java 控制台的智能消费记账系统，专为大学生设计，帮助用户记录收支、设置预算、进行消费分析，培养良好的财务管理习惯。

## 📋 项目简介

随着高等教育的普及，大学生消费市场日益繁荣，但多数学生缺乏系统的财务管理经验，普遍存在“月光族”、无计划消费等问题。本项目旨在运用面向对象编程思想和 Java 框架技术，构建一个功能完善、操作简便、具有智能提醒与统计分析的消费管理系统。

系统围绕“记账-预算-分析-优化”的财务管理闭环设计，提供全方位的消费管理解决方案。

## ✨ 主要功能

- **用户管理**：支持管理员与普通用户双角色体系，实现注册、登录、个人信息维护及权限控制。
- **收支管理**：记录收入与支出，支持自定义类别，并对金额、日期进行严格验证。
- **预算管理**：支持按月、按类别设置预算，提供三级预警机制（超出、用完、接近），实时监控预算执行情况。
- **统计分析**：提供收支汇总、类别占比分析、预算执行率计算及消费趋势分析。
- **管理员功能**：管理用户信息、查看系统统计数据，具备用户增删改查及搜索能力。

## 🛠️ 技术栈

- **开发语言**：Java 8+
- **数据库**：MySQL 8.0
- **连接驱动**：mysql-connector-java 8.0.13
- **构建工具**：Maven
- **开发环境**：IntelliJ IDEA / Eclipse
- **架构模式**：三层架构（表现层、业务逻辑层、数据访问层）

## 🗂️ 项目结构

```
com.smartconsumption/
├── entity/          # 实体类 (User, Income, Expense, Budget)
├── dao/             # 数据访问对象 (UserDAO, IncomeDAO, ExpenseDAO, BudgetDAO)
├── service/         # 业务逻辑层 (UserService, IncomeService, ExpenseService, BudgetService)
├── util/            # 工具类 (数据库连接、输入验证、自定义异常)
└── Main.java        # 程序入口
```

## 🚀 快速开始

### 环境准备

1. 安装 JDK 8 或以上版本。
2. 安装 MySQL 8.0 或以上版本，并启动服务。
3. 下载项目代码。

### 数据库初始化

1. 创建数据库：
    ```sql
    CREATE DATABASE IF NOT EXISTS smart_consumption_system
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```
2. 执行项目根目录下的 SQL 脚本（通常为 `init.sql`），创建表结构并初始化必要数据。

### 配置与运行

1. 在 `DatabaseConnection.java` 中配置数据库连接信息（URL、用户名、密码）。
2. 将 `mysql-connector-java-8.0.13.jar` 添加到项目依赖中。
3. 编译并运行主类 `Main.java`。

## 📊 核心设计亮点

- **统一异常退出机制**：通过自定义 `UserExitException` 实现跨层级的优雅菜单退出。
- **链式输入验证**：`InputUtil` 工具类提供类型、格式、业务规则等多级验证。
- **状态驱动菜单**：系统根据当前登录用户角色动态显示对应功能菜单。
- **预算三级预警**：实时计算预算执行率，并通过颜色编码（红/黄/默认）直观展示状态。

## 🧪 测试与性能

- **功能测试**：核心模块（用户、收支、预算）完成度 100%，管理员功能完成度 90%。
- **性能指标**：启动时间 <2秒，单表查询响应 <100ms，支持基础并发操作。
- **稳定性**：通过压力测试与 72 小时连续运行验证。

## 📈 未来规划

- **功能扩展**：增加数据导入/导出（Excel/CSV）、多维度可视化图表、智能消费提醒。
- **技术升级**：迁移至 Spring Boot 开发 Web 版本，支持移动端与云端部署。
- **性能优化**：引入缓存机制、数据库读写分离、异步处理等。

## 📚 项目文档

- [详细设计报告](https://gitee.com/chill109/bookkeeping/blob/main/docs/设计报告.md) - 包含完整的需求分析、架构设计、数据库说明及实现细节。
- 用户手册与管理员指南（见 `docs/` 目录）。

## 🙌 贡献与支持

项目已开源，欢迎提交 Issue 或 Pull Request。

**仓库地址**：[https://gitee.com/chill109/bookkeeping.git](https://gitee.com/chill109/bookkeeping.git)
或[https://github.com/chou109/bookkeeping.git](https://github.com/chou109/bookkeeping.git)

## 📄 许可证


本项目遵循 MIT 许可证。详见 [LICENSE](LICENSE) 文件。
