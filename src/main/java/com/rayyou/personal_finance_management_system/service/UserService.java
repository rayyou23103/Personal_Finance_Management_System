package com.rayyou.personal_finance_management_system.service;

import com.rayyou.personal_finance_management_system.dto.*;

public interface UserService {
    Integer register(UserRegisterDTO dto);

    boolean login(UserLoginDTO dto);

    Boolean verifyEmail(String token);

    void resendVerification(ResendVerificationRequestDTO dto);

    void resetRequest(ResetPasswordRequestDTO dto);

    boolean resetConfirm(String token);

    void resetPassword(ResetPasswordDTO dto);

}
