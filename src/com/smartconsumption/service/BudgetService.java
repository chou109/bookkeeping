package com.smartconsumption.service;

import com.smartconsumption.dao.BudgetDAO;
import com.smartconsumption.entity.Budget;
import java.math.BigDecimal;
import java.util.List;

public class BudgetService {
    private BudgetDAO budgetDAO = new BudgetDAO();

    // 添加预算
    public boolean addBudget(Budget budget) {
        // 检查是否已存在相同类别和月份的预算
        if (budgetDAO.budgetExists(budget.getUserId(), budget.getCategory(), budget.getMonthYear())) {
            System.out.println("该类别在此月份的预算已存在！");
            return false;
        }

        return budgetDAO.addBudget(budget);
    }

    // 获取用户的预算
    public List<Budget> getBudgetsByUserIdAndMonth(int userId, String monthYear) {
        return budgetDAO.getBudgetsByUserIdAndMonth(userId, monthYear);
    }

    // 更新预算
    public boolean updateBudget(Budget budget) {
        return budgetDAO.updateBudget(budget);
    }

    // 删除预算
    public boolean deleteBudget(int budgetId) {
        return budgetDAO.deleteBudget(budgetId);
    }

    // 检查预算是否存在
    public boolean budgetExists(int userId, String category, String monthYear) {
        return budgetDAO.budgetExists(userId, category, monthYear);
    }

    // 新增：获取用户某月某类别的预算
    public BigDecimal getBudgetByCategory(int userId, String category, String monthYear) {
        List<Budget> budgets = budgetDAO.getBudgetsByUserIdAndMonth(userId, monthYear);

        for (Budget budget : budgets) {
            if (budget.getCategory().equals(category)) {
                return budget.getAmount();
            }
        }

        return null; // 如果没有设置该类别预算
    }
}