package com.rayyou.personal_finance_management_system.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordConfirmDTO {
    @NotBlank
    String newPassword;

    String token;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
