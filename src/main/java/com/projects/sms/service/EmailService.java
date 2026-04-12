package com.projects.sms.service;

public interface EmailService {
    void sendVerificationEmail(String to, String token);
}
