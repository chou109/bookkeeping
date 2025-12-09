package com.smartconsumption.util;

/**
 * 用户退出操作异常
 * 当用户在输入过程中选择退出时抛出
 */
public class UserExitException extends RuntimeException {

    public enum ExitType {
        RETURN_TO_PARENT,    // 返回到上一级菜单
        RETURN_TO_MAIN,      // 返回到主菜单（未登录状态）
        RETURN_TO_USER_MENU, // 返回到用户菜单（已登录状态）
        LOGOUT               // 退出登录
    }

    private final ExitType exitType;

    public UserExitException(ExitType exitType) {
        super("用户退出操作");
        this.exitType = exitType;
    }

    public UserExitException(ExitType exitType, String message) {
        super(message);
        this.exitType = exitType;
    }

    public ExitType getExitType() {
        return exitType;
    }
}