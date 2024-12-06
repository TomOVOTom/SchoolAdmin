package org.example.springstudentmanagementsystem.dao.impl;

import org.example.springstudentmanagementsystem.dao.LeaveDao;
import org.example.springstudentmanagementsystem.entity.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LeaveDaoImpl implements LeaveDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Leave> leaveRowMapper = (rs, rowNum) -> {
        Leave leave = new Leave();
        leave.setId(rs.getString("id"));
        leave.setStudentId(rs.getString("student_id"));
        leave.setLeaveType(rs.getString("leave_type"));
        leave.setStartTime(rs.getTimestamp("start_time"));
        leave.setEndTime(rs.getTimestamp("end_time"));
        leave.setReason(rs.getString("reason"));
        leave.setRemarks(rs.getString("remarks"));
        leave.setLocation(rs.getString("location"));
        leave.setStatus(rs.getString("status"));
        leave.setAttachmentUrl(rs.getString("attachment_url"));
        leave.setCreateTime(rs.getTimestamp("create_time"));
        leave.setUpdateTime(rs.getTimestamp("update_time"));
        return leave;
    };

    @Override
    public Boolean addLeave(Leave leave) {
        String sql = "INSERT INTO leaves (id, student_id, leave_type, start_time, end_time, reason, remarks, location, status, attachment_url, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, leave.getId(), leave.getStudentId(), leave.getLeaveType(),
                leave.getStartTime(), leave.getEndTime(), leave.getReason(), leave.getRemarks(),
                leave.getLocation(), leave.getStatus(), leave.getAttachmentUrl(),
                leave.getCreateTime(), leave.getUpdateTime()) > 0;
    }

    @Override
    public Boolean updateLeave(Leave leave) {
        String sql = "UPDATE leaves SET leave_type=?, start_time=?, end_time=?, reason=?, remarks=?, location=?, status=?, attachment_url=?, update_time=? WHERE id=?";
        return jdbcTemplate.update(sql, leave.getLeaveType(), leave.getStartTime(), leave.getEndTime(),
                leave.getReason(), leave.getRemarks(), leave.getLocation(), leave.getStatus(),
                leave.getAttachmentUrl(), leave.getUpdateTime(), leave.getId()) > 0;
    }

    @Override
    public Leave findLeaveById(String id) {
        String sql = "SELECT * FROM leaves WHERE id = ?";
        List<Leave> leaves = jdbcTemplate.query(sql, leaveRowMapper, id);
        return leaves.isEmpty() ? null : leaves.get(0);
    }

    @Override
    public List<Leave> findLeavesByStudentId(String studentId) {
        String sql = "SELECT * FROM leaves WHERE student_id = ? ORDER BY create_time DESC";
        //按创建时间降序排列查询结果
        return jdbcTemplate.query(sql, leaveRowMapper, studentId);
    }

    @Override
    public List<Leave> findPendingLeaves() {
        String sql = "SELECT * FROM leaves WHERE status = 'PENDING' ORDER BY create_time ASC";
        return jdbcTemplate.query(sql, leaveRowMapper);
    }

    @Override
    public List<Leave> findLeavesByStatus(String status) {
        String sql = "SELECT * FROM leaves WHERE status = ? ORDER BY create_time DESC";
        return jdbcTemplate.query(sql, leaveRowMapper, status);
    }

    @Override
    public List<Leave> findAllLeaves() {
        String sql = "SELECT * FROM leaves ORDER BY create_time DESC";
        return jdbcTemplate.query(sql, leaveRowMapper);
    }

    @Override
    public Boolean updateLeaveStatus(String leaveId, String status) {
        String sql = "UPDATE leaves SET status = ? WHERE id = ?";
        return jdbcTemplate.update(sql, status, leaveId) > 0;
    }

    @Override
    public Boolean deleteLeave(String leaveId) {
        String sql = "DELETE FROM leaves WHERE id = ?";
        return jdbcTemplate.update(sql, leaveId) > 0;
    }

}