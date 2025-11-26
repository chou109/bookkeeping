package com.smartconsumption.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

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

    public static LocalDate getDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM-dd): ");
                String dateStr = scanner.nextLine().trim();
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请使用 yyyy-MM-dd 格式！例如: 2024-12-25");
            }
        }
    }

    public static String getMonthYear(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (格式: yyyy-MM): ");
                String monthYearStr = scanner.nextLine().trim();

                // 使用 YearMonth 来验证月份格式（推荐方法）
                YearMonth yearMonth = YearMonth.parse(monthYearStr);

                // 验证通过，返回格式化的字符串
                return yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            } catch (DateTimeParseException e) {
                System.out.println("月份格式错误，请使用 yyyy-MM 格式！例如: 2024-12");
            }
        }
    }
}