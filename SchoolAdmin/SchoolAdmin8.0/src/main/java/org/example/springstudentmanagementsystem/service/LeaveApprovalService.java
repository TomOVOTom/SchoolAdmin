package org.example.springstudentmanagementsystem.service;

import org.example.springstudentmanagementsystem.entity.LeaveApproval;

import java.util.List;

public interface LeaveApprovalService {
    Boolean approveLeave(LeaveApproval approval);

    Boolean rejectLeave(String leaveId, String approverId, String comments);

    List<LeaveApproval> findApprovalsByLeaveId(String leaveId);

    List<LeaveApproval> findPendingApprovals(String approverId);

    Boolean isApprovalRequired(String approverRole, int leaveDays);
}