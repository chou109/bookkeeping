package com.smartconsumption.service;

import com.smartconsumption.dao.IncomeDAO;
import com.smartconsumption.entity.Income;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class IncomeService {
    private IncomeDAO incomeDAO = new IncomeDAO();

    // 添加收入
    public boolean addIncome(Income income) {
        return incomeDAO.addIncome(income);
    }

    // 获取用户的收入记录
    public List<Income> getIncomesByUserId(int userId) {
        return incomeDAO.getIncomesByUserId(userId);
    }

    // 获取用户指定日期范围的收入记录
    public List<Income> getIncomesByDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        return incomeDAO.getIncomesByDateRange(userId, startDate, endDate);
    }

    // 计算用户总收入
    public BigDecimal getTotalIncome(int userId) {
        return incomeDAO.getTotalIncome(userId);
    }

    // 删除收入记录
    public boolean deleteIncome(int incomeId) {
        return incomeDAO.deleteIncome(incomeId);
    }
}