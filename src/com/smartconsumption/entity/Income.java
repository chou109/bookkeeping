package com.smartconsumption.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Income {
    private int incomeId;
    private int userId;
    private BigDecimal amount;
    private String source;
    private LocalDate incomeDate;
    private String description;
    private LocalDateTime createdDate;

    // 构造方法
    public Income() {}

    public Income(int userId, BigDecimal amount, String source, LocalDate incomeDate, String description) {
        this.userId = userId;
        this.amount = amount;
        this.source = source;
        this.incomeDate = incomeDate;
        this.description = description;
    }

    // Getter和Setter方法
    public int getIncomeId() { return incomeId; }
    public void setIncomeId(int incomeId) { this.incomeId = incomeId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDate getIncomeDate() { return incomeDate; }
    public void setIncomeDate(LocalDate incomeDate) { this.incomeDate = incomeDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return "收入ID: " + incomeId +
                "\n金额: " + amount +
                "\n来源: " + source +
                "\n日期: " + incomeDate +
                "\n描述: " + (description != null ? description : "无");
    }
}