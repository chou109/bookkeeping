package com.smartconsumption.service;

import com.smartconsumption.dao.UserDAO;
import com.smartconsumption.entity.User;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    // 用户注册
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            System.out.println("用户名已存在！");
            return false;
        }

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
}