package com.rayyou.personal_finance_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequestDTO {
    @NotBlank
    @Email
    String email;

    public ResetPasswordRequestDTO() {

    }

    public ResetPasswordRequestDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
