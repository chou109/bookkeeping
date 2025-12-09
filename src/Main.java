import com.smartconsumption.entity.*;
import com.smartconsumption.service.*;
import com.smartconsumption.util.InputUtil;
import com.smartconsumption.util.UserExitException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
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
        System.out.println("提示：在任何输入环节，输入 exit/quit/back/返回/退出 都可以退出当前操作");

        // 检查是否有管理员账号，如果没有则创建默认管理员
        checkAndCreateAdmin();

        while (true) {
            if (currentUser == null) {
                showMainMenu();
            } else {
                // 根据用户角色显示不同的菜单
                if (userService.isAdmin(currentUser)) {
                    showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }
    }

    // 检查并创建默认管理员账号
    private static void checkAndCreateAdmin() {
        User admin = userService.getUserByUsername("admin");
        if (admin == null) {
            System.out.println("检测到没有管理员账号，正在创建默认管理员账号...");
            boolean success = userService.addAdminUser("admin", "admin123", "系统管理员");
            if (success) {
                System.out.println("默认管理员账号创建成功！");
                System.out.println("用户名：admin");
                System.out.println("密码：admin123");
            } else {
                System.out.println("管理员账号创建失败！");
            }
        }
    }

    // 安全的菜单选择方法
    private static int getMenuChoice(String title, String[] options, UserExitException.ExitType exitType) {
        System.out.println("\n=== " + title + " ===");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        while (true) {
            try {
                System.out.print("请选择操作 (1-" + options.length + "): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                if (InputUtil.isExitCommand(input)) {
                    throw new UserExitException(exitType);
                }

                if (input.isEmpty()) {
                    System.out.println("请选择一个选项！");
                    continue;
                }

                // 检查是否为数字
                if (!input.matches("\\d+")) {
                    System.out.println("请输入有效的数字！");
                    continue;
                }

                int choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= options.length) {
                    return choice;
                } else {
                    System.out.println("请输入 1 到 " + options.length + " 之间的数字！");
                }
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字！");
            } catch (UserExitException e) {
                throw e; // 重新抛出
            }
        }
    }

    // 主菜单（未登录状态）
    private static void showMainMenu() {
        String[] options = {"用户注册", "用户登录", "管理员登录", "退出系统"};

        try {
            int choice = getMenuChoice("主菜单", options, UserExitException.ExitType.RETURN_TO_MAIN);

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login(false); // 普通用户登录
                    break;
                case 3:
                    login(true); // 管理员登录
                    break;
                case 4:
                    System.out.println("感谢使用大学生智能消费记账系统，再见！");
                    System.exit(0);
                    break;
            }
        } catch (UserExitException e) {
            // 在主菜单中退出就是退出程序
            System.out.println("感谢使用大学生智能消费记账系统，再见！");
            System.exit(0);
        }
    }

    // 用户菜单（已登录状态 - 普通用户）
    private static void showUserMenu() {
        String[] options = {"个人信息管理", "收入管理", "支出管理", "预算管理", "统计报表", "退出登录"};

        try {
            int choice = getMenuChoice("用户菜单 (" + currentUser.getUsername() + ")", options,
                    UserExitException.ExitType.RETURN_TO_USER_MENU);

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
            }
        } catch (UserExitException e) {
            // 在用户菜单中退出，相当于退出登录
            currentUser = null;
            System.out.println("已退出用户菜单，返回主菜单。");
        }
    }

    // 管理员菜单（已登录状态 - 管理员）
    private static void showAdminMenu() {
        String[] options = {"用户管理", "系统统计", "查看所有用户", "创建新管理员", "修改用户信息", "退出登录"};

        try {
            int choice = getMenuChoice("管理员菜单 (" + currentUser.getUsername() + ")", options,
                    UserExitException.ExitType.RETURN_TO_USER_MENU);

            switch (choice) {
                case 1:
                    manageUsers();
                    break;
                case 2:
                    showSystemStatistics();
                    break;
                case 3:
                    viewAllUsers();
                    break;
                case 4:
                    createNewAdmin();
                    break;
                case 5:
                    updateUserInfo();
                    break;
                case 6:
                    currentUser = null;
                    System.out.println("管理员已退出登录！");
                    break;
            }
        } catch (UserExitException e) {
            // 在管理员菜单中退出，相当于退出登录
            currentUser = null;
            System.out.println("已退出管理员菜单，返回主菜单。");
        }
    }

    // 管理员用户管理功能
    private static void manageUsers() {
        String[] options = {"添加用户", "删除用户", "查看用户列表", "搜索用户", "返回上级菜单"};

        try {
            int choice = getMenuChoice("用户管理", options, UserExitException.ExitType.RETURN_TO_PARENT);

            switch (choice) {
                case 1:
                    addUserAsAdmin();
                    break;
                case 2:
                    deleteUserAsAdmin();
                    break;
                case 3:
                    viewUserList();
                    break;
                case 4:
                    searchUser();
                    break;
                case 5:
                    return;
            }
        } catch (UserExitException e) {
            System.out.println("已退出用户管理，返回上级菜单。");
        }
    }

    // 管理员添加用户
    private static void addUserAsAdmin() {
        System.out.println("\n=== 添加新用户 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出添加，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

            String username = InputUtil.getUsername("请输入用户名: ", exitType);

            // 检查用户名是否已存在
            if (userService.getUserByUsername(username) != null) {
                System.out.println("用户名已存在！");
                return;
            }

            String password = InputUtil.getPassword("请输入密码: ", exitType);
            String name = InputUtil.getName("请输入姓名: ", exitType);
            String studentId = InputUtil.getStudentId("请输入学号: ", exitType);
            String gender = InputUtil.getGender("请输入性别 (男/女): ", exitType);
            int age = InputUtil.getInt("请输入年龄: ", 1, 150, exitType);
            String phone = InputUtil.getPhone("请输入电话: ", exitType);
            String email = InputUtil.getEmail("请输入邮箱: ", exitType);

            // 确认是否添加
            System.out.println("\n即将添加的用户信息：");
            System.out.println("用户名: " + username);
            System.out.println("姓名: " + name);
            System.out.println("学号: " + studentId);
            System.out.println("性别: " + gender);
            System.out.println("年龄: " + age);
            System.out.println("电话: " + phone);
            System.out.println("邮箱: " + email);

            boolean confirm = InputUtil.getYesNo("确认添加此用户？", exitType);
            if (!confirm) {
                System.out.println("已取消添加用户。");
                return;
            }

            User newUser = new User(username, password, name, studentId, gender, age, phone, email);

            if (userService.register(newUser)) {
                System.out.println("用户添加成功！");
            } else {
                System.out.println("用户添加失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出添加用户，返回上级菜单。");
        }
    }

    // 管理员删除用户
    private static void deleteUserAsAdmin() {
        System.out.println("\n=== 删除用户 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出删除，返回上级菜单");

        // 显示所有普通用户
        List<User> users = userService.getAllRegularUsers();
        if (users.isEmpty()) {
            System.out.println("没有可删除的普通用户！");
            return;
        }

        System.out.println("\n=== 普通用户列表 ===");
        for (User user : users) {
            System.out.println("ID: " + user.getUserId() + ", 用户名: " + user.getUsername() + ", 姓名: " + user.getName());
        }

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;
            int userId = InputUtil.getInt("请输入要删除的用户ID: ", 1, Integer.MAX_VALUE, exitType);

            // 检查用户是否存在
            boolean userExists = false;
            String userName = "";
            for (User user : users) {
                if (user.getUserId() == userId) {
                    userExists = true;
                    userName = user.getName() + " (" + user.getUsername() + ")";
                    break;
                }
            }

            if (!userExists) {
                System.out.println("未找到该用户ID！");
                return;
            }

            // 检查是否要删除当前登录的管理员自己（虽然不应该出现在列表中）
            if (userId == currentUser.getUserId()) {
                System.out.println("不能删除当前登录的管理员账号！");
                return;
            }

            boolean confirm = InputUtil.getYesNo("确认删除用户 " + userName + "？此操作不可恢复！", exitType);
            if (!confirm) {
                System.out.println("已取消删除操作。");
                return;
            }

            if (userService.deleteUser(userId)) {
                System.out.println("用户删除成功！");
            } else {
                System.out.println("用户删除失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出删除用户，返回上级菜单。");
        }
    }

    // 查看用户列表
    private static void viewUserList() {
        System.out.println("\n=== 用户列表 ===");

        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("系统中还没有任何用户！");
            return;
        }

        System.out.println("=".repeat(80));
        System.out.printf("%-10s %-15s %-15s %-10s %-20s %-10s%n",
                "用户ID", "用户名", "姓名", "角色", "注册时间", "状态");
        System.out.println("=".repeat(80));

        for (User user : users) {
            String roleDisplay = "admin".equals(user.getRole()) ? "👑管理员" : "👤用户";
            String status = (user.getUserId() == currentUser.getUserId()) ? "（当前）" : "";

            System.out.printf("%-10d %-15s %-15s %-10s %-20s %-10s%n",
                    user.getUserId(),
                    user.getUsername(),
                    user.getName(),
                    roleDisplay,
                    user.getCreatedDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    status);
        }
        System.out.println("=".repeat(80));

        // 显示统计信息
        int totalUsers = userService.getUserCount();
        int regularUsers = userService.getRegularUserCount();
        int adminUsers = userService.getAdminCount();

        System.out.println("\n📊 用户统计:");
        System.out.println("总用户数: " + totalUsers);
        System.out.println("普通用户: " + regularUsers);
        System.out.println("管理员: " + adminUsers);
    }

    // 搜索用户
    private static void searchUser() {
        System.out.println("\n=== 搜索用户 ===");
        System.out.println("提示：输入 exit 可以退出搜索，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

            String[] searchOptions = {"按用户名搜索", "按姓名搜索", "按学号搜索", "返回"};
            int choice = getMenuChoice("搜索方式", searchOptions, exitType);

            if (choice == 4) {
                return;
            }

            String keyword = InputUtil.getString("请输入搜索关键词: ", 50, exitType);

            List<User> allUsers = userService.getAllUsers();
            List<User> searchResults = new ArrayList<>();

            for (User user : allUsers) {
                boolean match = false;
                switch (choice) {
                    case 1: // 按用户名搜索
                        match = user.getUsername().toLowerCase().contains(keyword.toLowerCase());
                        break;
                    case 2: // 按姓名搜索
                        match = user.getName().toLowerCase().contains(keyword.toLowerCase());
                        break;
                    case 3: // 按学号搜索
                        if (user.getStudentId() != null) {
                            match = user.getStudentId().toLowerCase().contains(keyword.toLowerCase());
                        }
                        break;
                }

                if (match) {
                    searchResults.add(user);
                }
            }

            if (searchResults.isEmpty()) {
                System.out.println("没有找到匹配的用户！");
            } else {
                System.out.println("\n=== 搜索结果 (" + searchResults.size() + " 个匹配) ===");
                System.out.println("=".repeat(80));
                System.out.printf("%-10s %-15s %-15s %-10s %-20s%n",
                        "用户ID", "用户名", "姓名", "角色", "注册时间");
                System.out.println("=".repeat(80));

                for (User user : searchResults) {
                    String roleDisplay = "admin".equals(user.getRole()) ? "管理员" : "用户";

                    System.out.printf("%-10d %-15s %-15s %-10s %-20s%n",
                            user.getUserId(),
                            user.getUsername(),
                            user.getName(),
                            roleDisplay,
                            user.getCreatedDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                }
                System.out.println("=".repeat(80));
            }

        } catch (UserExitException e) {
            System.out.println("已退出搜索用户，返回上级菜单。");
        }
    }

    // 查看所有用户
    private static void viewAllUsers() {
        System.out.println("\n=== 所有用户信息 ===");

        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("系统中还没有任何用户！");
            return;
        }

        for (User user : users) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println(user);
            System.out.println("=".repeat(50));
        }
    }

    // 创建新管理员
    private static void createNewAdmin() {
        System.out.println("\n=== 创建新管理员 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出创建，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

            String username = InputUtil.getUsername("请输入管理员用户名: ", exitType);

            // 检查用户名是否已存在
            if (userService.getUserByUsername(username) != null) {
                System.out.println("用户名已存在！");
                return;
            }

            String password = InputUtil.getPassword("请输入管理员密码: ", exitType);
            String name = InputUtil.getName("请输入管理员姓名: ", exitType);

            // 确认是否创建
            System.out.println("\n即将创建的管理员信息：");
            System.out.println("用户名: " + username);
            System.out.println("姓名: " + name);
            System.out.println("角色: 管理员");

            boolean confirm = InputUtil.getYesNo("确认创建此管理员账号？", exitType);
            if (!confirm) {
                System.out.println("已取消创建管理员。");
                return;
            }

            if (userService.addAdminUser(username, password, name)) {
                System.out.println("管理员账号创建成功！");
            } else {
                System.out.println("管理员账号创建失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出创建管理员，返回上级菜单。");
        }
    }

    // 修改用户信息
    private static void updateUserInfo() {
        System.out.println("\n=== 修改用户信息 ===");

        // 显示所有用户
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("系统中还没有任何用户！");
            return;
        }

        System.out.println("\n=== 用户列表 ===");
        for (User user : users) {
            System.out.println("ID: " + user.getUserId() + ", 用户名: " + user.getUsername() +
                    ", 姓名: " + user.getName() + ", 角色: " +
                    ("admin".equals(user.getRole()) ? "管理员" : "用户"));
        }

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;
            int userId = InputUtil.getInt("请输入要修改的用户ID: ", 1, Integer.MAX_VALUE, exitType);

            // 查找用户
            User userToUpdate = null;
            for (User user : users) {
                if (user.getUserId() == userId) {
                    userToUpdate = user;
                    break;
                }
            }

            if (userToUpdate == null) {
                System.out.println("未找到该用户ID！");
                return;
            }

            System.out.println("\n当前用户信息：");
            System.out.println(userToUpdate);

            String[] updateOptions = {"修改基本信息", "修改角色", "重置密码", "返回"};
            int choice = getMenuChoice("选择修改类型", updateOptions, exitType);

            switch (choice) {
                case 1:
                    updateUserBasicInfo(userToUpdate, exitType);
                    break;
                case 2:
                    updateUserRole(userToUpdate, exitType);
                    break;
                case 3:
                    resetUserPassword(userToUpdate, exitType);
                    break;
                case 4:
                    return;
            }
        } catch (UserExitException e) {
            System.out.println("已退出修改用户信息，返回上级菜单。");
        }
    }

    // 修改用户基本信息
    private static void updateUserBasicInfo(User user, UserExitException.ExitType exitType) {
        System.out.println("\n=== 修改用户基本信息 ===");

        try {
            String name = InputUtil.getStringOptional("请输入新姓名 (" + user.getName() + "): ", 50, exitType);
            String studentId = InputUtil.getStringOptional("请输入新学号 (" + user.getStudentId() + "): ", 20, exitType);

            // 性别输入
            String gender = null;
            System.out.print("请输入新性别 (男/女) (" + user.getGender() + "，直接回车跳过): ");
            String genderInput = scanner.nextLine().trim();
            if (InputUtil.isExitCommand(genderInput)) {
                throw new UserExitException(exitType);
            }
            if (!genderInput.isEmpty()) {
                if (genderInput.equals("男") || genderInput.equals("女")) {
                    gender = genderInput;
                } else {
                    System.out.println("性别只能是'男'或'女'，将保持原值。");
                }
            }

            int age = -1;
            System.out.print("请输入新年龄 (" + user.getAge() + "，直接回车跳过): ");
            String ageInput = scanner.nextLine().trim();
            if (InputUtil.isExitCommand(ageInput)) {
                throw new UserExitException(exitType);
            }
            if (!ageInput.isEmpty()) {
                try {
                    age = Integer.parseInt(ageInput);
                    if (age < 1 || age > 150) {
                        System.out.println("年龄必须在1-150之间，将保持原值。");
                        age = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("年龄必须是数字，将保持原值。");
                }
            }

            String phone = InputUtil.getStringOptional("请输入新电话 (" + user.getPhone() + "): ", 15, exitType);
            String email = InputUtil.getStringOptional("请输入新邮箱 (" + user.getEmail() + "): ", 100, exitType);

            // 更新用户信息
            if (!name.isEmpty()) {
                user.setName(name);
            }
            if (!studentId.isEmpty()) {
                user.setStudentId(studentId);
            }
            if (gender != null) {
                user.setGender(gender);
            }
            if (age != -1) {
                user.setAge(age);
            }
            if (!phone.isEmpty()) {
                user.setPhone(phone);
            }
            if (!email.isEmpty()) {
                user.setEmail(email);
            }

            if (userService.updateUser(user)) {
                System.out.println("用户信息更新成功！");
            } else {
                System.out.println("用户信息更新失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出修改用户信息，返回上级菜单。");
        }
    }

    // 修改用户角色
    private static void updateUserRole(User user, UserExitException.ExitType exitType) {
        System.out.println("\n=== 修改用户角色 ===");
        System.out.println("当前角色: " + ("admin".equals(user.getRole()) ? "管理员" : "普通用户"));

        // 不能修改当前登录的管理员自己的角色
        if (user.getUserId() == currentUser.getUserId()) {
            System.out.println("不能修改当前登录的管理员自己的角色！");
            return;
        }

        try {
            System.out.print("请选择新角色 (输入'管理员'或'用户'，输入 exit 退出): ");
            String input = scanner.nextLine().trim();

            if (InputUtil.isExitCommand(input)) {
                throw new UserExitException(exitType);
            }

            String roleValue;
            if (input.equals("管理员")) {
                roleValue = "admin";
            } else if (input.equals("用户")) {
                roleValue = "user";
            } else {
                System.out.println("角色只能输入'管理员'或'用户'！");
                return;
            }

            boolean confirm = InputUtil.getYesNo("确认将用户 " + user.getUsername() + " 的角色修改为 " +
                    ("admin".equals(roleValue) ? "管理员" : "普通用户") + "？", exitType);
            if (!confirm) {
                System.out.println("已取消修改角色。");
                return;
            }

            user.setRole(roleValue);

            if (userService.updateUser(user)) {
                System.out.println("用户角色更新成功！");
            } else {
                System.out.println("用户角色更新失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出修改用户角色，返回上级菜单。");
        }
    }

    // 重置用户密码
    private static void resetUserPassword(User user, UserExitException.ExitType exitType) {
        System.out.println("\n=== 重置用户密码 ===");
        System.out.println("用户名: " + user.getUsername());
        System.out.println("姓名: " + user.getName());

        try {
            String newPassword = InputUtil.getPassword("请输入新密码: ", exitType);
            String confirmPassword = InputUtil.getString("请确认新密码: ", 100, exitType);

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("两次输入的密码不一致！");
                return;
            }

            boolean confirm = InputUtil.getYesNo("确认重置用户 " + user.getUsername() + " 的密码？", exitType);
            if (!confirm) {
                System.out.println("已取消重置密码。");
                return;
            }

            user.setPassword(newPassword);

            if (userService.updateUser(user)) {
                System.out.println("用户密码重置成功！");
            } else {
                System.out.println("用户密码重置失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出重置用户密码，返回上级菜单。");
        }
    }

    // 显示系统统计信息
    private static void showSystemStatistics() {
        System.out.println("\n=== 系统统计信息 ===");

        int totalUsers = userService.getUserCount();
        int regularUsers = userService.getRegularUserCount();
        int adminUsers = userService.getAdminCount();

        System.out.println("📊 用户统计:");
        System.out.println("总用户数: " + totalUsers);
        System.out.println("普通用户: " + regularUsers);
        System.out.println("管理员: " + adminUsers);

        // 显示当前系统时间
        System.out.println("\n⏰ 系统时间: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 显示系统信息
        System.out.println("\n💻 系统信息:");
        System.out.println("Java版本: " + System.getProperty("java.version"));
        System.out.println("操作系统: " + System.getProperty("os.name"));
    }

    // 修改后的登录方法，支持管理员登录
    private static void login(boolean isAdminLogin) {
        if (isAdminLogin) {
            System.out.println("\n=== 管理员登录 ===");
        } else {
            System.out.println("\n=== 用户登录 ===");
        }
        System.out.println("提示：在任何步骤输入 exit 可以退出登录，返回主菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_MAIN;

            String username = InputUtil.getString("请输入用户名: ", 50, exitType);
            String password = InputUtil.getString("请输入密码: ", 100, exitType);

            User user = userService.login(username, password);

            if (user != null) {
                // 检查登录类型是否匹配
                if (isAdminLogin && !userService.isAdmin(user)) {
                    System.out.println("登录失败！该账号不是管理员账号！");
                    return;
                }

                if (!isAdminLogin && userService.isAdmin(user)) {
                    System.out.println("登录失败！管理员账号请从管理员登录入口进入！");
                    return;
                }

                currentUser = user;
                if (userService.isAdmin(user)) {
                    System.out.println("管理员登录成功！欢迎 " + user.getName() + "！");
                } else {
                    System.out.println("登录成功！欢迎 " + user.getName() + "！");
                }
            } else {
                System.out.println("登录失败！用户名或密码错误！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出登录，返回主菜单。");
        }
    }

    // 个人信息管理
    private static void managePersonalInfo() {
        String[] options = {"查看个人信息", "修改个人信息"};

        try {
            int choice = getMenuChoice("个人信息管理", options, UserExitException.ExitType.RETURN_TO_PARENT);

            switch (choice) {
                case 1:
                    System.out.println("\n=== 个人信息 ===");
                    System.out.println(currentUser);
                    break;
                case 2:
                    updatePersonalInfo();
                    break;
            }
        } catch (UserExitException e) {
            System.out.println("已退出个人信息管理，返回上级菜单。");
        }
    }

    // 收入管理
    private static void manageIncome() {
        String[] options = {"添加收入记录", "查看收入记录", "删除收入记录"};

        try {
            int choice = getMenuChoice("收入管理", options, UserExitException.ExitType.RETURN_TO_PARENT);

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
            }
        } catch (UserExitException e) {
            System.out.println("已退出收入管理，返回上级菜单。");
        }
    }

    // 支出管理
    private static void manageExpense() {
        String[] options = {"添加支出记录", "查看支出记录", "删除支出记录"};

        try {
            int choice = getMenuChoice("支出管理", options, UserExitException.ExitType.RETURN_TO_PARENT);

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
            }
        } catch (UserExitException e) {
            System.out.println("已退出支出管理，返回上级菜单。");
        }
    }

    // 预算管理
    private static void manageBudget() {
        String[] options = {"添加预算", "查看预算", "修改预算", "删除预算", "预算执行情况"};

        try {
            int choice = getMenuChoice("预算管理", options, UserExitException.ExitType.RETURN_TO_PARENT);

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
                case 5:
                    viewBudgetExecution();
                    break;
            }
        } catch (UserExitException e) {
            System.out.println("已退出预算管理，返回上级菜单。");
        }
    }

    // 统计报表
    private static void showStatistics() {
        String[] options = {"收支概况", "支出类别统计"};

        try {
            int choice = getMenuChoice("统计报表", options, UserExitException.ExitType.RETURN_TO_PARENT);

            switch (choice) {
                case 1:
                    showIncomeExpenseOverview();
                    break;
                case 2:
                    showExpenseByCategory();
                    break;
            }
        } catch (UserExitException e) {
            System.out.println("已退出统计报表，返回上级菜单。");
        }
    }

    // 修改个人信息
    private static void updatePersonalInfo() {
        System.out.println("\n=== 修改个人信息 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出修改，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

            String name = InputUtil.getStringOptional("请输入新姓名 (" + currentUser.getName() + "): ", 50, exitType);
            String studentId = InputUtil.getStringOptional("请输入新学号 (" + currentUser.getStudentId() + "): ", 20, exitType);

            // 性别输入，允许跳过
            String gender = null;
            System.out.print("请输入新性别 (男/女) (" + currentUser.getGender() + "，输入 exit 退出): ");
            String genderInput = scanner.nextLine().trim();
            if (InputUtil.isExitCommand(genderInput)) {
                throw new UserExitException(exitType);
            }
            if (!genderInput.isEmpty()) {
                if (genderInput.equals("男") || genderInput.equals("女")) {
                    gender = genderInput;
                } else {
                    System.out.println("性别只能是'男'或'女'，将保持原值。");
                }
            }

            int age = -1; // 使用-1表示未修改
            System.out.print("请输入新年龄 (" + currentUser.getAge() + "，输入 exit 退出): ");
            String ageInput = scanner.nextLine().trim();
            if (InputUtil.isExitCommand(ageInput)) {
                throw new UserExitException(exitType);
            }
            if (!ageInput.isEmpty()) {
                try {
                    age = Integer.parseInt(ageInput);
                    if (age < 1 || age > 150) {
                        System.out.println("年龄必须在1-150之间，将保持原值。");
                        age = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("年龄必须是数字，将保持原值。");
                }
            }

            String phone = InputUtil.getStringOptional("请输入新电话 (" + currentUser.getPhone() + "): ", 15, exitType);
            String email = InputUtil.getStringOptional("请输入新邮箱 (" + currentUser.getEmail() + "): ", 100, exitType);

            // 显示将要更新的信息
            System.out.println("\n将要更新的个人信息:");
            System.out.println("姓名: " + (!name.isEmpty() ? name : "(保持原值)"));
            System.out.println("学号: " + (!studentId.isEmpty() ? studentId : "(保持原值)"));
            System.out.println("性别: " + (gender != null ? gender : "(保持原值)"));
            System.out.println("年龄: " + (age != -1 ? age : "(保持原值)"));
            System.out.println("电话: " + (!phone.isEmpty() ? phone : "(保持原值)"));
            System.out.println("邮箱: " + (!email.isEmpty() ? email : "(保持原值)"));

            // 确认是否更新
            boolean confirm = InputUtil.getYesNo("确认更新个人信息？", exitType);
            if (!confirm) {
                boolean tryAgain = InputUtil.getYesNo("是否重新输入？", exitType);
                if (!tryAgain) {
                    System.out.println("已取消修改个人信息，返回上级菜单。");
                    return;
                }
                updatePersonalInfo(); // 重新开始
                return;
            }

            // 只更新用户输入了内容的字段
            if (!name.isEmpty()) {
                currentUser.setName(name);
            }
            if (!studentId.isEmpty()) {
                currentUser.setStudentId(studentId);
            }
            if (gender != null) {
                currentUser.setGender(gender);
            }
            if (age != -1) {
                currentUser.setAge(age);
            }
            if (!phone.isEmpty()) {
                currentUser.setPhone(phone);
            }
            if (!email.isEmpty()) {
                currentUser.setEmail(email);
            }

            if (userService.updateUser(currentUser)) {
                System.out.println("个人信息更新成功！");
            } else {
                System.out.println("个人信息更新失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出修改个人信息，返回上级菜单。");
        }
    }

    // 添加收入记录
    private static void addIncome() {
        System.out.println("\n=== 添加收入记录 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出添加，返回上级菜单");

        boolean retry = true;

        while (retry) {
            try {
                UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

                BigDecimal amount = InputUtil.getValidAmount("请输入收入金额(整数最多8位，小数最多2位): ", 8, 2, exitType);
                String source = InputUtil.getString("请输入收入来源: ", 100, exitType);

                // 使用新的日期验证方法，只能选择当前日期或之前的日期
                LocalDate incomeDate = InputUtil.getPastOrCurrentDate("请输入收入日期", exitType);

                String description = InputUtil.getStringOptional("请输入收入描述 (可选): ", 500, exitType);

                Income income = new Income(currentUser.getUserId(), amount, source, incomeDate, description);

                if (incomeService.addIncome(income)) {
                    System.out.println("收入记录添加成功！");
                    retry = false; // 添加成功，退出循环
                } else {
                    System.out.println("收入记录添加失败！");
                    // 询问是否重试
                    boolean tryAgain = InputUtil.getYesNo("是否重试？", exitType);
                    if (!tryAgain) {
                        System.out.println("已取消添加收入记录，返回上级菜单。");
                        return;
                    }
                }
            } catch (UserExitException e) {
                System.out.println("已退出添加收入记录，返回上级菜单。");
                return;
            } catch (Exception e) {
                // 如果发生异常，询问用户是否重试
                System.out.println("输入过程中出现错误: " + e.getMessage());
                boolean tryAgain = InputUtil.getYesNo("是否重试？", UserExitException.ExitType.RETURN_TO_PARENT);
                if (!tryAgain) {
                    System.out.println("已取消添加收入记录，返回上级菜单。");
                    return;
                }
            }
        }
    }

    // 查看收入记录
    private static void viewIncomes() {
        System.out.println("\n=== 查看收入记录 ===");
        String[] options = {"查看所有收入记录", "按日期范围查看收入记录"};

        try {
            int choice = getMenuChoice("查看收入记录", options, UserExitException.ExitType.RETURN_TO_PARENT);

            List<Income> incomes;

            switch (choice) {
                case 1:
                    incomes = incomeService.getIncomesByUserId(currentUser.getUserId());
                    break;
                case 2:
                    try {
                        UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;
                        LocalDate startDate = InputUtil.getDate("请输入开始日期: ", exitType);
                        LocalDate endDate = InputUtil.getDate("请输入结束日期: ", exitType);
                        incomes = incomeService.getIncomesByDateRange(currentUser.getUserId(), startDate, endDate);
                    } catch (UserExitException e) {
                        System.out.println("已退出查看收入记录，返回上级菜单。");
                        return;
                    }
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
        } catch (UserExitException e) {
            System.out.println("已退出查看收入记录，返回上级菜单。");
        }
    }

    // 删除收入记录
    private static void deleteIncome() {
        System.out.println("\n=== 删除收入记录 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出删除，返回上级菜单");

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

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;
            int incomeId = InputUtil.getInt("请输入要删除的收入记录ID: ", 1, Integer.MAX_VALUE, exitType);

            boolean confirm = InputUtil.getYesNo("确认删除此收入记录？", exitType);
            if (!confirm) {
                System.out.println("已取消删除操作，返回上级菜单。");
                return;
            }

            if (incomeService.deleteIncome(incomeId)) {
                System.out.println("收入记录删除成功！");
            } else {
                System.out.println("收入记录删除失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出删除收入记录，返回上级菜单。");
        }
    }

    // 添加支出记录
    private static void addExpense() {
        System.out.println("\n=== 添加支出记录 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出添加，返回上级菜单");

        boolean retry = true;

        while (retry) {
            try {
                UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

                // 整数最多8位，小数最多2位
                BigDecimal amount = InputUtil.getValidAmount("请输入支出金额(整数最多8位，小数最多2位): ", 8, 2, exitType);

                String category = InputUtil.getString("请输入支出类别: ", 50, exitType);

                // 使用新的日期验证方法，只能选择当前日期或之前的日期
                LocalDate expenseDate = InputUtil.getPastOrCurrentDate("请输入支出日期", exitType);

                // 检查预算
                String monthYear = expenseDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));

                // 检查预算警告，如果用户取消则返回false
                if (!checkBudgetWarning(currentUser.getUserId(), category, amount, monthYear, exitType)) {
                    System.out.println("已取消添加支出记录，返回上级菜单。");
                    return; // 直接返回，不继续执行
                }

                String description = InputUtil.getStringOptional("请输入支出描述 (可选): ", 500, exitType);

                Expense expense = new Expense(currentUser.getUserId(), amount, category, expenseDate, description);

                if (expenseService.addExpense(expense)) {
                    System.out.println("支出记录添加成功！");
                    retry = false; // 添加成功，退出循环
                } else {
                    System.out.println("支出记录添加失败！");
                    // 询问是否重试
                    boolean tryAgain = InputUtil.getYesNo("是否重试？", exitType);
                    if (!tryAgain) {
                        System.out.println("已取消添加支出记录，返回上级菜单。");
                        return;
                    }
                }
            } catch (UserExitException e) {
                System.out.println("已退出添加支出记录，返回上级菜单。");
                return;
            } catch (Exception e) {
                // 如果发生异常，询问用户是否重试
                System.out.println("输入过程中出现错误: " + e.getMessage());
                boolean tryAgain = InputUtil.getYesNo("是否重试？", UserExitException.ExitType.RETURN_TO_PARENT);
                if (!tryAgain) {
                    System.out.println("已取消添加支出记录，返回上级菜单。");
                    return;
                }
            }
        }
    }

    // 检查预算警告的辅助方法
    private static boolean checkBudgetWarning(int userId, String category, BigDecimal newExpenseAmount,
                                              String monthYear, UserExitException.ExitType exitType) {
        // 获取该类别预算
        BigDecimal budgetAmount = budgetService.getBudgetByCategory(userId, category, monthYear);

        if (budgetAmount != null) {
            // 获取该类别已支出总额
            BigDecimal currentExpense = expenseService.getMonthlyExpenseByCategory(userId, category, monthYear);

            // 计算新的支出总额
            BigDecimal newTotalExpense = currentExpense.add(newExpenseAmount);

            // 检查是否超出预算
            if (newTotalExpense.compareTo(budgetAmount) > 0) {
                System.out.println("\n⚠️ 警告：本月'" + category + "'类别的预算为 " + budgetAmount);
                System.out.println("   已支出: " + currentExpense);
                System.out.println("   本次支出: " + newExpenseAmount);
                System.out.println("   支出后总额: " + newTotalExpense + " (超出预算!)");
                System.out.println("   建议调整支出计划或修改预算！");

                // 确认是否继续
                boolean continueExpense = InputUtil.getYesNo("是否继续添加此支出记录？", exitType);
                if (!continueExpense) {
                    return false; // 用户取消
                }
            } else if (newTotalExpense.compareTo(budgetAmount) == 0) {
                System.out.println("\n⚠️ 注意：本月'" + category + "'类别的预算已用完！");
                System.out.println("   预算: " + budgetAmount);
                System.out.println("   本次支出后总额: " + newTotalExpense);

                // 确认是否继续
                boolean continueExpense = InputUtil.getYesNo("预算已用完，是否继续添加此支出记录？", exitType);
                if (!continueExpense) {
                    return false; // 用户取消
                }
            } else if (newTotalExpense.compareTo(budgetAmount.multiply(new BigDecimal("0.9"))) >= 0) {
                // 当支出达到预算的90%时给出警告
                System.out.println("\n⚠️ 注意：本月'" + category + "'类别的支出已接近预算！");
                System.out.println("   预算: " + budgetAmount);
                System.out.println("   已支出: " + currentExpense);
                System.out.println("   本次支出后总额: " + newTotalExpense);
                System.out.println("   剩余预算: " + budgetAmount.subtract(newTotalExpense));

                // 确认是否继续
                boolean continueExpense = InputUtil.getYesNo("支出已接近预算，是否继续添加此支出记录？", exitType);
                if (!continueExpense) {
                    return false; // 用户取消
                }
            }
        }

        return true; // 可以继续
    }

    // 查看支出记录
    private static void viewExpenses() {
        System.out.println("\n=== 查看支出记录 ===");
        String[] options = {"查看所有支出记录", "按日期范围查看支出记录"};

        try {
            int choice = getMenuChoice("查看支出记录", options, UserExitException.ExitType.RETURN_TO_PARENT);

            List<Expense> expenses;

            switch (choice) {
                case 1:
                    expenses = expenseService.getExpensesByUserId(currentUser.getUserId());
                    break;
                case 2:
                    try {
                        UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;
                        LocalDate startDate = InputUtil.getDate("请输入开始日期: ", exitType);
                        LocalDate endDate = InputUtil.getDate("请输入结束日期: ", exitType);
                        expenses = expenseService.getExpensesByDateRange(currentUser.getUserId(), startDate, endDate);
                    } catch (UserExitException e) {
                        System.out.println("已退出查看支出记录，返回上级菜单。");
                        return;
                    }
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
        } catch (UserExitException e) {
            System.out.println("已退出查看支出记录，返回上级菜单。");
        }
    }

    // 删除支出记录
    private static void deleteExpense() {
        System.out.println("\n=== 删除支出记录 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出删除，返回上级菜单");

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

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;
            int expenseId = InputUtil.getInt("请输入要删除的支出记录ID: ", 1, Integer.MAX_VALUE, exitType);

            boolean confirm = InputUtil.getYesNo("确认删除此支出记录？", exitType);
            if (!confirm) {
                System.out.println("已取消删除操作，返回上级菜单。");
                return;
            }

            if (expenseService.deleteExpense(expenseId)) {
                System.out.println("支出记录删除成功！");
            } else {
                System.out.println("支出记录删除失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出删除支出记录，返回上级菜单。");
        }
    }

    // 添加预算
    private static void addBudget() {
        System.out.println("\n=== 添加预算 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出添加，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

            String category = InputUtil.getString("请输入预算类别: ", 50, exitType);
            BigDecimal amount = InputUtil.getValidAmount("请输入预算金额(整数最多8位，小数最多2位): ", 8, 2, exitType);

            // 使用增强的月份验证
            String monthYear = InputUtil.getMonthYear("请输入预算月份: ", exitType);

            Budget budget = new Budget(currentUser.getUserId(), category, amount, monthYear);

            if (budgetService.addBudget(budget)) {
                System.out.println("预算添加成功！");
            } else {
                System.out.println("预算添加失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出添加预算，返回上级菜单。");
        }
    }

    // 查看预算
    private static void viewBudgets() {
        System.out.println("\n=== 查看预算 ===");
        System.out.println("提示：输入 exit 可以退出查看，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;
            String monthYear = InputUtil.getMonthYear("请输入要查看的月份: ", exitType);

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
        } catch (UserExitException e) {
            System.out.println("已退出查看预算，返回上级菜单。");
        }
    }

    // 修改预算
    private static void updateBudget() {
        System.out.println("\n=== 修改预算 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出修改，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

            String monthYear = InputUtil.getMonthYear("请输入要修改的预算月份: ", exitType);

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

            int budgetId = InputUtil.getInt("请输入要修改的预算ID: ", 1, Integer.MAX_VALUE, exitType);

            // 整数最多8位，小数最多2位
            BigDecimal newAmount = InputUtil.getValidAmount("请输入新的预算金额(整数最多8位，小数最多2位): ", 8, 2, exitType);

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

            boolean confirm = InputUtil.getYesNo("确认修改此预算？", exitType);
            if (!confirm) {
                System.out.println("已取消修改操作，返回上级菜单。");
                return;
            }

            budgetToUpdate.setAmount(newAmount);

            if (budgetService.updateBudget(budgetToUpdate)) {
                System.out.println("预算修改成功！");
            } else {
                System.out.println("预算修改失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出修改预算，返回上级菜单。");
        }
    }

    // 删除预算
    private static void deleteBudget() {
        System.out.println("\n=== 删除预算 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出删除，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

            String monthYear = InputUtil.getMonthYear("请输入要删除的预算月份: ", exitType);

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

            int budgetId = InputUtil.getInt("请输入要删除的预算ID: ", 1, Integer.MAX_VALUE, exitType);

            boolean confirm = InputUtil.getYesNo("确认删除此预算？", exitType);
            if (!confirm) {
                System.out.println("已取消删除操作，返回上级菜单。");
                return;
            }

            if (budgetService.deleteBudget(budgetId)) {
                System.out.println("预算删除成功！");
            } else {
                System.out.println("预算删除失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出删除预算，返回上级菜单。");
        }
    }

    // 查看预算执行情况
    private static void viewBudgetExecution() {
        System.out.println("\n=== 预算执行情况 ===");
        System.out.println("提示：输入 exit 可以退出查看，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

            String monthYear = InputUtil.getMonthYear("请输入要查看的月份: ", exitType);

            // 获取该月所有预算
            List<Budget> budgets = budgetService.getBudgetsByUserIdAndMonth(currentUser.getUserId(), monthYear);

            if (budgets.isEmpty()) {
                System.out.println("没有找到预算记录！");
                return;
            }

            System.out.println("\n=== 预算执行情况 (" + monthYear + ") ===");
            System.out.println("=".repeat(70));
            System.out.printf("%-15s %-15s %-15s %-15s %-10s%n",
                    "类别", "预算金额", "已支出", "剩余预算", "执行率");
            System.out.println("=".repeat(70));

            for (Budget budget : budgets) {
                String category = budget.getCategory();
                BigDecimal budgetAmount = budget.getAmount();

                // 获取该类别已支出总额
                BigDecimal currentExpense = expenseService.getMonthlyExpenseByCategory(
                        currentUser.getUserId(), category, monthYear);

                // 计算剩余预算
                BigDecimal remaining = budgetAmount.subtract(currentExpense);

                // 计算执行率（已支出/预算）
                double executionRate = 0;
                if (budgetAmount.compareTo(BigDecimal.ZERO) > 0) {
                    executionRate = currentExpense.divide(budgetAmount, 4, RoundingMode.HALF_UP).doubleValue() * 100;
                }

                // 设置颜色标记
                String colorCode = "";
                String resetCode = "";

                if (remaining.compareTo(BigDecimal.ZERO) < 0) {
                    colorCode = "\u001B[31m"; // 红色（超出预算）
                } else if (executionRate >= 90) {
                    colorCode = "\u001B[33m"; // 黄色（接近预算）
                }

                System.out.printf("%-15s %-15s %-15s %-15s %s%-9.2f%%%s%n",
                        category,
                        budgetAmount,
                        currentExpense,
                        remaining,
                        colorCode,
                        executionRate,
                        resetCode);
            }
            System.out.println("=".repeat(70));

            // 显示预算执行情况分析
            showBudgetExecutionAnalysis(budgets, monthYear);
        } catch (UserExitException e) {
            System.out.println("已退出查看预算执行情况，返回上级菜单。");
        }
    }

    // 预算执行情况分析
    private static void showBudgetExecutionAnalysis(List<Budget> budgets, String monthYear) {
        System.out.println("\n=== 预算执行分析 ===");

        int overBudgetCount = 0;
        int nearBudgetCount = 0;
        BigDecimal totalBudget = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (Budget budget : budgets) {
            String category = budget.getCategory();
            BigDecimal budgetAmount = budget.getAmount();
            BigDecimal currentExpense = expenseService.getMonthlyExpenseByCategory(
                    currentUser.getUserId(), category, monthYear);

            totalBudget = totalBudget.add(budgetAmount);
            totalExpense = totalExpense.add(currentExpense);

            double executionRate = 0;
            if (budgetAmount.compareTo(BigDecimal.ZERO) > 0) {
                executionRate = currentExpense.divide(budgetAmount, 4, RoundingMode.HALF_UP).doubleValue() * 100;
            }

            if (executionRate > 100) {
                overBudgetCount++;
            } else if (executionRate >= 90) {
                nearBudgetCount++;
            }
        }

        // 计算总体执行率
        BigDecimal overallRate = BigDecimal.ZERO;
        if (totalBudget.compareTo(BigDecimal.ZERO) > 0) {
            overallRate = totalExpense.divide(totalBudget, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }

        System.out.println("总预算: " + totalBudget);
        System.out.println("总支出: " + totalExpense);
        System.out.printf("总体执行率: %.2f%%\n", overallRate);

        if (overBudgetCount > 0) {
            System.out.println("⚠️ 警告: " + overBudgetCount + " 个类别已超出预算");
        }

        if (nearBudgetCount > 0) {
            System.out.println("📝 注意: " + nearBudgetCount + " 个类别接近预算上限");
        }

        // 给出建议
        if (overallRate.doubleValue() > 90) {
            System.out.println("\n💡 建议: 总体支出已接近预算上限，建议控制后续支出");
        } else if (overallRate.doubleValue() <= 50) {
            System.out.println("\n✅ 良好: 预算控制良好，仍有较多预算空间");
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
        System.out.println("提示：输入 exit 可以退出统计，返回上级菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_PARENT;

            String monthYear = InputUtil.getMonthYear("请输入要统计的月份: ", exitType);

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
                    double percentage = amount.divide(total, 4, RoundingMode.HALF_UP).doubleValue() * 100;

                    System.out.printf("类别: %s, 比例: %.2f%%\n", category, percentage);
                }
            }
        } catch (UserExitException e) {
            System.out.println("已退出支出类别统计，返回上级菜单。");
        }
    }

    // 用户注册
    private static void register() {
        System.out.println("\n=== 用户注册 ===");
        System.out.println("提示：在任何步骤输入 exit 可以退出注册，返回主菜单");

        try {
            UserExitException.ExitType exitType = UserExitException.ExitType.RETURN_TO_MAIN;

            String username = InputUtil.getUsername("请输入用户名: ", exitType);
            String password = InputUtil.getPassword("请输入密码: ", exitType);
            String name = InputUtil.getName("请输入姓名: ", exitType);
            String studentId = InputUtil.getStudentId("请输入学号: ", exitType);
            String gender = InputUtil.getGender("请输入性别 (男/女): ", exitType);
            int age = InputUtil.getInt("请输入年龄: ", 1, 150, exitType);
            String phone = InputUtil.getPhone("请输入电话: ", exitType);
            String email = InputUtil.getEmail("请输入邮箱: ", exitType);

            User user = new User(username, password, name, studentId, gender, age, phone, email);

            if (userService.register(user)) {
                System.out.println("注册成功！");
            } else {
                System.out.println("注册失败！");
            }
        } catch (UserExitException e) {
            System.out.println("已退出注册，返回主菜单。");
        }
    }
}