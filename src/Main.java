import com.smartconsumption.entity.*;
import com.smartconsumption.service.*;
import com.smartconsumption.util.InputUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService = new UserService();
    private static IncomeService incomeService = new IncomeService();
    private static ExpenseService expenseService = new ExpenseService();
    private static BudgetService budgetService = new BudgetService();

    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("=== 大学生智能消费记账系统 ===");

        while (true) {
            if (currentUser == null) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
    }

    // 主菜单（未登录状态）
    private static void showMainMenu() {
        System.out.println("\n=== 主菜单 ===");
        System.out.println("1. 用户注册");
        System.out.println("2. 用户登录");
        System.out.println("3. 退出系统");
        System.out.print("请选择操作: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                System.out.println("感谢使用大学生智能消费记账系统，再见！");
                System.exit(0);
                break;
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }

    // 用户菜单（已登录状态）
    private static void showUserMenu() {
        System.out.println("\n=== 用户菜单 (" + currentUser.getUsername() + ") ===");
        System.out.println("1. 个人信息管理");
        System.out.println("2. 收入管理");
        System.out.println("3. 支出管理");
        System.out.println("4. 预算管理");
        System.out.println("5. 统计报表");
        System.out.println("6. 退出登录");
        System.out.print("请选择操作: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                managePersonalInfo();
                break;
            case 2:
                manageIncome();
                break;
            case 3:
                manageExpense();
                break;
            case 4:
                manageBudget();
                break;
            case 5:
                showStatistics();
                break;
            case 6:
                currentUser = null;
                System.out.println("已成功退出登录！");
                break;
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }

    // 用户注册
    private static void register() {
        System.out.println("\n=== 用户注册 ===");

        String username = InputUtil.getString("请输入用户名: ");
        String password = InputUtil.getString("请输入密码: ");
        String name = InputUtil.getString("请输入姓名: ");
        String studentId = InputUtil.getString("请输入学号: ");
        String gender = InputUtil.getString("请输入性别 (男/女): ");
        int age = InputUtil.getInt("请输入年龄: ");
        String phone = InputUtil.getString("请输入电话: ");
        String email = InputUtil.getString("请输入邮箱: ");

        User user = new User(username, password, name, studentId, gender, age, phone, email);

        if (userService.register(user)) {
            System.out.println("注册成功！");
        } else {
            System.out.println("注册失败！");
        }
    }

    // 用户登录
    private static void login() {
        System.out.println("\n=== 用户登录 ===");

        String username = InputUtil.getString("请输入用户名: ");
        String password = InputUtil.getString("请输入密码: ");

        User user = userService.login(username, password);

        if (user != null) {
            currentUser = user;
            System.out.println("登录成功！欢迎 " + user.getName() + "！");
        } else {
            System.out.println("登录失败！用户名或密码错误！");
        }
    }

    // 个人信息管理
    private static void managePersonalInfo() {
        System.out.println("\n=== 个人信息管理 ===");
        System.out.println("1. 查看个人信息");
        System.out.println("2. 修改个人信息");
        System.out.print("请选择操作: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                System.out.println("\n=== 个人信息 ===");
                System.out.println(currentUser);
                break;
            case 2:
                updatePersonalInfo();
                break;
            default:
                System.out.println("无效的选择！");
        }
    }

    // 修改个人信息
    private static void updatePersonalInfo() {
        System.out.println("\n=== 修改个人信息 ===");

        String name = InputUtil.getString("请输入新姓名 (" + currentUser.getName() + "): ");
        String studentId = InputUtil.getString("请输入新学号 (" + currentUser.getStudentId() + "): ");
        String gender = InputUtil.getString("请输入新性别 (男/女) (" + currentUser.getGender() + "): ");
        int age = InputUtil.getInt("请输入新年龄 (" + currentUser.getAge() + "): ");
        String phone = InputUtil.getString("请输入新电话 (" + currentUser.getPhone() + "): ");
        String email = InputUtil.getString("请输入新邮箱 (" + currentUser.getEmail() + "): ");

        currentUser.setName(name);
        currentUser.setStudentId(studentId);
        currentUser.setGender(gender);
        currentUser.setAge(age);
        currentUser.setPhone(phone);
        currentUser.setEmail(email);

        if (userService.updateUser(currentUser)) {
            System.out.println("个人信息更新成功！");
        } else {
            System.out.println("个人信息更新失败！");
        }
    }

    // 收入管理
    private static void manageIncome() {
        System.out.println("\n=== 收入管理 ===");
        System.out.println("1. 添加收入记录");
        System.out.println("2. 查看收入记录");
        System.out.println("3. 删除收入记录");
        System.out.print("请选择操作: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                addIncome();
                break;
            case 2:
                viewIncomes();
                break;
            case 3:
                deleteIncome();
                break;
            default:
                System.out.println("无效的选择！");
        }
    }

    // 添加收入记录
    private static void addIncome() {
        System.out.println("\n=== 添加收入记录 ===");

        BigDecimal amount = InputUtil.getBigDecimal("请输入收入金额: ");
        String source = InputUtil.getString("请输入收入来源: ");
        LocalDate incomeDate = InputUtil.getDate("请输入收入日期");
        String description = InputUtil.getString("请输入收入描述 (可选): ");

        Income income = new Income(currentUser.getUserId(), amount, source, incomeDate, description);

        if (incomeService.addIncome(income)) {
            System.out.println("收入记录添加成功！");
        } else {
            System.out.println("收入记录添加失败！");
        }
    }

    // 查看收入记录
    private static void viewIncomes() {
        System.out.println("\n=== 查看收入记录 ===");
        System.out.println("1. 查看所有收入记录");
        System.out.println("2. 按日期范围查看收入记录");
        System.out.print("请选择操作: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        List<Income> incomes;

        switch (choice) {
            case 1:
                incomes = incomeService.getIncomesByUserId(currentUser.getUserId());
                break;
            case 2:
                LocalDate startDate = InputUtil.getDate("请输入开始日期");
                LocalDate endDate = InputUtil.getDate("请输入结束日期");
                incomes = incomeService.getIncomesByDateRange(currentUser.getUserId(), startDate, endDate);
                break;
            default:
                System.out.println("无效的选择！");
                return;
        }

        if (incomes.isEmpty()) {
            System.out.println("没有找到收入记录！");
        } else {
            System.out.println("\n=== 收入记录列表 ===");
            for (Income income : incomes) {
                System.out.println(income);
                System.out.println("-------------------");
            }

            BigDecimal total = incomeService.getTotalIncome(currentUser.getUserId());
            System.out.println("总收入: " + total);
        }
    }

    // 删除收入记录
    private static void deleteIncome() {
        System.out.println("\n=== 删除收入记录 ===");

        List<Income> incomes = incomeService.getIncomesByUserId(currentUser.getUserId());
        if (incomes.isEmpty()) {
            System.out.println("没有可删除的收入记录！");
            return;
        }

        System.out.println("\n=== 收入记录列表 ===");
        for (Income income : incomes) {
            System.out.println(income);
            System.out.println("-------------------");
        }

        int incomeId = InputUtil.getInt("请输入要删除的收入记录ID: ");

        if (incomeService.deleteIncome(incomeId)) {
            System.out.println("收入记录删除成功！");
        } else {
            System.out.println("收入记录删除失败！");
        }
    }

    // 支出管理
    private static void manageExpense() {
        System.out.println("\n=== 支出管理 ===");
        System.out.println("1. 添加支出记录");
        System.out.println("2. 查看支出记录");
        System.out.println("3. 删除支出记录");
        System.out.print("请选择操作: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                addExpense();
                break;
            case 2:
                viewExpenses();
                break;
            case 3:
                deleteExpense();
                break;
            default:
                System.out.println("无效的选择！");
        }
    }

    // 添加支出记录
    private static void addExpense() {
        System.out.println("\n=== 添加支出记录 ===");

        BigDecimal amount = InputUtil.getBigDecimal("请输入支出金额: ");
        String category = InputUtil.getString("请输入支出类别: ");
        LocalDate expenseDate = InputUtil.getDate("请输入支出日期");
        String description = InputUtil.getString("请输入支出描述 (可选): ");

        Expense expense = new Expense(currentUser.getUserId(), amount, category, expenseDate, description);

        if (expenseService.addExpense(expense)) {
            System.out.println("支出记录添加成功！");
        } else {
            System.out.println("支出记录添加失败！");
        }
    }

    // 查看支出记录
    private static void viewExpenses() {
        System.out.println("\n=== 查看支出记录 ===");
        System.out.println("1. 查看所有支出记录");
        System.out.println("2. 按日期范围查看支出记录");
        System.out.print("请选择操作: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        List<Expense> expenses;

        switch (choice) {
            case 1:
                expenses = expenseService.getExpensesByUserId(currentUser.getUserId());
                break;
            case 2:
                LocalDate startDate = InputUtil.getDate("请输入开始日期");
                LocalDate endDate = InputUtil.getDate("请输入结束日期");
                expenses = expenseService.getExpensesByDateRange(currentUser.getUserId(), startDate, endDate);
                break;
            default:
                System.out.println("无效的选择！");
                return;
        }

        if (expenses.isEmpty()) {
            System.out.println("没有找到支出记录！");
        } else {
            System.out.println("\n=== 支出记录列表 ===");
            for (Expense expense : expenses) {
                System.out.println(expense);
                System.out.println("-------------------");
            }

            BigDecimal total = expenseService.getTotalExpense(currentUser.getUserId());
            System.out.println("总支出: " + total);
        }
    }

    // 删除支出记录
    private static void deleteExpense() {
        System.out.println("\n=== 删除支出记录 ===");

        List<Expense> expenses = expenseService.getExpensesByUserId(currentUser.getUserId());
        if (expenses.isEmpty()) {
            System.out.println("没有可删除的支出记录！");
            return;
        }

        System.out.println("\n=== 支出记录列表 ===");
        for (Expense expense : expenses) {
            System.out.println(expense);
            System.out.println("-------------------");
        }

        int expenseId = InputUtil.getInt("请输入要删除的支出记录ID: ");

        if (expenseService.deleteExpense(expenseId)) {
            System.out.println("支出记录删除成功！");
        } else {
            System.out.println("支出记录删除失败！");
        }
    }

    // 预算管理
    private static void manageBudget() {
        System.out.println("\n=== 预算管理 ===");
        System.out.println("1. 添加预算");
        System.out.println("2. 查看预算");
        System.out.println("3. 修改预算");
        System.out.println("4. 删除预算");
        System.out.print("请选择操作: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                addBudget();
                break;
            case 2:
                viewBudgets();
                break;
            case 3:
                updateBudget();
                break;
            case 4:
                deleteBudget();
                break;
            default:
                System.out.println("无效的选择！");
        }
    }

    // 添加预算
    private static void addBudget() {
        System.out.println("\n=== 添加预算 ===");

        String category = InputUtil.getString("请输入预算类别: ");
        BigDecimal amount = InputUtil.getBigDecimal("请输入预算金额: ");
        String monthYear = InputUtil.getMonthYear("请输入预算月份");

        Budget budget = new Budget(currentUser.getUserId(), category, amount, monthYear);

        if (budgetService.addBudget(budget)) {
            System.out.println("预算添加成功！");
        } else {
            System.out.println("预算添加失败！");
        }
    }

    // 查看预算
    private static void viewBudgets() {
        System.out.println("\n=== 查看预算 ===");

        String monthYear = InputUtil.getMonthYear("请输入要查看的月份");

        List<Budget> budgets = budgetService.getBudgetsByUserIdAndMonth(currentUser.getUserId(), monthYear);

        if (budgets.isEmpty()) {
            System.out.println("没有找到预算记录！");
        } else {
            System.out.println("\n=== 预算列表 (" + monthYear + ") ===");
            for (Budget budget : budgets) {
                System.out.println(budget);
                System.out.println("-------------------");
            }
        }
    }

    // 修改预算
    private static void updateBudget() {
        System.out.println("\n=== 修改预算 ===");

        String monthYear = InputUtil.getMonthYear("请输入要修改的预算月份");

        List<Budget> budgets = budgetService.getBudgetsByUserIdAndMonth(currentUser.getUserId(), monthYear);

        if (budgets.isEmpty()) {
            System.out.println("没有找到预算记录！");
            return;
        }

        System.out.println("\n=== 预算列表 (" + monthYear + ") ===");
        for (Budget budget : budgets) {
            System.out.println(budget);
            System.out.println("-------------------");
        }

        int budgetId = InputUtil.getInt("请输入要修改的预算ID: ");
        BigDecimal newAmount = InputUtil.getBigDecimal("请输入新的预算金额: ");

        // 找到要修改的预算
        Budget budgetToUpdate = null;
        for (Budget budget : budgets) {
            if (budget.getBudgetId() == budgetId) {
                budgetToUpdate = budget;
                break;
            }
        }

        if (budgetToUpdate == null) {
            System.out.println("未找到指定的预算记录！");
            return;
        }

        budgetToUpdate.setAmount(newAmount);

        if (budgetService.updateBudget(budgetToUpdate)) {
            System.out.println("预算修改成功！");
        } else {
            System.out.println("预算修改失败！");
        }
    }

    // 删除预算
    private static void deleteBudget() {
        System.out.println("\n=== 删除预算 ===");

        String monthYear = InputUtil.getMonthYear("请输入要删除的预算月份");

        List<Budget> budgets = budgetService.getBudgetsByUserIdAndMonth(currentUser.getUserId(), monthYear);

        if (budgets.isEmpty()) {
            System.out.println("没有找到预算记录！");
            return;
        }

        System.out.println("\n=== 预算列表 (" + monthYear + ") ===");
        for (Budget budget : budgets) {
            System.out.println(budget);
            System.out.println("-------------------");
        }

        int budgetId = InputUtil.getInt("请输入要删除的预算ID: ");

        if (budgetService.deleteBudget(budgetId)) {
            System.out.println("预算删除成功！");
        } else {
            System.out.println("预算删除失败！");
        }
    }

    // 统计报表
    private static void showStatistics() {
        System.out.println("\n=== 统计报表 ===");
        System.out.println("1. 收支概况");
        System.out.println("2. 支出类别统计");
        System.out.print("请选择操作: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                showIncomeExpenseOverview();
                break;
            case 2:
                showExpenseByCategory();
                break;
            default:
                System.out.println("无效的选择！");
        }
    }

    // 收支概况
    private static void showIncomeExpenseOverview() {
        System.out.println("\n=== 收支概况 ===");

        BigDecimal totalIncome = incomeService.getTotalIncome(currentUser.getUserId());
        BigDecimal totalExpense = expenseService.getTotalExpense(currentUser.getUserId());
        BigDecimal balance = totalIncome.subtract(totalExpense);

        System.out.println("总收入: " + totalIncome);
        System.out.println("总支出: " + totalExpense);
        System.out.println("余额: " + balance);

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("警告：您的支出已超过收入！");
        }
    }

    // 支出类别统计
    private static void showExpenseByCategory() {
        System.out.println("\n=== 支出类别统计 ===");

        String monthYear = InputUtil.getMonthYear("请输入要统计的月份");

        List<Object[]> categoryExpenses = expenseService.getExpenseByCategory(currentUser.getUserId(), monthYear);

        if (categoryExpenses.isEmpty()) {
            System.out.println("没有找到支出记录！");
        } else {
            System.out.println("\n=== 支出类别统计 (" + monthYear + ") ===");
            BigDecimal total = BigDecimal.ZERO;

            for (Object[] row : categoryExpenses) {
                String category = (String) row[0];
                BigDecimal amount = (BigDecimal) row[1];
                total = total.add(amount);

                System.out.println("类别: " + category + ", 金额: " + amount);
            }

            System.out.println("总支出: " + total);

            // 显示每个类别的百分比
            System.out.println("\n=== 支出比例 ===");
            for (Object[] row : categoryExpenses) {
                String category = (String) row[0];
                BigDecimal amount = (BigDecimal) row[1];
                double percentage = amount.divide(total, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;

                System.out.printf("类别: %s, 比例: %.2f%%\n", category, percentage);
            }
        }
    }
}