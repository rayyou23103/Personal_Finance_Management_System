CREATE TABLE users (
                       user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       role_id TINYINT NOT NULL DEFAULT 1,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       is_email_verified BOOLEAN DEFAULT TRUE,
                       email_verification_token VARCHAR(255),
                       email_token_expired_at TIMESTAMP,
                       password_reset_token VARCHAR(255),
                       password_reset_token_expired_at TIMESTAMP,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       is_active BOOLEAN DEFAULT TRUE
);