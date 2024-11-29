package org.example.springstudentmanagementsystem.controller;

import org.example.springstudentmanagementsystem.entity.Leave;
import org.example.springstudentmanagementsystem.entity.LeaveApproval;
import org.example.springstudentmanagementsystem.entity.User;
import org.example.springstudentmanagementsystem.service.LeaveApprovalService;
import org.example.springstudentmanagementsystem.service.LeaveService;
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
            // 从session获取学生ID
            String studentId = ((User) session.getAttribute("user")).getId();
            leave.setStudentId(studentId);

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

    // 审批请假申请
    @PostMapping("/approve/{leaveId}")
    @ResponseBody
    public Map<String, Object> approveLeave(
            @PathVariable String leaveId,
            @RequestBody LeaveApproval approval,
            HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            String approverId = ((User) session.getAttribute("user")).getId();
            approval.setLeaveId(leaveId);
            approval.setApproverId(approverId);

            if (leaveApprovalService.approveLeave(approval)) {
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
}