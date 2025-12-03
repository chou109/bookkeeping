package com.smartconsumption.dao;

import com.smartconsumption.entity.Expense;
import com.smartconsumption.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    // 添加支出记录
    public boolean addExpense(Expense expense) {
        String sql = "INSERT INTO expense (user_id, amount, category, expense_date, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, expense.getUserId());
            pstmt.setBigDecimal(2, expense.getAmount());
            pstmt.setString(3, expense.getCategory());
            pstmt.setDate(4, Date.valueOf(expense.getExpenseDate()));
            pstmt.setString(5, expense.getDescription());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据用户ID获取支出记录
    public List<Expense> getExpensesByUserId(int userId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expense WHERE user_id = ? ORDER BY expense_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Expense expense = new Expense();
                expense.setExpenseId(rs.getInt("expense_id"));
                expense.setUserId(rs.getInt("user_id"));
                expense.setAmount(rs.getBigDecimal("amount"));
                expense.setCategory(rs.getString("category"));
                expense.setExpenseDate(rs.getDate("expense_date").toLocalDate());
                expense.setDescription(rs.getString("description"));
                expense.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());

                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    // 根据日期范围获取支出记录
    public List<Expense> getExpensesByDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expense WHERE user_id = ? AND expense_date BETWEEN ? AND ? ORDER BY expense_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(startDate));
            pstmt.setDate(3, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Expense expense = new Expense();
                expense.setExpenseId(rs.getInt("expense_id"));
                expense.setUserId(rs.getInt("user_id"));
                expense.setAmount(rs.getBigDecimal("amount"));
                expense.setCategory(rs.getString("category"));
                expense.setExpenseDate(rs.getDate("expense_date").toLocalDate());
                expense.setDescription(rs.getString("description"));
                expense.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());

                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    // 计算总支出
    public BigDecimal getTotalExpense(int userId) {
        String sql = "SELECT SUM(amount) as total FROM expense WHERE user_id = ?";
        BigDecimal total = BigDecimal.ZERO;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                total = rs.getBigDecimal("total");
                if (total == null) {
                    total = BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    // 按类别统计支出
    public List<Object[]> getExpenseByCategory(int userId, String monthYear) {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT category, SUM(amount) as total FROM expense WHERE user_id = ? AND DATE_FORMAT(expense_date, '%Y-%m') = ? GROUP BY category";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, monthYear);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getString("category");
                row[1] = rs.getBigDecimal("total");
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // 删除支出记录
    public boolean deleteExpense(int expenseId) {
        String sql = "DELETE FROM expense WHERE expense_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, expenseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 新增：获取用户某月某类别的支出总额
    public BigDecimal getMonthlyExpenseByCategory(int userId, String category, String monthYear) {
        String sql = "SELECT SUM(amount) as total FROM expense WHERE user_id = ? AND category = ? AND DATE_FORMAT(expense_date, '%Y-%m') = ?";
        BigDecimal total = BigDecimal.ZERO;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, category);
            pstmt.setString(3, monthYear);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                total = rs.getBigDecimal("total");
                if (total == null) {
                    total = BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
}