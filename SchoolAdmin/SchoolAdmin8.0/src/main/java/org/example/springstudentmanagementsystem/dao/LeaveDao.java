package org.example.springstudentmanagementsystem.dao;

import org.example.springstudentmanagementsystem.entity.Leave;

import java.util.List;

public interface LeaveDao {
    Boolean addLeave(Leave leave);

    Boolean updateLeave(Leave leave);

    Boolean deleteLeave(String leaveId);

    Leave findLeaveById(String id);

    List<Leave> findLeavesByStudentId(String studentId);

    List<Leave> findPendingLeaves();

    List<Leave> findLeavesByStatus(String status);

    List<Leave> findAllLeaves();

    Boolean updateLeaveStatus(String leaveId, String status);
}