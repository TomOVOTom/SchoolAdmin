package org.example.springstudentmanagementsystem.dao;

import org.example.springstudentmanagementsystem.entity.LeaveApproval;

import java.util.List;

public interface LeaveApprovalDao {
    Boolean addApproval(LeaveApproval approval);

    Boolean updateApproval(LeaveApproval approval);

    List<LeaveApproval> findApprovalsByLeaveId(String leaveId);

    List<LeaveApproval> findApprovalsByApproverId(String approverId);

    List<LeaveApproval> findPendingApprovals(String approverId);
}