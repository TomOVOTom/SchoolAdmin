package org.example.springstudentmanagementsystem.controller;

import org.example.springstudentmanagementsystem.entity.Leave;
import org.example.springstudentmanagementsystem.entity.LeaveApproval;
import org.example.springstudentmanagementsystem.entity.User;
import org.example.springstudentmanagementsystem.service.LeaveApprovalService;
import org.example.springstudentmanagementsystem.service.LeaveService;
import org.example.springstudentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private LeaveApprovalService leaveApprovalService;
    @Autowired
    private UserService userService;

    // 请假申请页面
    @GetMapping("/apply")
    public String applyPage() {
        return "leave/apply";
    }

    // 提交请假申请
    @PostMapping("/submit")
    @ResponseBody
    public Map<String, Object> submitLeave(@RequestBody Leave leave, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查用户是否登录
            User user = (User) session.getAttribute("user");
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户未登录");
                return result;
            }

            // 从session获取学生ID
            leave.setStudentId(user.getId());

            if (leaveService.submitLeave(leave)) {
                result.put("success", true);
                result.put("message", "请假申请提交成功");
            } else {
                result.put("success", false);
                result.put("message", "请假申请提交失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "提交失败：" + e.getMessage());
        }
        return result;
    }

    // 查看请假历史
    @GetMapping("/history")
    public String historyPage() {
        return "leave/history";
    }

    // 获取请假历史数据
    @GetMapping("/history/data")
    @ResponseBody
    public List<Leave> getLeaveHistory(HttpSession session) {
        String studentId = ((User) session.getAttribute("user")).getId();
        return leaveService.findLeaveHistory(studentId);
    }

    // 教师审批页面
    @GetMapping("/approve")
    public String approvePage() {
        return "leave/approve";
    }

    // 获取待审批的请假申请
    @GetMapping("/pending")
    @ResponseBody
    public List<Leave> getPendingLeaves(HttpSession session) {
        return leaveService.findPendingLeaves();
    }

    @PostMapping("/approve/{leaveId}")
    @ResponseBody
    public Map<String, Object> approveLeave(
            @PathVariable String leaveId,
            @RequestBody LeaveApproval approval,
            HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            User approver = (User) session.getAttribute("user");
            Leave leave = leaveService.findLeaveById(leaveId);

            // 计算请假天数
            long diffInMillies = leave.getEndTime().getTime() - leave.getStartTime().getTime();
            int leaveDays = (int) (diffInMillies / (1000 * 60 * 60 * 24)) + 1;

            // 检查是否有权限审批
            if (!leaveApprovalService.isApprovalRequired(approver.getRole(), leaveDays)) {
                String message = String.format("您当前角色为%s，只能审批%d天及以下的请假申请，该申请共%d天，请转交给相应级别的审批人",
                        approver.getRole(),
                        getMaxDays(approver.getRole()),
                        leaveDays);
                result.put("success", false);
                result.put("message", message);
                return result;
            }

            approval.setLeaveId(leaveId);
            approval.setApproverId(approver.getId());
            approval.setApproverRole(approver.getRole());

            if (leaveApprovalService.approveLeave(approval)) {
                // 更新请假记录状态
                leaveService.updateLeaveStatus(leaveId, approval.getStatus());
                result.put("success", true);
                result.put("message", "审批成功");
            } else {
                result.put("success", false);
                result.put("message", "审批失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "审批失败：" + e.getMessage());
        }
        return result;
    }

    // 获取各角色可审批的最大天数
    private int getMaxDays(String role) {
        switch (role) {
            case "教师":
                return 3;
            case "班主任":
                return 7;
            case "辅导员":
                return 14;
            case "副院长":
                return 30;
            case "院长":
                return Integer.MAX_VALUE;
            default:
                return 0;
        }
    }

    @PostMapping("/draft")
    @ResponseBody
    public Map<String, Object> saveDraft(@RequestBody Leave leave, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            String studentId = ((User) session.getAttribute("user")).getId();
            leave.setStudentId(studentId);
            leave.setStatus("DRAFT");

            if (leaveService.submitLeave(leave)) {
                result.put("success", true);
                result.put("message", "草稿保存成功");
            } else {
                result.put("success", false);
                result.put("message", "草稿保存失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "保存失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/draft/{id}")
    @ResponseBody
    public Map<String, Object> getDraft(@PathVariable String id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            String studentId = ((User) session.getAttribute("user")).getId();
            Leave leave = leaveService.findLeaveById(id);

            if (leave == null) {
                throw new RuntimeException("草稿不存在");
            }

            if (!studentId.equals(leave.getStudentId())) {
                throw new RuntimeException("无权限查看此草稿");
            }

            // 添加调试日志
            System.out.println("Leave status: " + leave.getStatus());

            // 修改状态检查逻辑，确保大小写一致
            if (!"DRAFT".equalsIgnoreCase(leave.getStatus())) {
                throw new RuntimeException("该请假申请不是草稿状态，当前状态：" + leave.getStatus());
            }

            result.put("success", true);
            result.put("data", leave);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Leave getLeaveDetails(@PathVariable String id) {
        Leave leave = leaveService.findLeaveById(id);
        if (leave != null) {
            User student = userService.findUserById(leave.getStudentId());
            if (student != null) {
                // 优先使用name字段，如果为空则使用username
                String studentName = student.getName();
                if (studentName == null || studentName.trim().isEmpty()) {
                    studentName = student.getUsername();
                }
                leave.setStudentName(studentName);
            } else {
                leave.setStudentName("未知用户");
            }
        }
        return leave;
    }

    @GetMapping("/approvals/{leaveId}")
    @ResponseBody
    public List<LeaveApproval> getLeaveApprovals(@PathVariable String leaveId) {
        return leaveApprovalService.findApprovalsByLeaveId(leaveId);
    }

}