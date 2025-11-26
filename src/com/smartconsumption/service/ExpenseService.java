package com.smartconsumption.service;

import com.smartconsumption.dao.ExpenseDAO;
import com.smartconsumption.entity.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExpenseService {
    private ExpenseDAO expenseDAO = new ExpenseDAO();

    // 添加支出
    public boolean addExpense(Expense expense) {
        return expenseDAO.addExpense(expense);
    }

    // 获取用户的支出记录
    public List<Expense> getExpensesByUserId(int userId) {
        return expenseDAO.getExpensesByUserId(userId);
    }

    // 获取用户指定日期范围的支出记录
    public List<Expense> getExpensesByDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        return expenseDAO.getExpensesByDateRange(userId, startDate, endDate);
    }

    // 计算用户总支出
    public BigDecimal getTotalExpense(int userId) {
        return expenseDAO.getTotalExpense(userId);
    }

    // 按类别统计支出
    public List<Object[]> getExpenseByCategory(int userId, String monthYear) {
        return expenseDAO.getExpenseByCategory(userId, monthYear);
    }

    // 删除支出记录
    public boolean deleteExpense(int expenseId) {
        return expenseDAO.deleteExpense(expenseId);
    }
}