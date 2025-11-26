package com.smartconsumption.dao;

import com.smartconsumption.entity.Income;
import com.smartconsumption.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {

    // 添加收入记录
    public boolean addIncome(Income income) {
        String sql = "INSERT INTO income (user_id, amount, source, income_date, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, income.getUserId());
            pstmt.setBigDecimal(2, income.getAmount());
            pstmt.setString(3, income.getSource());
            pstmt.setDate(4, Date.valueOf(income.getIncomeDate()));
            pstmt.setString(5, income.getDescription());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据用户ID获取收入记录
    public List<Income> getIncomesByUserId(int userId) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM income WHERE user_id = ? ORDER BY income_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Income income = new Income();
                income.setIncomeId(rs.getInt("income_id"));
                income.setUserId(rs.getInt("user_id"));
                income.setAmount(rs.getBigDecimal("amount"));
                income.setSource(rs.getString("source"));
                income.setIncomeDate(rs.getDate("income_date").toLocalDate());
                income.setDescription(rs.getString("description"));
                income.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());

                incomes.add(income);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return incomes;
    }

    // 根据日期范围获取收入记录
    public List<Income> getIncomesByDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM income WHERE user_id = ? AND income_date BETWEEN ? AND ? ORDER BY income_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(startDate));
            pstmt.setDate(3, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Income income = new Income();
                income.setIncomeId(rs.getInt("income_id"));
                income.setUserId(rs.getInt("user_id"));
                income.setAmount(rs.getBigDecimal("amount"));
                income.setSource(rs.getString("source"));
                income.setIncomeDate(rs.getDate("income_date").toLocalDate());
                income.setDescription(rs.getString("description"));
                income.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());

                incomes.add(income);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return incomes;
    }

    // 计算总收入
    public BigDecimal getTotalIncome(int userId) {
        String sql = "SELECT SUM(amount) as total FROM income WHERE user_id = ?";
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

    // 删除收入记录
    public boolean deleteIncome(int incomeId) {
        String sql = "DELETE FROM income WHERE income_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, incomeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}