

# 智能记账系统 (Smart Consumption Bookkeeping)

一个基于Java的轻量级个人财务管理应用，支持收入管理、支出跟踪、预算规划和统计分析。

## 功能特性

### 用户管理
- 用户注册与登录
- 管理员与普通用户角色分离
- 个人信息管理（姓名、学号、性别、年龄、联系方式等）
- 管理员可管理系统用户

### 财务管理
- 记录收入（来源、金额、日期、描述）
- 记录支出（类别、金额、日期、描述）
- 按时间范围查询收支记录
- 分类统计支出情况

### 预算管理
- 按月份和类别设置预算
- 预算执行情况分析
- 超预算警告提醒
- 预算进度可视化

### 数据统计
- 收支概览统计
- 分类支出统计
- 预算执行分析
- 系统整体数据统计（管理员功能）

## 技术栈

- **编程语言**: Java
- **数据库**: MySQL
- **构建工具**: 无（纯Java项目）
- **运行环境**: JDK 8+

## 项目结构

```
src/
├── Main.java                           # 程序入口，主菜单逻辑
├── com/smartconsumption/
│   ├── dao/                            # 数据访问层
│   │   ├── BudgetDAO.java
│   │   ├── ExpenseDAO.java
│   │   ├── IncomeDAO.java
│   │   └── UserDAO.java
│   ├── entity/                         # 实体类
│   │   ├── Budget.java
│   │   ├── Expense.java
│   │   ├── Income.java
│   │   └── User.java
│   ├── service/                        # 业务逻辑层
│   │   ├── BudgetService.java
│   │   ├── ExpenseService.java
│   │   ├── IncomeService.java
│   │   └── UserService.java
│   └── util/                           # 工具类
│       ├── DatabaseConnection.java     # 数据库连接管理
│       ├── InputUtil.java              # 输入验证工具
│       └── UserExitException.java      # 用户退出异常
```

## 快速开始

### 环境要求

- JDK 8 或更高版本
- MySQL 5.7+
- Maven（可选）

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE bookkeeping;
```

2. 修改 `DatabaseConnection.java` 中的数据库连接配置：
```java
private static final String URL = "jdbc:mysql://localhost:3306/bookkeeping";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

3. 运行程序后会自动创建所需的数据表。

### 编译与运行

```bash
# 编译
javac -d bin src/**/*.java

# 运行
java -cp bin Main
```

或使用IDE（如IntelliJ IDEA、Eclipse）直接导入项目运行。

## 使用指南

### 首次使用

1. 启动程序后，系统会自动创建一个默认管理员账户
2. 使用管理员账号登录进行系统管理
3. 普通用户需先注册后登录

### 菜单说明

#### 主菜单
- **1. 登录**：用户登录
- **2. 注册**：新用户注册
- **3. 退出**：退出程序

#### 用户菜单（普通用户）
- **1. 个人信息管理**：修改个人资料
- **2. 收入管理**：添加/查看/删除收入记录
- **3. 支出管理**：添加/查看/删除支出记录
- **4. 预算管理**：设置/查看/修改/删除预算
- **5. 统计分析**：查看收支统计和预算执行情况
- **6. 退出登录**：返回主菜单

#### 管理员菜单
- **1. 用户管理**：查看/添加/删除/搜索用户
- **2. 创建管理员**：提升用户为管理员
- **3. 个人信息管理**：修改个人资料
- **4. 统计分析**：查看系统整体统计
- **5. 退出登录**：返回主菜单

### 常用操作

#### 记录收入
1. 进入「收入管理」→「添加收入」
2. 输入金额、来源、日期、描述
3. 系统自动保存记录

#### 记录支出
1. 进入「支出管理」→「添加支出」
2. 输入金额、类别、日期、描述
3. 系统会自动检查是否超出预算

#### 设置预算
1. 进入「预算管理」→「添加预算」
2. 选择月份和类别
3. 输入预算金额
4. 系统将跟踪该类别的支出情况

## 数据字段说明

### 用户 (User)
| 字段 | 类型 | 说明 |
|------|------|------|
| username | String | 用户名（唯一） |
| password | String | 密码 |
| name | String | 真实姓名 |
| studentId | String | 学号 |
| gender | String | 性别 |
| age | int | 年龄 |
| phone | String | 联系电话 |
| email | String | 邮箱 |
| role | String | 角色（admin/user） |

### 收入 (Income)
| 字段 | 类型 | 说明 |
|------|------|------|
| amount | BigDecimal | 金额 |
| source | String | 来源 |
| incomeDate | LocalDate | 收入日期 |
| description | String | 描述 |

### 支出 (Expense)
| 字段 | 类型 | 说明 |
|------|------|------|
| amount | BigDecimal | 金额 |
| category | String | 类别 |
| expenseDate | LocalDate | 支出日期 |
| description | String | 描述 |

### 预算 (Budget)
| 字段 | 类型 | 说明 |
|------|------|------|
| amount | BigDecimal | 预算金额 |
| category | String | 预算类别 |
| monthYear | String | 月份（格式：yyyy-MM） |

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

## 联系方式

- 项目地址：https://gitee.com/chill109/bookkeeping
- 问题反馈：请在项目 Issues 页面提交