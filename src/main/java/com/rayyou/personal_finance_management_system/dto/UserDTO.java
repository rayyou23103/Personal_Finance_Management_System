package com.rayyou.personal_finance_management_system.dto;

import com.rayyou.personal_finance_management_system.entity.User;

import java.time.LocalDateTime;

public class UserDTO {
    private Integer userId;
    private String email;
    private String username;
    private Boolean isEmailVerified;
    private LocalDateTime createdAt;

    public UserDTO(Integer userId, String email, String username,Boolean isEmailVerified,LocalDateTime createdAt){
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.isEmailVerified = isEmailVerified;
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static UserDTO form(User user){
        return new UserDTO(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getEmailVerified(),
                user.getCreatedAt()
        );
    }
}
