package com.rayyou.personal_finance_management_system.service;

import com.rayyou.personal_finance_management_system.dto.ResendVerificationRequestDTO;
import com.rayyou.personal_finance_management_system.dto.ResetPasswordConfirmDTO;
import com.rayyou.personal_finance_management_system.dto.ResetPasswordRequestDTO;

public interface UserService {
    Integer register(String username,   String email, String password);

    boolean login(String email, String password);

    Boolean verifyEmail(String token);

    void resendVerification(ResendVerificationRequestDTO dto);

    void resetRequest(ResetPasswordRequestDTO dto);

    void resetConfirm(ResetPasswordConfirmDTO dto);


}
