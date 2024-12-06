package org.example.springstudentmanagementsystem.service.impl;

import org.example.springstudentmanagementsystem.dao.LeaveDao;
import org.example.springstudentmanagementsystem.entity.Leave;
import org.example.springstudentmanagementsystem.entity.User;
import org.example.springstudentmanagementsystem.service.EmailService;
import org.example.springstudentmanagementsystem.service.LeaveService;
import org.example.springstudentmanagementsystem.service.UserService;
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

    @Autowired
    private UserService userService;

    @Override
    public Boolean submitLeave(Leave leave) {
        if (leave == null) {
            throw new RuntimeException("请假信息不能为空");
        }

        // 设置基本信息
        if (leave.getId() == null || leave.getId().trim().isEmpty()) {
            leave.setId(String.valueOf(System.currentTimeMillis()));
        }

        // 如果没有设置状态，默认为PENDING
        if (leave.getStatus() == null) {
            leave.setStatus("PENDING");
        }

        leave.setCreateTime(new Date());
        leave.setUpdateTime(new Date());

        // 保存请假记录
        Boolean result = leaveDao.addLeave(leave);

        // 只有在状态为PENDING时才发送邮件通知教师
        if (result && "PENDING".equals(leave.getStatus())) {
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
        Leave leave = leaveDao.findLeaveById(id);
        if (leave != null) {
            System.out.println("Found leave with status: " + leave.getStatus());
        }
        return leave;
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
        // 从数据库中查找状态为 "PENDING" 的请假记录
        List<Leave> leaves = leaveDao.findLeavesByStatus("PENDING");

        // 遍历每个请假记录
        for (Leave leave : leaves) {
            // 根据请假记录中的学生ID查找对应的学生信息
            User student = userService.findUserById(leave.getStudentId());

            if (student != null) {
                // 优先使用学生的 name 字段，如果为空则使用 username 字段
                String studentName = student.getName();
                if (studentName == null || studentName.trim().isEmpty()) {
                    studentName = student.getUsername();
                }
                // 将学生姓名设置到请假记录中
                leave.setStudentName(studentName);
            } else {
                // 如果没有找到对应的学生信息，将学生姓名设置为 "未知用户"
                leave.setStudentName("未知用户");
            }
        }

        // 返回包含学生姓名的请假记录列表
        return leaves;
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