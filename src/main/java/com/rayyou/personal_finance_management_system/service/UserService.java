package com.rayyou.personal_finance_management_system.service;

import org.springframework.stereotype.Service;

public interface UserService {
    Integer register(String username,   String email, String password);

    boolean login(String email, String password);

    Boolean verifyEmail(String token);

    void resendVerification(String email);

    void resetRequest(String email);

    void resetConfirm(String token, String newPassword);


}
