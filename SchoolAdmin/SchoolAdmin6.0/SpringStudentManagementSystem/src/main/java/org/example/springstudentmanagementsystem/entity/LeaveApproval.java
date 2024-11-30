package org.example.springstudentmanagementsystem.entity;

import java.util.Date;

public class LeaveApproval {
    private String id;
    private String leaveId;      // 关联的请假记录ID
    private String approverId;   // 审批人ID
    private String approverRole; // 审批人角色：教师/班主任/辅导员/副院长/院长
    private String status;       // 审批状态：待审批/已批准/已拒绝
    private String comments;     // 审批意见
    private Date approvalTime;   // 审批时间

    // Getter方法
    public String getId() {
        return id;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public String getApproverId() {
        return approverId;
    }

    public String getApproverRole() {
        return approverRole;
    }

    public String getStatus() {
        return status;
    }

    public String getComments() {
        return comments;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    // Setter方法
    public void setId(String id) {
        this.id = id;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public void setApproverRole(String approverRole) {
        this.approverRole = approverRole;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }
}