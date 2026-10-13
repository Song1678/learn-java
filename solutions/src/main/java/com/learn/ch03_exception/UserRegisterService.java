package com.learn.ch03_exception;

import java.util.HashSet;
import java.util.Set;

/**
 * 练习 5：用异常做参数校验（业务代码里最常见的写法）
 */
public class UserRegisterService {

    /** 已经存在的用户名（模拟数据库） */
    private final Set<String> existing = new HashSet<>(Set.of("admin", "root"));

    /**
     * 注册用户，校验通过后把用户名加入 existing 并返回 "注册成功"。
     * 按顺序校验，第一个不满足的就抛 ServiceException：
     * <ol>
     *   <li>username 为 null 或空白：ServiceException("用户名不能为空", 400)</li>
     *   <li>username 长度不在 4~16：ServiceException("用户名长度必须在4到16之间", 400)</li>
     *   <li>password 为 null 或长度小于 6：ServiceException("密码长度不能小于6", 400)</li>
     *   <li>password 不是同时包含字母和数字：ServiceException("密码必须同时包含字母和数字", 400)</li>
     *   <li>username 已存在：ServiceException("用户名已存在", 409)</li>
     * </ol>
     * 提示：Character.isLetter / Character.isDigit
     */
    public String register(String username, String password) {
        // @answer
        if (username == null || username.isBlank()) {
            throw new ServiceException("用户名不能为空", 400);
        }
        if (username.length() < 4 || username.length() > 16) {
            throw new ServiceException("用户名长度必须在4到16之间", 400);
        }
        if (password == null || password.length() < 6) {
            throw new ServiceException("密码长度不能小于6", 400);
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        if (!hasLetter || !hasDigit) {
            throw new ServiceException("密码必须同时包含字母和数字", 400);
        }
        if (existing.contains(username)) {
            throw new ServiceException("用户名已存在", 409);
        }
        existing.add(username);
        return "注册成功";
        // @end
    }
}
