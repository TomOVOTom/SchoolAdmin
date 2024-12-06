package org.example.springstudentmanagementsystem.service;

import org.example.springstudentmanagementsystem.entity.User;

import java.util.Date;
import java.util.List;

public interface UserService {
    Boolean addUser(User user);

    Boolean deleteUser(String userId);

    Boolean updateUser(User user);

    List<User> findAllUser();

    User findUserById(String id);

    // 添加新的方法
    User login(String username, String password);

    Boolean register(User user);

    Boolean changePassword(String userId, String oldPassword, String newPassword);

    Boolean disableUser(String userId);

    Boolean updateLastLoginTime(String userId, Date lastLoginTime);

    Boolean resetPassword(String username, String email);

    
}