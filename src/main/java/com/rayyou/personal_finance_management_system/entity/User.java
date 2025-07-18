package com.rayyou.personal_finance_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "pfms_schemas")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String username;

    @Column()
    private String password;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    private Boolean isEmailVerified = false;

    private String emailVerificationToken;

    @Column(name = "token_expired_at")
    private LocalDateTime tokenExpiredAt;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    private void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    private void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    private void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public LocalDateTime getTokenExpiredAt() {
        return tokenExpiredAt;
    }

    private void setTokenExpiredAt(LocalDateTime tokenExpriredAt) {
        this.tokenExpiredAt = tokenExpriredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    private void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private void setUpdatedAt(LocalDateTime updateAt) {
        this.updatedAt = updateAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    private void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(this.userId, user.userId)
                && Objects.equals(this.username, user.username)
                && Objects.equals(this.email, user.email);
    }

    //    不包含用戶私密資料，如password
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updateAt=" + updatedAt +
                '}';
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public void applyVerificationToken(String token, LocalDateTime tokenExpiredAt) {
        this.emailVerificationToken = token;
        this.tokenExpiredAt = tokenExpiredAt;
    }

    // 驗證信箱成功，清除 token, tokenExpiredAt
    public void verifyEmail() {
        this.isEmailVerified = true;
        this.emailVerificationToken = null;
        this.tokenExpiredAt = null;
    }
}
