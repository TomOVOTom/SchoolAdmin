package org.example.springstudentmanagementsystem.controller;

import org.example.springstudentmanagementsystem.entity.User;
import org.example.springstudentmanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new MockHttpSession();
    }

    @Test
    void testLoginSuccess() {
        // 准备测试数据
        User mockUser = new User();
        mockUser.setId("1");
        mockUser.setUsername("testUser");
        mockUser.setPassword("123456");
        mockUser.setRole("教师");

        // 模拟 UserService 的行为
        when(userService.login(any(String.class), any(String.class))).thenReturn(mockUser);

        // 执行测试
        Map<String, Object> result = authController.login("testUser", "123456", session);

        // 验证结果
        assertTrue((Boolean) result.get("success"));
        assertEquals("登录成功", result.get("message"));
        assertEquals(mockUser, session.getAttribute("user"));
        assertEquals("/auth/profile", result.get("redirect"));
    }

    @Test
    void testLoginFailureWithInvalidCredentials() {
        // 模拟登录失败
        when(userService.login(any(String.class), any(String.class))).thenReturn(null);

        // 执行测试
        Map<String, Object> result = authController.login("wrongUser", "wrongPass", session);

        // 验证结果
        assertFalse((Boolean) result.get("success"));
        assertEquals("用户名或密码错误", result.get("message"));
        assertNull(session.getAttribute("user"));
    }

    @Test
    void testLoginFailureWithEmptyUsername() {
        // 模拟 UserService 抛出异常
        when(userService.login("", "123456"))
                .thenThrow(new RuntimeException("用户名和密码不能为空"));

        // 执行测试
        Map<String, Object> result = authController.login("", "123456", session);

        // 验证结果
        assertFalse((Boolean) result.get("success"));
        assertEquals("登录失败：用户名和密码不能为空", result.get("message"));
        assertNull(session.getAttribute("user"));
    }

    @Test
    void testLoginFailureWithEmptyPassword() {
        // 模拟 UserService 抛出异常
        when(userService.login("testUser", ""))
                .thenThrow(new RuntimeException("用户名和密码不能为空"));

        // 执行测试
        Map<String, Object> result = authController.login("testUser", "", session);

        // 验证结果
        assertFalse((Boolean) result.get("success"));
        assertEquals("登录失败：用户名和密码不能为空", result.get("message"));
        assertNull(session.getAttribute("user"));
    }
}