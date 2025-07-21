INSERT INTO users (role_id, username, password, email, is_email_verified, created_at, updated_at, is_active)
VALUES
    (1, 'alice',   'hashed_pwd_1', 'alice@example.com',   true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (1, 'bob',     'hashed_pwd_2', 'bob@example.com',     true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (1, 'charlie', 'hashed_pwd_3', 'charlie@example.com', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (1, 'david',   'hashed_pwd_4', 'david@example.com',   false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (1, 'eva',     'hashed_pwd_5', 'eva@example.com',     false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);