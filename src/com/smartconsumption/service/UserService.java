package com.smartconsumption.service;

import com.smartconsumption.dao.UserDAO;
import com.smartconsumption.entity.User;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    // 用户注册（只能注册普通用户）
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            System.out.println("用户名已存在！");
            return false;
        }

        // 确保注册的是普通用户，不能注册管理员
        user.setRole("user");
        return userDAO.addUser(user);
    }

    // 用户登录
    public User login(String username, String password) {
        return userDAO.validateLogin(username, password);
    }

    // 获取所有用户
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // 获取所有普通用户
    public List<User> getAllRegularUsers() {
        return userDAO.getAllRegularUsers();
    }

    // 更新用户信息
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    // 删除用户
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }

    // 根据用户名获取用户
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    // 新增：添加管理员用户
    public boolean addAdminUser(String username, String password, String name) {
        // 检查用户名是否已存在
        if (userDAO.getUserByUsername(username) != null) {
            System.out.println("用户名已存在！");
            return false;
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setName(name);
        admin.setRole("admin");

        return userDAO.addAdmin(admin);
    }

    // 新增：检查是否为管理员
    public boolean isAdmin(User user) {
        return user != null && "admin".equals(user.getRole());
    }

    // 新增：获取用户数量统计
    public int getUserCount() {
        return userDAO.getAllUsers().size();
    }

    // 新增：获取普通用户数量
    public int getRegularUserCount() {
        return userDAO.getAllRegularUsers().size();
    }

    // 新增：获取管理员数量
    public int getAdminCount() {
        return userDAO.getAllUsers().stream()
                .filter(user -> "admin".equals(user.getRole()))
                .mapToInt(user -> 1)
                .sum();
    }
}