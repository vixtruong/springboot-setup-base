CREATE TABLE users
(
    id        CHAR(36) NOT NULL PRIMARY KEY,
    username  VARCHAR(255),
    password  VARCHAR(255),
    full_name VARCHAR(255),
    birthday  DATE,
    roles     TEXT
);
