package org.example.springstudentmanagementsystem.service;

import org.example.springstudentmanagementsystem.entity.Leave;

import java.util.List;

public interface LeaveService {
    Boolean submitLeave(Leave leave);

    Boolean updateLeave(Leave leave);

    Boolean cancelLeave(String leaveId);

    Leave findLeaveById(String id);

    List<Leave> findLeavesByStudentId(String studentId);

    List<Leave> findPendingLeaves();

    List<Leave> findLeavesByStatus(String status);

    Boolean updateLeaveStatus(String leaveId, String status);

    List<Leave> findLeaveHistory(String studentId);
    
}