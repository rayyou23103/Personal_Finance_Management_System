package com.rayyou.personal_finance_management_system.service;

import org.springframework.stereotype.Service;

@Service
public interface PasswordResetService {
    void sendRestToken (String email);
    void resetPassword(String token, String newPassword);

}
