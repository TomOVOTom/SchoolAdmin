package org.example.springstudentmanagementsystem.service.impl;

import org.example.springstudentmanagementsystem.dao.UserDao;
import org.example.springstudentmanagementsystem.entity.User;
import org.example.springstudentmanagementsystem.service.EmailService;
import org.example.springstudentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    // UserDao 对象，通过 Spring 注入
    @Autowired
    private UserDao userDao;

    
    // setter 方法用于依赖注入
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Boolean addUser(User user) {
        // 可以在这里添加业务逻辑，比如验证用户数据
        if (user == null || user.getId() == null || user.getId().trim().isEmpty()) {
            return false;
        }
        return userDao.addUser(user);
    }

    @Override
    public Boolean deleteUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        return userDao.deleteUser(userId);
    }

    @Override
    public Boolean updateUser(User user) {
        if (user == null || user.getId() == null || user.getId().trim().isEmpty()) {
            return false;
        }

        // 获取原用户信息
        User existingUser = userDao.findUserById(user.getId());
        if (existingUser == null) {
            return false;
        }

        // 如果用户名发生变化，检查新用户名是否已存在
        if (!existingUser.getUsername().equals(user.getUsername())
                && userDao.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        return userDao.updateUser(user);
    }

    @Override
    public List<User> findAllUser() {
        return userDao.findAllUser();
    }

    @Override
    public User findUserById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return userDao.findUserById(id);
    }


    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        try {
            List<User> users = userDao.findByUsernameAndPassword(username, password);
            if (!users.isEmpty()) {
                User user = users.get(0);
                // 设置默认状态
                if (user.getStatus() == null) {
                    user.setStatus(1);
                }
                // 更新最后登录时间
                Date now = new Date();
                user.setLastLoginTime(now);
                userDao.updateLastLoginTime(user.getId(), now);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean register(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return false;
        }
        // 检查用户名是否已存在
        if (userDao.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        // 生成随机ID
        user.setId(String.valueOf(System.currentTimeMillis()));
        user.setStatus(1); // 设置默认状态为可用
        user.setRole("USER"); // 设置默认角色
        return userDao.addUser(user);
    }

    @Override
    public Boolean changePassword(String userId, String oldPassword, String newPassword) {
        // 实现修改密码逻辑
        User user = userDao.findUserById(userId);
        if (user == null || !user.getPassword().equals(oldPassword)) {
            return false;
        }
        user.setPassword(newPassword);
        return userDao.updateUser(user);
    }

    @Override
    public Boolean disableUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        return userDao.disableUser(userId);
    }

    @Override
    public Boolean updateLastLoginTime(String userId, java.util.Date lastLoginTime) {
        if (userId == null || userId.trim().isEmpty() || lastLoginTime == null) {
            return false;
        }
        return userDao.updateLastLoginTime(userId, lastLoginTime);
    }

    @Autowired
    private EmailService emailService;

    @Override
    public Boolean resetPassword(String username, String email) {
        if (username == null || email == null) {
            throw new RuntimeException("用户名和邮箱不能为空");
        }

        User user = userDao.findByUsernameAndEmail(username, email);
        if (user == null) {
            throw new RuntimeException("用户名或邮箱不正确");
        }

        // 生成8位随机密码
        String newPassword = String.valueOf(System.currentTimeMillis()).substring(0, 8);
        user.setPassword(newPassword);

        // 发送邮件
        try {
            emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
        } catch (Exception e) {
            throw new RuntimeException("邮件发送失败：" + e.getMessage());
        }
        // TODO: 这里应该发送邮件通知用户新密码
        System.out.println("新密码已生成：" + newPassword);

        return userDao.updateUser(user);
    }


    private String generateRandomPassword() {
        // 生成8位随机密码
        return String.valueOf(System.currentTimeMillis()).substring(0, 8);
    }
}