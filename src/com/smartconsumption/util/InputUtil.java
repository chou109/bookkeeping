package com.smartconsumption.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    // 退出命令列表
    private static final String[] EXIT_COMMANDS = {"exit", "quit", "back", "返回", "退出"};

    // 检查是否为退出命令
    public static boolean isExitCommand(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        String lowerInput = input.trim().toLowerCase();
        for (String command : EXIT_COMMANDS) {
            if (command.equals(lowerInput)) {
                return true;
            }
        }
        return false;
    }

    // 检查并抛出退出异常
    private static void checkForExit(String input, UserExitException.ExitType exitType) {
        if (isExitCommand(input)) {
            throw new UserExitException(exitType);
        }
    }

    // 获取角色选择输入
    public static String getRole(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (管理员/用户，输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                if (input.equals("管理员") || input.equals("用户")) {
                    return input;
                } else {
                    System.out.println("角色只能输入'管理员'或'用户'！");
                }
            } catch (UserExitException e) {
                throw e;
            }
        }
    }

    // 获取字符串输入（带退出选项）
    public static String getString(String prompt, int maxLength, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                if (input.isEmpty()) {
                    System.out.println("输入不能为空！");
                    continue;
                }

                if (input.length() > maxLength) {
                    System.out.println("输入过长！最多允许 " + maxLength + " 个字符。");
                    continue;
                }

                return input;
            } catch (UserExitException e) {
                throw e; // 重新抛出，让上层处理
            }
        }
    }

    // 获取字符串输入（可选，带退出选项）
    public static String getStringOptional(String prompt, int maxLength, UserExitException.ExitType exitType) {
        System.out.print(prompt + " (输入 exit 退出): ");
        String input = scanner.nextLine().trim();

        // 检查是否为退出命令
        checkForExit(input, exitType);

        if (!input.isEmpty() && input.length() > maxLength) {
            System.out.println("输入过长！最多允许 " + maxLength + " 个字符。");
            // 递归调用直到输入正确
            return getStringOptional(prompt, maxLength, exitType);
        }

        return input;
    }

    // 获取整数输入（带退出选项）
    public static int getInt(String prompt, int min, int max, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                int value = Integer.parseInt(input);

                if (value < min) {
                    System.out.println("输入值不能小于 " + min + "！");
                    continue;
                }

                if (value > max) {
                    System.out.println("输入值不能大于 " + max + "！");
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的整数！");
            } catch (UserExitException e) {
                throw e; // 重新抛出，让上层处理
            }
        }
    }

    // 获取验证后的金额输入（带退出选项）
    public static BigDecimal getValidAmount(String prompt, int maxIntegerDigits, int maxDecimalDigits, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                // 检查输入是否为空
                if (input.isEmpty()) {
                    System.out.println("金额不能为空！");
                    continue;
                }

                // 转换为BigDecimal
                BigDecimal amount = new BigDecimal(input);

                // 检查是否为负数或零
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("金额必须大于0！");
                    continue;
                }

                // 分离整数和小数部分
                String[] parts = input.split("\\.");
                String integerPart = parts[0];
                String decimalPart = parts.length > 1 ? parts[1] : "";

                // 检查整数部分长度
                if (integerPart.length() > maxIntegerDigits) {
                    System.out.println("整数部分不能超过" + maxIntegerDigits + "位！当前：" + integerPart.length() + "位");

                    // 计算最大整数
                    StringBuilder maxIntBuilder = new StringBuilder();
                    for (int i = 0; i < maxIntegerDigits; i++) {
                        maxIntBuilder.append("9");
                    }
                    System.out.println("允许的最大整数：" + formatNumberWithCommas(maxIntBuilder.toString()));
                    continue;
                }

                // 检查小数部分长度
                if (decimalPart.length() > maxDecimalDigits) {
                    System.out.println("小数部分不能超过" + maxDecimalDigits + "位！当前：" + decimalPart.length() + "位");
                    continue;
                }

                // 检查总金额是否超过限制
                StringBuilder maxAmountStr = new StringBuilder();
                for (int i = 0; i < maxIntegerDigits; i++) {
                    maxAmountStr.append("9");
                }
                maxAmountStr.append(".");
                for (int i = 0; i < maxDecimalDigits; i++) {
                    maxAmountStr.append("9");
                }

                BigDecimal maxAmount = new BigDecimal(maxAmountStr.toString());
                if (amount.compareTo(maxAmount) > 0) {
                    System.out.println("金额太大！最大允许：" + formatNumberWithCommas(maxAmountStr.toString()));
                    continue;
                }

                return amount;
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的金额格式！例如：1000 或 1000.50");
            } catch (UserExitException e) {
                throw e; // 重新抛出，让上层处理
            }
        }
    }

    // 获取日期输入（带退出选项）
    public static LocalDate getDate(String prompt, UserExitException.ExitType exitType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM-dd，输入 exit 退出): ");
                String dateStr = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(dateStr, exitType);

                // 尝试解析日期
                LocalDate date = LocalDate.parse(dateStr, formatter);

                // 验证年份不得早于1900年
                if (date.getYear() < 1900) {
                    System.out.println("年份不得早于1900年！");
                    continue;
                }

                // 验证月份在1-12之间
                if (date.getMonthValue() < 1 || date.getMonthValue() > 12) {
                    System.out.println("月份必须在1-12之间！");
                    continue;
                }

                // 验证日期是否有效（根据月份和闰年）
                if (!isValidDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth())) {
                    System.out.println(getDateValidationMessage(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
                    continue;
                }

                return date;
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请使用 yyyy-MM-dd 格式！例如: 2024-12-25");
            } catch (UserExitException e) {
                throw e; // 重新抛出，让上层处理
            }
        }
    }

    // 获取只能选择当前日期及之前的日期（带退出选项）
    public static LocalDate getPastOrCurrentDate(String prompt, UserExitException.ExitType exitType) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM-dd，不能晚于今天 " + today + "，输入 exit 退出): ");
                String dateStr = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(dateStr, exitType);

                LocalDate date = LocalDate.parse(dateStr, formatter);

                // 验证年份不得早于1900年
                if (date.getYear() < 1900) {
                    System.out.println("年份不得早于1900年！");
                    continue;
                }

                // 验证日期是否有效
                if (!isValidDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth())) {
                    System.out.println(getDateValidationMessage(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
                    continue;
                }

                // 验证日期不能晚于当前日期
                if (date.isAfter(today)) {
                    System.out.println("日期不能晚于今天！");
                    continue;
                }

                return date;
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请使用 yyyy-MM-dd 格式！");
            } catch (UserExitException e) {
                throw e; // 重新抛出，让上层处理
            }
        }
    }

    // 获取月份输入（带退出选项）
    public static String getMonthYear(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM，输入 exit 退出): ");
                String monthYearStr = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(monthYearStr, exitType);

                // 使用 YearMonth 来验证月份格式
                YearMonth yearMonth = YearMonth.parse(monthYearStr);

                // 验证年份不得早于1900年
                if (yearMonth.getYear() < 1900) {
                    System.out.println("年份不得早于1900年！");
                    continue;
                }

                // 验证月份在1-12之间
                if (yearMonth.getMonthValue() < 1 || yearMonth.getMonthValue() > 12) {
                    System.out.println("月份必须在1-12之间！");
                    continue;
                }

                // 验证通过，返回格式化的字符串
                return yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            } catch (DateTimeParseException e) {
                System.out.println("月份格式错误，请使用 yyyy-MM 格式！例如: 2024-12");
            } catch (UserExitException e) {
                throw e; // 重新抛出，让上层处理
            }
        }
    }

    // 获取性别输入（带退出选项）
    public static String getGender(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            System.out.print(prompt + " (男/女，输入 exit 退出): ");
            String input = scanner.nextLine().trim();

            // 检查是否为退出命令
            try {
                checkForExit(input, exitType);
            } catch (UserExitException e) {
                throw e;
            }

            if (input.equals("男") || input.equals("女")) {
                return input;
            } else {
                System.out.println("性别只能输入'男'或'女'！");
            }
        }
    }

    // 获取学号输入（带退出选项）
    public static String getStudentId(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                if (input.isEmpty()) {
                    System.out.println("学号不能为空！");
                    continue;
                }

                // 长度限制（根据数据库VARCHAR(20)）
                if (input.length() > 20) {
                    System.out.println("学号过长！最多允许20个字符。");
                    continue;
                }

                // 检查是否只包含允许的字符（字母、数字、下划线、短横线）
                if (!input.matches("^[a-zA-Z0-9_-]+$")) {
                    System.out.println("学号只能包含字母、数字、下划线(_)和短横线(-)！");
                    continue;
                }

                return input;
            } catch (UserExitException e) {
                throw e;
            }
        }
    }

    // 获取电话号码输入（带退出选项）
    public static String getPhone(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                if (input.isEmpty()) {
                    // 允许为空
                    return input;
                }

                // 长度限制（根据数据库VARCHAR(15)）
                if (input.length() > 15) {
                    System.out.println("电话号码过长！最多允许15个字符。");
                    continue;
                }

                // 检查是否只包含数字、空格、括号、短横线等常见电话字符
                if (!input.matches("^[0-9\\s\\+\\-\\(\\)]+$")) {
                    System.out.println("电话号码只能包含数字、空格、+、-、(、)等字符！");
                    continue;
                }

                // 移除所有非数字字符，检查纯数字长度
                String digitsOnly = input.replaceAll("[^0-9]", "");
                if (digitsOnly.length() < 7) {
                    System.out.println("电话号码太短！至少需要7位数字。");
                    continue;
                }

                if (digitsOnly.length() > 15) {
                    System.out.println("电话号码太长！最多15位数字。");
                    continue;
                }

                return input;
            } catch (UserExitException e) {
                throw e;
            }
        }
    }

    // 获取邮箱输入（带退出选项）
    public static String getEmail(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                if (input.isEmpty()) {
                    // 允许为空
                    return input;
                }

                // 长度限制（根据数据库VARCHAR(100)）
                if (input.length() > 100) {
                    System.out.println("邮箱地址过长！最多允许100个字符。");
                    continue;
                }

                // 简单的邮箱格式验证
                if (!input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    System.out.println("邮箱格式不正确！请使用正确的邮箱格式，如：example@domain.com");
                    continue;
                }

                return input;
            } catch (UserExitException e) {
                throw e;
            }
        }
    }

    // 获取用户名输入（带退出选项）
    public static String getUsername(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                if (input.isEmpty()) {
                    System.out.println("用户名不能为空！");
                    continue;
                }

                // 长度限制（根据数据库VARCHAR(50)）
                if (input.length() > 50) {
                    System.out.println("用户名过长！最多允许50个字符。");
                    continue;
                }

                // 检查是否只包含允许的字符（字母、数字、下划线）
                if (!input.matches("^[a-zA-Z0-9_]+$")) {
                    System.out.println("用户名只能包含字母、数字和下划线(_)！");
                    continue;
                }

                // 检查用户名长度
                if (input.length() < 3) {
                    System.out.println("用户名太短！至少需要3个字符。");
                    continue;
                }

                return input;
            } catch (UserExitException e) {
                throw e;
            }
        }
    }

    // 获取密码输入（带退出选项）
    public static String getPassword(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                if (input.isEmpty()) {
                    System.out.println("密码不能为空！");
                    continue;
                }

                // 长度限制（根据数据库VARCHAR(100)）
                if (input.length() > 100) {
                    System.out.println("密码过长！最多允许100个字符。");
                    continue;
                }

                // 检查密码长度
                if (input.length() < 6) {
                    System.out.println("密码太短！至少需要6个字符。");
                    continue;
                }

                return input;
            } catch (UserExitException e) {
                throw e;
            }
        }
    }

    // 获取姓名输入（带退出选项）
    public static String getName(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (输入 exit 退出): ");
                String input = scanner.nextLine().trim();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                if (input.isEmpty()) {
                    System.out.println("姓名不能为空！");
                    continue;
                }

                // 长度限制（根据数据库VARCHAR(50)）
                if (input.length() > 50) {
                    System.out.println("姓名过长！最多允许50个字符。");
                    continue;
                }

                // 检查姓名是否只包含中文字母、空格和点号
                if (!input.matches("^[\\u4e00-\\u9fa5a-zA-Z\\s·.]+$")) {
                    System.out.println("姓名只能包含中文、英文字母、空格和点号！");
                    continue;
                }

                return input;
            } catch (UserExitException e) {
                throw e;
            }
        }
    }

    // 获取是/否输入（带退出选项）
    public static boolean getYesNo(String prompt, UserExitException.ExitType exitType) {
        while (true) {
            try {
                System.out.print(prompt + " (y/n，输入 exit 退出): ");
                String input = scanner.nextLine().trim().toLowerCase();

                // 检查是否为退出命令
                checkForExit(input, exitType);

                if (input.equals("y") || input.equals("yes") || input.equals("是")) {
                    return true;
                } else if (input.equals("n") || input.equals("no") || input.equals("否")) {
                    return false;
                } else {
                    System.out.println("请输入 y/n 或 是/否！");
                }
            } catch (UserExitException e) {
                throw e;
            }
        }
    }

    // 辅助方法：获取正确的退出类型
    public static UserExitException.ExitType getExitType(boolean isLoggedIn) {
        if (isLoggedIn) {
            return UserExitException.ExitType.RETURN_TO_USER_MENU;
        } else {
            return UserExitException.ExitType.RETURN_TO_MAIN;
        }
    }

    // ========== 原有的辅助方法保持不变 ==========

    // 格式化数字，添加千位分隔符
    private static String formatNumberWithCommas(String number) {
        // ... 原有代码保持不变 ...
        try {
            // 如果是小数，分别处理整数和小数部分
            if (number.contains(".")) {
                String[] parts = number.split("\\.");
                return formatIntegerWithCommas(parts[0]) + "." + parts[1];
            } else {
                return formatIntegerWithCommas(number);
            }
        } catch (Exception e) {
            return number;
        }
    }

    private static String formatIntegerWithCommas(String integerStr) {
        // ... 原有代码保持不变 ...
        if (integerStr.length() <= 3) {
            return integerStr;
        }

        StringBuilder result = new StringBuilder();
        int length = integerStr.length();

        for (int i = 0; i < length; i++) {
            if ((length - i) % 3 == 0 && i > 0) {
                result.append(",");
            }
            result.append(integerStr.charAt(i));
        }

        return result.toString();
    }

    // 验证日期是否有效（包括闰年判断）
    private static boolean isValidDate(int year, int month, int day) {
        // ... 原有代码保持不变 ...
        // 检查月份是否在1-12之间
        if (month < 1 || month > 12) {
            return false;
        }

        // 检查年份是否有效
        if (year < 1900) {
            return false;
        }

        // 获取该月的最大天数
        int maxDays = getMaxDaysInMonth(year, month);

        // 检查日期是否在有效范围内
        return day >= 1 && day <= maxDays;
    }

    // 获取指定月份的最大天数（考虑闰年）
    private static int getMaxDaysInMonth(int year, int month) {
        // ... 原有代码保持不变 ...
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return isLeapYear(year) ? 29 : 28;
            default:
                return 0;
        }
    }

    // 判断是否为闰年
    public static boolean isLeapYear(int year) {
        // ... 原有代码保持不变 ...
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // 获取日期验证的详细错误消息
    private static String getDateValidationMessage(int year, int month, int day) {
        // ... 原有代码保持不变 ...
        StringBuilder message = new StringBuilder("日期无效！");

        if (year < 1900) {
            message.append(" 年份不得早于1900年。");
        }

        if (month < 1 || month > 12) {
            message.append(" 月份必须在1-12之间。");
        } else {
            int maxDays = getMaxDaysInMonth(year, month);

            if (day < 1) {
                message.append(" 日期不能小于1。");
            } else if (day > maxDays) {
                message.append(" ").append(year).append("年").append(month).append("月最多有").append(maxDays).append("天。");

                if (month == 2) {
                    message.append(" 二月");
                    if (isLeapYear(year)) {
                        message.append("是闰年，最多29天。");
                    } else {
                        message.append("不是闰年，最多28天。");
                    }
                }
            }
        }

        return message.toString();
    }
}