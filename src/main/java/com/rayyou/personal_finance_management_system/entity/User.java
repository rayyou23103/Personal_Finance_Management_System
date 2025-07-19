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

    private String password;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    private Boolean isEmailVerified = false;

    // 信箱認證相關欄位
    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "email_token_expired_at")
    private LocalDateTime emailTokenExpiredAt;

    //密碼重設相關欄位
    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_token_expired_at")
    private LocalDateTime passwordResetTokenExpiredAt;

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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public LocalDateTime getEmailTokenExpiredAt() {
        return emailTokenExpiredAt;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public LocalDateTime getPasswordResetTokenExpiredAt() {
        return passwordResetTokenExpiredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
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


    // 信箱驗證相關方法
    public void applyVerificationToken(String token, LocalDateTime tokenExpiredAt) {
        this.emailVerificationToken = token;
        this.emailTokenExpiredAt = tokenExpiredAt;
    }

    // 驗證信箱成功，清除 token, tokenExpiredAt
    public void clearEmailVerificationToken() {
        this.isEmailVerified = true;
        this.emailVerificationToken = null;
        this.emailTokenExpiredAt = null;
    }

    // 密碼重設相關方法
    public void applyPasswordResetToken(String passwordResetToken, LocalDateTime passwordResetTokenExpiredAt) {
        this.passwordResetToken = passwordResetToken;
        this.passwordResetTokenExpiredAt = passwordResetTokenExpiredAt;
    }

    // 重設密碼成功，清除 token, tokenExpiredAt
    public void clearPasswordResetToken() {
        this.passwordResetToken = null;
        this.passwordResetTokenExpiredAt = null;
    }

    public boolean isPasswordResetTokenValid() {
        return passwordResetToken != null &&
                passwordResetTokenExpiredAt != null &&
                passwordResetTokenExpiredAt.isAfter(LocalDateTime.now());
    }
}
