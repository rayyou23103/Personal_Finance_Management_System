package com.rayyou.personal_finance_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResetPasswordConfirmDTO {
    @NotBlank
    private String token;
    @NotBlank
    private String newPassword;

    public ResetPasswordConfirmDTO() {
    }

    public ResetPasswordConfirmDTO(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

