package org.example.springstudentmanagementsystem;

import org.example.springstudentmanagementsystem.entity.User;
import org.example.springstudentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Main implements CommandLineRunner {

    @Autowired
    private UserService userService;

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        while (true) {
            System.out.println("\n=== 用户管理系统 ===");
            System.out.println("1. 添加用户");
            System.out.println("2. 删除用户");
            System.out.println("3. 更新用户");
            System.out.println("4. 查询所有用户");
            System.out.println("5. 按ID查询用户");
            System.out.println("6. 退出");
            System.out.print("请选择操作: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消费换行符

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    deleteUser();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    findAllUsers();
                    break;
                case 5:
                    findUserById();
                    break;
                case 6:
                    System.out.println("感谢使用，再见！");
                    return;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }

    private void addUser() {
        System.out.println("\n=== 添加用户 ===");
        System.out.print("输入用户名: ");
        String name = scanner.nextLine();
        System.out.print("输入邮箱: ");
        String email = scanner.nextLine();
        System.out.print("输入密码: ");
        String password = scanner.nextLine();
        System.out.print("输入角色: ");
        String role = scanner.nextLine();

        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        if (userService.addUser(user)) {
            System.out.println("用户添加成功！");
        } else {
            System.out.println("用户添加失败！");
        }
    }

    private void deleteUser() {
        System.out.println("\n=== 删除用户 ===");
        System.out.print("输入要删除的用户ID: ");
        String id = scanner.nextLine();

        if (userService.deleteUser(id)) {
            System.out.println("用户删除成功！");
        } else {
            System.out.println("用户删除失败！");
        }
    }

    private void updateUser() {
        System.out.println("\n=== 更新用户 ===");
        System.out.print("输入要更新的用户ID: ");
        String id = scanner.nextLine();

        User existingUser = userService.findUserById(id);
        if (existingUser == null) {
            System.out.println("用户不存在！");
            return;
        }

        System.out.print("输入新的用户名 (直接回车保持不变): ");
        String name = scanner.nextLine();
        System.out.print("输入新的邮箱 (直接回车保持不变): ");
        String email = scanner.nextLine();
        System.out.print("输入新的密码 (直接回车保持不变): ");
        String password = scanner.nextLine();
        System.out.print("输入新的角色 (直接回车保持不变): ");
        String role = scanner.nextLine();

        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setUsername(name.isEmpty() ? existingUser.getUsername() : name);
        updatedUser.setEmail(email.isEmpty() ? existingUser.getEmail() : email);
        updatedUser.setPassword(password.isEmpty() ? existingUser.getPassword() : password);
        updatedUser.setRole(role.isEmpty() ? existingUser.getRole() : role);

        if (userService.updateUser(updatedUser)) {
            System.out.println("用户更新成功！");
        } else {
            System.out.println("用户更新失败！");
        }
    }

    private void findAllUsers() {
        System.out.println("\n=== 所有用户列表 ===");
        List<User> users = userService.findAllUser();
        if (users.isEmpty()) {
            System.out.println("没有找到任何用户！");
            return;
        }

        System.out.println("ID\t\t名称\t\t邮箱\t\t角色");
        System.out.println("----------------------------------------");
        for (User user : users) {
            System.out.printf("%s\t\t%s\t\t%s\t\t%s%n",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole());
        }
    }

    private void findUserById() {
        System.out.println("\n=== 查询用户 ===");
        System.out.print("输入要查询的用户ID: ");
        String id = scanner.nextLine();
        User user = userService.findUserById(id);
        if (user == null) {
            System.out.println("未找到该用户！");
            return;
        }
        System.out.println("\n用户信息：");
        System.out.println("ID: " + user.getId());
        System.out.println("名称: " + user.getUsername());
        System.out.println("邮箱: " + user.getEmail());
        System.out.println("角色: " + user.getRole());
    }
}