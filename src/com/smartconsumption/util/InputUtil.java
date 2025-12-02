package com.smartconsumption.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    // 获取字符串输入
    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // 获取整数输入
    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的整数！");
            }
        }
    }

    // 获取BigDecimal输入（基础版本）
    public static BigDecimal getBigDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return new BigDecimal(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的金额！");
            }
        }
    }

    // 获取验证后的金额输入（带整数和小数位数限制）
    public static BigDecimal getValidAmount(String prompt, int maxIntegerDigits, int maxDecimalDigits) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();

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
            }
        }
    }

    // 获取验证后的金额输入（默认：整数最多8位，小数最多2位）
    public static BigDecimal getValidAmount(String prompt) {
        return getValidAmount(prompt, 8, 2);
    }

    // 格式化数字，添加千位分隔符
    private static String formatNumberWithCommas(String number) {
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

    // 格式化整数部分，添加千位分隔符
    private static String formatIntegerWithCommas(String integerStr) {
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

    // 获取日期输入（带完整验证）
    public static LocalDate getDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM-dd): ");
                String dateStr = scanner.nextLine().trim();

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
            }
        }
    }

    // 验证日期是否有效（包括闰年判断）
    private static boolean isValidDate(int year, int month, int day) {
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
        switch (month) {
            case 1:  // 一月
            case 3:  // 三月
            case 5:  // 五月
            case 7:  // 七月
            case 8:  // 八月
            case 10: // 十月
            case 12: // 十二月
                return 31;

            case 4:  // 四月
            case 6:  // 六月
            case 9:  // 九月
            case 11: // 十一月
                return 30;

            case 2:  // 二月
                return isLeapYear(year) ? 29 : 28;

            default:
                return 0; // 无效的月份
        }
    }

    // 判断是否为闰年
    public static boolean isLeapYear(int year) {
        // 闰年规则：
        // 1. 能被4整除但不能被100整除的年份是闰年
        // 2. 能被400整除的年份也是闰年
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // 获取日期验证的详细错误消息
    private static String getDateValidationMessage(int year, int month, int day) {
        StringBuilder message = new StringBuilder("日期无效！");

        // 检查年份
        if (year < 1900) {
            message.append(" 年份不得早于1900年。");
        }

        // 检查月份
        if (month < 1 || month > 12) {
            message.append(" 月份必须在1-12之间。");
        } else {
            // 获取该月的最大天数
            int maxDays = getMaxDaysInMonth(year, month);

            // 检查天数
            if (day < 1) {
                message.append(" 日期不能小于1。");
            } else if (day > maxDays) {
                message.append(" ").append(year).append("年").append(month).append("月最多有").append(maxDays).append("天。");

                // 提供特殊提示
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

    // 获取日期，并允许指定最小年份
    public static LocalDate getDateWithMinYear(String prompt, int minYear) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM-dd，年份不得早于" + minYear + "年): ");
                String dateStr = scanner.nextLine().trim();

                LocalDate date = LocalDate.parse(dateStr, formatter);

                // 验证年份不得早于指定年份
                if (date.getYear() < minYear) {
                    System.out.println("年份不得早于" + minYear + "年！");
                    continue;
                }

                // 验证日期是否有效
                if (!isValidDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth())) {
                    System.out.println(getDateValidationMessage(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
                    continue;
                }

                return date;
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请使用 yyyy-MM-dd 格式！例如: 2024-12-25");
            }
        }
    }

    // 获取日期，并允许指定日期范围
    public static LocalDate getDateInRange(String prompt, LocalDate minDate, LocalDate maxDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM-dd");
                if (minDate != null && maxDate != null) {
                    System.out.print("，范围: " + minDate + " 至 " + maxDate);
                }
                System.out.print("): ");

                String dateStr = scanner.nextLine().trim();
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

                // 验证日期范围
                if (minDate != null && date.isBefore(minDate)) {
                    System.out.println("日期不能早于 " + minDate + "！");
                    continue;
                }

                if (maxDate != null && date.isAfter(maxDate)) {
                    System.out.println("日期不能晚于 " + maxDate + "！");
                    continue;
                }

                return date;
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请使用 yyyy-MM-dd 格式！");
            }
        }
    }

    // 快速获取当前日期或未来日期（用于收入和支出记录）
    public static LocalDate getCurrentOrFutureDate(String prompt) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM-dd，建议: " + today + "): ");
                String dateStr = scanner.nextLine().trim();

                // 如果用户直接按回车，使用今天日期
                if (dateStr.isEmpty()) {
                    return today;
                }

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

                // 验证日期不能是未来太久（可选限制，例如最多到明年）
                LocalDate maxAllowedDate = today.plusYears(1);
                if (date.isAfter(maxAllowedDate)) {
                    System.out.println("日期不能超过一年后（" + maxAllowedDate + "）！");
                    continue;
                }

                return date;
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请使用 yyyy-MM-dd 格式！");
            }
        }
    }

    // 月份输入验证（增强版）
    public static String getMonthYear(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM): ");
                String monthYearStr = scanner.nextLine().trim();

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
            }
        }
    }

    // 获取月份，并允许指定最小年份
    public static String getMonthYearWithMinYear(String prompt, int minYear) {
        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM，年份不得早于" + minYear + "年): ");
                String monthYearStr = scanner.nextLine().trim();

                YearMonth yearMonth = YearMonth.parse(monthYearStr);

                // 验证年份不得早于指定年份
                if (yearMonth.getYear() < minYear) {
                    System.out.println("年份不得早于" + minYear + "年！");
                    continue;
                }

                // 验证月份在1-12之间
                if (yearMonth.getMonthValue() < 1 || yearMonth.getMonthValue() > 12) {
                    System.out.println("月份必须在1-12之间！");
                    continue;
                }

                return yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            } catch (DateTimeParseException e) {
                System.out.println("月份格式错误，请使用 yyyy-MM 格式！");
            }
        }
    }

    // 月份天数查询工具方法
    public static void printMonthDaysInfo(int year, int month) {
        if (month < 1 || month > 12) {
            System.out.println("无效的月份！");
            return;
        }

        int maxDays = getMaxDaysInMonth(year, month);
        String monthName = getMonthName(month);
        boolean isLeap = isLeapYear(year);

        System.out.println(year + "年" + monthName + "（" + month + "月）有" + maxDays + "天");
        if (month == 2) {
            System.out.println("该年" + (isLeap ? "是" : "不是") + "闰年");
        }
    }

    // 获取月份名称
    private static String getMonthName(int month) {
        switch (month) {
            case 1: return "一月";
            case 2: return "二月";
            case 3: return "三月";
            case 4: return "四月";
            case 5: return "五月";
            case 6: return "六月";
            case 7: return "七月";
            case 8: return "八月";
            case 9: return "九月";
            case 10: return "十月";
            case 11: return "十一月";
            case 12: return "十二月";
            default: return "未知月份";
        }
    }

    // 获取选择输入（用于菜单选择等）
    public static int getChoice(String prompt, int min, int max) {
        while (true) {
            int choice = getInt(prompt);
            if (choice >= min && choice <= max) {
                return choice;
            } else {
                System.out.println("请选择 " + min + " 到 " + max + " 之间的数字！");
            }
        }
    }

    // 获取是/否输入
    public static boolean getYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes") || input.equals("是")) {
                return true;
            } else if (input.equals("n") || input.equals("no") || input.equals("否")) {
                return false;
            } else {
                System.out.println("请输入 y/n 或 是/否！");
            }
        }
    }

    // 获取性别输入
    public static String getGender(String prompt) {
        while (true) {
            System.out.print(prompt + " (男/女): ");
            String gender = scanner.nextLine().trim();

            if (gender.equals("男") || gender.equals("女")) {
                return gender;
            } else {
                System.out.println("请输入 男 或 女！");
            }
        }
    }

    // 获取邮箱输入（简单验证）
    public static String getEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            String email = scanner.nextLine().trim();

            if (email.isEmpty()) {
                return email; // 允许为空
            }

            // 简单的邮箱格式验证
            if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return email;
            } else {
                System.out.println("邮箱格式不正确，请重新输入！");
            }
        }
    }

    // 获取手机号输入（简单验证）
    public static String getPhone(String prompt) {
        while (true) {
            System.out.print(prompt);
            String phone = scanner.nextLine().trim();

            if (phone.isEmpty()) {
                return phone; // 允许为空
            }

            // 简单的手机号格式验证（11位数字）
            if (phone.matches("\\d{11}")) {
                return phone;
            } else {
                System.out.println("手机号必须是11位数字！");
            }
        }
    }

    // 关闭Scanner（程序结束时调用）
    public static void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}