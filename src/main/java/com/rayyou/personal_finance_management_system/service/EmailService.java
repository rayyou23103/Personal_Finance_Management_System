package com.rayyou.personal_finance_management_system.service;

public interface EmailService {
    void sendVerificationEmail(String email);

    void sendResetToken(String email);
}
