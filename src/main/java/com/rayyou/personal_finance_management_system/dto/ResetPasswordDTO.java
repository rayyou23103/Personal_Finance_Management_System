package com.rayyou.personal_finance_management_system.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordDTO {
    @NotBlank
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


}
