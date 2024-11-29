package org.example.springstudentmanagementsystem.service.impl;

import org.example.springstudentmanagementsystem.dao.LeaveApprovalDao;
import org.example.springstudentmanagementsystem.entity.LeaveApproval;
import org.example.springstudentmanagementsystem.service.EmailService;
import org.example.springstudentmanagementsystem.service.LeaveApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LeaveApprovalServiceImpl implements LeaveApprovalService {
    @Autowired
    private LeaveApprovalDao leaveApprovalDao;

    @Autowired
    private EmailService emailService;

    @Override
    public Boolean approveLeave(LeaveApproval approval) {
        if (approval == null) {
            throw new RuntimeException("审批信息不能为空");
        }

        approval.setId(String.valueOf(System.currentTimeMillis()));
        approval.setStatus("APPROVED");
        approval.setApprovalTime(new Date());

        Boolean result = leaveApprovalDao.addApproval(approval);

        // 发送邮件通知学生
        if (result) {
            try {
                // TODO: 获取学生邮箱并发送通知
                // emailService.sendApprovalNotification(studentEmail, approval);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public Boolean rejectLeave(String leaveId, String approverId, String comments) {
        LeaveApproval approval = new LeaveApproval();
        approval.setId(String.valueOf(System.currentTimeMillis()));
        approval.setLeaveId(leaveId);
        approval.setApproverId(approverId);
        approval.setStatus("REJECTED");
        approval.setComments(comments);
        approval.setApprovalTime(new Date());

        return leaveApprovalDao.addApproval(approval);
    }

    @Override
    public List<LeaveApproval> findApprovalsByLeaveId(String leaveId) {
        return leaveApprovalDao.findApprovalsByLeaveId(leaveId);
    }

    @Override
    public List<LeaveApproval> findPendingApprovals(String approverId) {
        return leaveApprovalDao.findPendingApprovals(approverId);
    }

    @Override
    public Boolean isApprovalRequired(String approverRole, int leaveDays) {
        // 根据请假天数和审批人角色判断是否需要审批
        switch (approverRole) {
            case "教师":
                return leaveDays <= 3;
            case "班主任":
                return leaveDays <= 7;
            case "辅导员":
                return leaveDays <= 14;
            case "副院长":
                return leaveDays <= 30;
            case "院长":
                return true;
            default:
                return false;
        }
    }
}