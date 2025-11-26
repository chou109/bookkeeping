package com.smartconsumption.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Budget {
    private int budgetId;
    private int userId;
    private String category;
    private BigDecimal amount;
    private String monthYear; // 格式: YYYY-MM
    private LocalDateTime createdDate;

    // 构造方法
    public Budget() {}

    public Budget(int userId, String category, BigDecimal amount, String monthYear) {
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.monthYear = monthYear;
    }

    // Getter和Setter方法
    public int getBudgetId() { return budgetId; }
    public void setBudgetId(int budgetId) { this.budgetId = budgetId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getMonthYear() { return monthYear; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return "预算ID: " + budgetId +
                "\n类别: " + category +
                "\n金额: " + amount +
                "\n月份: " + monthYear;
    }
}