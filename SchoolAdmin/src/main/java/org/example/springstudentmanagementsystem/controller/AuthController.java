package org.example.springstudentmanagementsystem.controller;

import org.example.springstudentmanagementsystem.entity.User;
import org.example.springstudentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password,
                                     HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.login(username, password);
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户名或密码错误");
                return result;
            }
            session.setAttribute("user", user);
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("redirect", "/auth/profile");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    @ResponseBody
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            userService.register(user);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/change-password")
    @ResponseBody
    public Map<String, Object> changePassword(@RequestParam String oldPassword,
                                              @RequestParam String newPassword,
                                              HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("user");

        try {
            userService.changePassword(user.getId(), oldPassword, newPassword);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        return "auth/profile";
    }

    @PostMapping("/disable")
    @ResponseBody
    public Map<String, Object> disableAccount(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            result.put("success", false);
            result.put("message", "用户未登录");
            return result;
        }

        try {
            if (userService.disableUser(user.getId())) {
                session.invalidate();
                result.put("success", true);
                result.put("message", "账号已禁用成功");
            } else {
                result.put("success", false);
                result.put("message", "账号禁用失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "禁用失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/status")
    @ResponseBody
    public Map<String, Object> getLoginStatus(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            result.put("isLoggedIn", true);
            result.put("username", user.getUsername());
            result.put("lastLoginTime", user.getLastLoginTime());
        } else {
            result.put("isLoggedIn", false);
        }
        return result;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    @ResponseBody
    public Map<String, Object> forgotPassword(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        String username = request.get("username");
        String email = request.get("email");

        try {
            if (userService.resetPassword(username, email)) {
                result.put("success", true);
                result.put("message", "重置密码链接已发送到您的邮箱");
            } else {
                result.put("success", false);
                result.put("message", "用户名或邮箱不正确");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

}