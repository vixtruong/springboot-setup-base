CREATE TABLE refresh_tokens
(
    id          CHAR(36)     NOT NULL PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     CHAR(36)     NOT NULL,
    expiry_date DATETIME     NOT NULL,
    is_invoked  BOOLEAN      NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_user_refresh_token FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
