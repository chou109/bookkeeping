package com.smartconsumption.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Expense {
    private int expenseId;
    private int userId;
    private BigDecimal amount;
    private String category;
    private LocalDate expenseDate;
    private String description;
    private LocalDateTime createdDate;

    // 构造方法
    public Expense() {}

    public Expense(int userId, BigDecimal amount, String category, LocalDate expenseDate, String description) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
        this.description = description;
    }

    // Getter和Setter方法
    public int getExpenseId() { return expenseId; }
    public void setExpenseId(int expenseId) { this.expenseId = expenseId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return "支出ID: " + expenseId +
                "\n金额: " + amount +
                "\n类别: " + category +
                "\n日期: " + expenseDate +
                "\n描述: " + (description != null ? description : "无");
    }
}