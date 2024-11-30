package org.example.springstudentmanagementsystem.dao.impl;

import org.example.springstudentmanagementsystem.dao.LeaveApprovalDao;
import org.example.springstudentmanagementsystem.entity.LeaveApproval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LeaveApprovalDaoImpl implements LeaveApprovalDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<LeaveApproval> approvalRowMapper = (rs, rowNum) -> {
        LeaveApproval approval = new LeaveApproval();
        approval.setId(rs.getString("id"));
        approval.setLeaveId(rs.getString("leave_id"));
        approval.setApproverId(rs.getString("approver_id"));
        approval.setApproverRole(rs.getString("approver_role"));
        approval.setStatus(rs.getString("status"));
        approval.setComments(rs.getString("comments"));
        approval.setApprovalTime(rs.getTimestamp("approval_time"));
        return approval;
    };

    @Override
    public Boolean addApproval(LeaveApproval approval) {
        String sql = "INSERT INTO leave_approvals (id, leave_id, approver_id, approver_role, status, comments, approval_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, approval.getId(), approval.getLeaveId(),
                approval.getApproverId(), approval.getApproverRole(), approval.getStatus(),
                approval.getComments(), approval.getApprovalTime()) > 0;
    }

    @Override
    public Boolean updateApproval(LeaveApproval approval) {
        String sql = "UPDATE leave_approvals SET status=?, comments=?, approval_time=? WHERE id=?";
        return jdbcTemplate.update(sql, approval.getStatus(), approval.getComments(),
                approval.getApprovalTime(), approval.getId()) > 0;
    }

    @Override
    public List<LeaveApproval> findApprovalsByLeaveId(String leaveId) {
        String sql = "SELECT * FROM leave_approvals WHERE leave_id = ? ORDER BY approval_time DESC";
        return jdbcTemplate.query(sql, approvalRowMapper, leaveId);
    }

    @Override
    public List<LeaveApproval> findApprovalsByApproverId(String approverId) {
        String sql = "SELECT * FROM leave_approvals WHERE approver_id = ? ORDER BY approval_time DESC";
        return jdbcTemplate.query(sql, approvalRowMapper, approverId);
    }

    @Override
    public List<LeaveApproval> findPendingApprovals(String approverId) {
        String sql = "SELECT * FROM leave_approvals WHERE approver_id = ? AND status = 'PENDING' ORDER BY approval_time ASC";
        return jdbcTemplate.query(sql, approvalRowMapper, approverId);
    }
}