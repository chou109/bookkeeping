# Smart Consumption Bookkeeping System

A lightweight personal finance management application based on Java, supporting income management, expense tracking, budget planning, and statistical analysis.

## Features

### User Management
- User registration and login
- Separation of administrator and regular user roles
- Personal information management (name, student ID, gender, age, contact information, etc.)
- Administrators can manage system users

### Financial Management
- Record income (source, amount, date, description)
- Record expenses (category, amount, date, description)
- Query income and expense records by time range
- Categorized expense statistics

### Budget Management
- Set budgets by month and category
- Analysis of budget execution
- Over-budget alert notifications
- Visualized budget progress

### Data Statistics
- Income and expense overview
- Categorized expense statistics
- Budget execution analysis
- Overall system data statistics (administrator feature)

## Technology Stack

- **Programming Language**: Java
- **Database**: MySQL
- **Build Tool**: None (pure Java project)
- **Runtime Environment**: JDK 8+

## Project Structure

```
src/
├── Main.java                           # Program entry point, main menu logic
├── com/smartconsumption/
│   ├── dao/                            # Data Access Layer
│   │   ├── BudgetDAO.java
│   │   ├── ExpenseDAO.java
│   │   ├── IncomeDAO.java
│   │   └── UserDAO.java
│   ├── entity/                         # Entity classes
│   │   ├── Budget.java
│   │   ├── Expense.java
│   │   ├── Income.java
│   │   └── User.java
│   ├── service/                        # Business Logic Layer
│   │   ├── BudgetService.java
│   │   ├── ExpenseService.java
│   │   ├── IncomeService.java
│   │   └── UserService.java
│   └── util/                           # Utility classes
│       ├── DatabaseConnection.java     # Database connection management
│       ├── InputUtil.java              # Input validation utility
│       └── UserExitException.java      # User exit exception
```

## Quick Start

### Prerequisites

- JDK 8 or higher
- MySQL 5.7+
- Maven (optional)

### Database Configuration

1. Create the database:
```sql
CREATE DATABASE bookkeeping;
```

2. Modify the database connection settings in `DatabaseConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/bookkeeping";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

3. After running the program, required database tables will be created automatically.

### Compilation and Execution

```bash
# Compile
javac -d bin src/**/*.java

# Run
java -cp bin Main
```

Alternatively, import the project directly into an IDE (such as IntelliJ IDEA or Eclipse) and run it.

## Usage Guide

### First-Time Setup

1. Upon launching the program, a default administrator account will be created automatically.
2. Log in with the administrator account to perform system management.
3. Regular users must register before logging in.

### Menu Overview

#### Main Menu
- **1. Login**: User login
- **2. Register**: New user registration
- **3. Exit**: Exit the program

#### User Menu (Regular User)
- **1. Personal Information Management**: Edit personal profile
- **2. Income Management**: Add/view/delete income records
- **3. Expense Management**: Add/view/delete expense records
- **4. Budget Management**: Set/view/modify/delete budgets
- **5. Statistical Analysis**: View income/expense statistics and budget execution
- **6. Logout**: Return to main menu

#### Administrator Menu
- **1. User Management**: View/add/delete/search users
- **2. Create Administrator**: Promote a user to administrator
- **3. Personal Information Management**: Edit personal profile
- **4. Statistical Analysis**: View overall system statistics
- **5. Logout**: Return to main menu

### Common Operations

#### Recording Income
1. Go to 'Income Management' → 'Add Income'
2. Enter amount, source, date, and description
3. The system automatically saves the record

#### Recording Expenses
1. Go to 'Expense Management' → 'Add Expense'
2. Enter amount, category, date, and description
3. The system automatically checks whether the budget has been exceeded

#### Setting a Budget
1. Go to 'Budget Management' → 'Add Budget'
2. Select month and category
3. Enter budget amount
4. The system will track spending for that category

## Data Field Definitions

### User (User)
| Field | Type | Description |
|-------|------|-------------|
| username | String | Username (unique) |
| password | String | Password |
| name | String | Real name |
| studentId | String | Student ID |
| gender | String | Gender |
| age | int | Age |
| phone | String | Phone number |
| email | String | Email |
| role | String | Role (admin/user) |

### Income (Income)
| Field | Type | Description |
|-------|------|-------------|
| amount | BigDecimal | Amount |
| source | String | Source |
| incomeDate | LocalDate | Income date |
| description | String | Description |

### Expense (Expense)
| Field | Type | Description |
|-------|------|-------------|
| amount | BigDecimal | Amount |
| category | String | Category |
| expenseDate | LocalDate | Expense date |
| description | String | Description |

### Budget (Budget)
| Field | Type | Description |
|-------|------|-------------|
| amount | BigDecimal | Budget amount |
| category | String | Budget category |
| monthYear | String | Month (format: yyyy-MM) |

## Contribution Guidelines

1. Fork this repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Submit a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

- Project URL: https://gitee.com/chill109/bookkeeping
- Feedback: Please submit issues via the project's Issues page