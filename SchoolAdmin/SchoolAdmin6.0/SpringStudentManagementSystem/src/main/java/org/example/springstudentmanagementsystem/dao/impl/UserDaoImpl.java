package org.example.springstudentmanagementsystem.dao.impl;

import org.example.springstudentmanagementsystem.dao.UserDao;
import org.example.springstudentmanagementsystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setUsername(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setStatus(1);  // 设置默认状态为1
        user.setLastLoginTime(rs.getTimestamp("last_login_time"));
        return user;
    };

    @Override
    public Boolean addUser(User user) {
        String sql = "INSERT INTO users (id, name, email, password, role) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRole()) > 0;
    }
    //将 user 对象的各个属性值绑定到 SQL 语句的占位符中。如果插入操作影响的行数大于 0，则返回 true，表示操作成功；否则返回 false。

    @Override
    public Boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, userId) > 0;
    }

    @Override
    public Boolean updateUser(User user) {
        String sql = "UPDATE users SET name=?, email=?, password=?, role=? WHERE id=?";
        return jdbcTemplate.update(sql, user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRole(), user.getId()) > 0;
    }

    @Override
    public List<User> findAllUser() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User findUserById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE name = ? AND password = ? AND status = 1";
        try {
            return jdbcTemplate.query(sql, new Object[]{username, password}, userRowMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Boolean disableUser(String userId) {
        String sql = "UPDATE users SET status = 0 WHERE id = ?";
        return jdbcTemplate.update(sql, userId) > 0;
    }

    @Override
    public Boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE name = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count > 0;
    }

    @Override
    public Boolean updateLastLoginTime(String userId, Date lastLoginTime) {
        String sql = "UPDATE users SET last_login_time = ? WHERE id = ?";
        return jdbcTemplate.update(sql, lastLoginTime, userId) > 0;
    }

    @Override
    public User findByUsernameAndEmail(String username, String email) {
        String sql = "SELECT * FROM users WHERE name = ? AND email = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, username, email);
        return users.isEmpty() ? null : users.get(0);
    }
}