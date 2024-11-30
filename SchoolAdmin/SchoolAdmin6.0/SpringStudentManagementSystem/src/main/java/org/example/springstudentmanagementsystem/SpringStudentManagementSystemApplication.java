package org.example.springstudentmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.example.springstudentmanagementsystem")
public class SpringStudentManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringStudentManagementSystemApplication.class, args);
    }
}