package com.smartconsumption.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/smart_consumption_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            // 对于MySQL 8.0+
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // 只记录错误，不打印到控制台
            System.err.println("MySQL JDBC驱动加载失败");
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("数据库连接失败");
            return null;
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // 忽略关闭时的错误
            }
        }
    }

    // 测试数据库连接（仅用于调试）
    public static void testConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("数据库连接测试成功！");
            closeConnection(conn);
        } else {
            System.out.println("数据库连接测试失败！");
        }
    }

    // 静默测试数据库连接（不输出任何信息）
    public static boolean testConnectionSilently() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return conn != null;
        } catch (SQLException e) {
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}