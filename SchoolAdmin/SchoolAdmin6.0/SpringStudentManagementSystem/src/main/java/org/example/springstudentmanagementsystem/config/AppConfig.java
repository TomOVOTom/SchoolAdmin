package org.example.springstudentmanagementsystem.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackages = {
        "org.example.springstudentmanagementsystem.dao",
        "org.example.springstudentmanagementsystem.service",
        "org.example.springstudentmanagementsystem"
})
@ImportResource("classpath:applicationContext.xml")
public class AppConfig {
}