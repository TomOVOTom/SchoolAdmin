package org.example.springstudentmanagementsystem.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String newPassword);
}