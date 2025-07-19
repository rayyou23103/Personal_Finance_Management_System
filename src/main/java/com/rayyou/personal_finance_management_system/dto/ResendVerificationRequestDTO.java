package com.rayyou.personal_finance_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ResendVerificationRequestDTO {
    @NotBlank
    @Email
    String email;

    public ResendVerificationRequestDTO(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
