package com.projects.sms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projects.sms.repository.UserRepository;

@Service
public class ResetPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void resetPassword(String username, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);

        int updated = userRepository.resetPasswordByUsername(username, encodedPassword);

        if (updated == 0) {
            throw new RuntimeException("User not found");
        }
    }
}
