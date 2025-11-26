package com.smartconsumption.dao;

import com.smartconsumption.entity.Budget;
import com.smartconsumption.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {

    // 添加预算
    public boolean addBudget(Budget budget) {
        String sql = "INSERT INTO budget (user_id, category, amount, month_year) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, budget.getUserId());
            pstmt.setString(2, budget.getCategory());
            pstmt.setBigDecimal(3, budget.getAmount());
            pstmt.setString(4, budget.getMonthYear());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据用户ID和月份获取预算
    public List<Budget> getBudgetsByUserIdAndMonth(int userId, String monthYear) {
        List<Budget> budgets = new ArrayList<>();
        String sql = "SELECT * FROM budget WHERE user_id = ? AND month_year = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, monthYear);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Budget budget = new Budget();
                budget.setBudgetId(rs.getInt("budget_id"));
                budget.setUserId(rs.getInt("user_id"));
                budget.setCategory(rs.getString("category"));
                budget.setAmount(rs.getBigDecimal("amount"));
                budget.setMonthYear(rs.getString("month_year"));
                budget.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());

                budgets.add(budget);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return budgets;
    }

    // 更新预算
    public boolean updateBudget(Budget budget) {
        String sql = "UPDATE budget SET amount = ? WHERE budget_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, budget.getAmount());
            pstmt.setInt(2, budget.getBudgetId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除预算
    public boolean deleteBudget(int budgetId) {
        String sql = "DELETE FROM budget WHERE budget_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, budgetId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 检查预算是否存在
    public boolean budgetExists(int userId, String category, String monthYear) {
        String sql = "SELECT COUNT(*) FROM budget WHERE user_id = ? AND category = ? AND month_year = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, category);
            pstmt.setString(3, monthYear);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}