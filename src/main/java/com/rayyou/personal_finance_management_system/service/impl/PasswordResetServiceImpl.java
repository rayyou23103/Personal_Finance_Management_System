package com.rayyou.personal_finance_management_system.service.impl;

import com.rayyou.personal_finance_management_system.service.PasswordResetService;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    @Override
    public void sendRestToken(String email) {

    }

    @Override
    public void resetPassword(String token, String newPassword) {

    }
}
