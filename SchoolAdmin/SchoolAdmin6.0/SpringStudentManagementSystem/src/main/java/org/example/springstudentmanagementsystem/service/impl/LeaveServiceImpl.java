package org.example.springstudentmanagementsystem.service.impl;

import org.example.springstudentmanagementsystem.dao.LeaveDao;
import org.example.springstudentmanagementsystem.entity.Leave;
import org.example.springstudentmanagementsystem.service.EmailService;
import org.example.springstudentmanagementsystem.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {
    @Autowired
    private LeaveDao leaveDao;

    @Autowired
    private EmailService emailService;

    @Override
    public Boolean submitLeave(Leave leave) {
        if (leave == null) {
            throw new RuntimeException("请假信息不能为空");
        }

        // 设置基本信息
        leave.setId(String.valueOf(System.currentTimeMillis()));
        leave.setStatus("PENDING");
        leave.setCreateTime(new Date());
        leave.setUpdateTime(new Date());

        // 保存请假记录
        Boolean result = leaveDao.addLeave(leave);

        // 发送邮件通知教师
        if (result) {
            try {
                // TODO: 获取教师邮箱并发送通知
                // emailService.sendLeaveNotification(teacherEmail, leave);
            } catch (Exception e) {
                // 记录日志但不影响请假流程
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public Boolean updateLeave(Leave leave) {
        if (leave == null) {
            throw new RuntimeException("请假信息不能为空");
        }
        leave.setUpdateTime(new Date());
        return leaveDao.updateLeave(leave);
    }

    @Override
    public Boolean cancelLeave(String leaveId) {
        if (leaveId == null || leaveId.trim().isEmpty()) {
            throw new RuntimeException("请假ID不能为空");
        }
        return leaveDao.deleteLeave(leaveId);
    }

    @Override
    public Leave findLeaveById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new RuntimeException("请假ID不能为空");
        }
        return leaveDao.findLeaveById(id);
    }

    @Override
    public List<Leave> findLeavesByStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new RuntimeException("学生ID不能为空");
        }
        return leaveDao.findLeavesByStudentId(studentId);
    }

    @Override
    public List<Leave> findPendingLeaves() {
        return leaveDao.findLeavesByStatus("PENDING");
    }

    @Override
    public List<Leave> findLeavesByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new RuntimeException("状态不能为空");
        }
        return leaveDao.findLeavesByStatus(status);
    }

    @Override
    public Boolean updateLeaveStatus(String leaveId, String status) {
        if (leaveId == null || status == null) {
            throw new RuntimeException("请假ID和状态不能为空");
        }
        return leaveDao.updateLeaveStatus(leaveId, status);
    }

    @Override
    public List<Leave> findLeaveHistory(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new RuntimeException("学生ID不能为空");
        }
        return leaveDao.findLeavesByStudentId(studentId);
    }
}