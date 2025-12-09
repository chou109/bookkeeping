package com.smartconsumption.entity;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String password;
    private String name;
    private String studentId;
    private String gender;
    private int age;
    private String phone;
    private String email;
    private String role; // 新增角色字段：admin 或 user
    private LocalDateTime createdDate;

    // 构造方法
    public User() {}

    public User(String username, String password, String name, String studentId,
                String gender, int age, String phone, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.studentId = studentId;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.role = "user"; // 默认角色为普通用户
    }

    // 新增：管理员构造方法
    public User(String username, String password, String name, String studentId,
                String gender, int age, String phone, String email, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.studentId = studentId;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }

    // Getter和Setter方法
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; } // 新增
    public void setRole(String role) { this.role = role; } // 新增

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return "用户ID: " + userId +
                "\n用户名: " + username +
                "\n角色: " + role +
                "\n姓名: " + name +
                "\n学号: " + studentId +
                "\n性别: " + gender +
                "\n年龄: " + age +
                "\n电话: " + phone +
                "\n邮箱: " + email +
                "\n注册时间: " + createdDate;
    }

    // 新增：判断是否为管理员
    public boolean isAdmin() {
        return "admin".equals(role);
    }

    // 新增：判断是否为普通用户
    public boolean isUser() {
        return "user".equals(role);
    }
}