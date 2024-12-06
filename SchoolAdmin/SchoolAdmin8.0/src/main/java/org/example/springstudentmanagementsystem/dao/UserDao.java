package org.example.springstudentmanagementsystem.dao;

import org.example.springstudentmanagementsystem.entity.User;

import java.util.List;

public interface UserDao {
    Boolean addUser(User user);

    Boolean deleteUser(String userId);

    Boolean updateUser(User user);

    List<User> findAllUser();

    User findUserById(String id);

    List<User> findByUsernameAndPassword(String username, String password);

    Boolean disableUser(String userId);

    Boolean existsByUsername(String username);

    Boolean updateLastLoginTime(String userId, java.util.Date lastLoginTime);

    User findByUsernameAndEmail(String username, String email);
    
}
