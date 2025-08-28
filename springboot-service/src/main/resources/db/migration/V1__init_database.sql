CREATE TABLE Users
(
    Id          BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    Uid         CHAR(36)     NOT NULL UNIQUE,
    Email       VARCHAR(255) NOT NULL UNIQUE,
    FullName    VARCHAR(255) NOT NULL,
    Birthday    DATE,
    IsActive    BIT          NOT NULL DEFAULT 1,
    Roles       TEXT,
    CreatedTime BIGINT       NOT NULL,
    UpdatedTime BIGINT       NOT NULL
);

CREATE TABLE Accounts
(
    Id           BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    UserId       BIGINT NOT NULL,
    Provider     VARCHAR(100),
    PasswordHash VARCHAR(255),
    CreatedTime  BIGINT NOT NULL,

    CONSTRAINT FK_Accounts_Users FOREIGN KEY (UserId) REFERENCES Users (Id) ON DELETE CASCADE
);

CREATE TABLE RefreshTokens
(
    Id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    Token      VARCHAR(255) NOT NULL UNIQUE,
    UserId     BIGINT       NOT NULL,
    ExpiryDate TIMESTAMP(6) NOT NULL,
    IsInvoked  BIT          NOT NULL DEFAULT 0,

    CONSTRAINT FK_RefreshTokens_Users FOREIGN KEY (UserId) REFERENCES Users (Id) ON DELETE CASCADE
);
