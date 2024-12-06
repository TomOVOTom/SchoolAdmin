package org.example.springstudentmanagementsystem.controller;

import org.example.springstudentmanagementsystem.entity.Leave;
import org.example.springstudentmanagementsystem.entity.User;
import org.example.springstudentmanagementsystem.service.LeaveService;
import org.example.springstudentmanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LeaveControllerTest {

    @Mock
    private LeaveService leaveService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LeaveController leaveController;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new MockHttpSession();
    }

    @Test
    void testSubmitLeaveSuccess() {
        // 准备测试数据
        Leave mockLeave = new Leave();
        mockLeave.setId("1");
        mockLeave.setStudentId("1");
        mockLeave.setReason("生病");
        mockLeave.setStartTime(new Date());
        mockLeave.setEndTime(new Date());
        mockLeave.setStatus("PENDING");

        User mockUser = new User();
        mockUser.setId("1");
        mockUser.setRole("学生");
        session.setAttribute("user", mockUser);

        // 模拟服务行为
        when(leaveService.submitLeave(any(Leave.class))).thenReturn(true);

        // 执行测试
        Map<String, Object> result = leaveController.submitLeave(mockLeave, session);

        // 验证结果
        assertTrue((Boolean) result.get("success"));
        assertEquals("请假申请提交成功", result.get("message"));
    }

    @Test
    void testSubmitLeaveWithoutLogin() {
        // 准备测试数据
        Leave mockLeave = new Leave();
        mockLeave.setReason("生病");
        mockLeave.setStartTime(new Date());
        mockLeave.setEndTime(new Date());

        // session中不设置user，模拟未登录状态

        // 执行测试
        Map<String, Object> result = leaveController.submitLeave(mockLeave, session);

        // 验证结果
        assertFalse((Boolean) result.get("success"));
        assertEquals("用户未登录", result.get("message"));
    }

    @Test
    void testGetLeaveDetails() {
        // 准备测试数据
        String leaveId = "1";
        Leave mockLeave = new Leave();
        mockLeave.setId(leaveId);
        mockLeave.setStudentId("1");

        User mockStudent = new User();
        mockStudent.setId("1");
        mockStudent.setUsername("testStudent");
        mockStudent.setName("张三");

        // 模拟服务行为
        when(leaveService.findLeaveById(leaveId)).thenReturn(mockLeave);
        when(userService.findUserById("1")).thenReturn(mockStudent);

        // 执行测试
        Leave result = leaveController.getLeaveDetails(leaveId);

        // 验证结果
        assertNotNull(result);
        assertEquals("张三", result.getStudentName());
    }

    @Test
    void testGetPendingLeaves() {
        // 准备测试数据
        User mockTeacher = new User();
        mockTeacher.setId("1");
        mockTeacher.setRole("教师");
        session.setAttribute("user", mockTeacher);

        List<Leave> mockLeaves = new ArrayList<>();
        Leave mockLeave = new Leave();
        mockLeave.setId("1");
        mockLeave.setStatus("PENDING");
        mockLeaves.add(mockLeave);

        // 模拟服务行为
        when(leaveService.findPendingLeaves()).thenReturn(mockLeaves);

        // 执行测试
        List<Leave> result = leaveController.getPendingLeaves(session);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
    }
}