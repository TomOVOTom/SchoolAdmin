package org.example.springstudentmanagementsystem.entity;


import java.util.Date;

public class Leave {
    private String id;
    private String studentId;
    private String leaveType;    // 请假类型：事假/病假
    private Date startTime;      // 开始时间
    private Date endTime;        // 结束时间
    private String reason;       // 请假原因
    private String remarks;      // 备注说明
    private String location;     // 请假地点
    private String status;       // 审批状态：待审批/已批准/已拒绝
    private String attachmentUrl;// 附件URL
    private Date createTime;     // 创建时间
    private Date updateTime;     // 更新时间

    // Getter方法
    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getReason() {
        return reason;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    // Setter方法
    public void setId(String id) {
        this.id = id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}