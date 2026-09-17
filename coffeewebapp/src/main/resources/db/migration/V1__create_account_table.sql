CREATE TABLE account (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    firstname   VARCHAR(255) NOT NULL,
    lastname    VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(255) NOT NULL,
    username    VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(255),
    totp_secret VARCHAR(255),
    totp_enabled BIT(1)      DEFAULT 0,
    verified     BIT(1)      DEFAULT 0,
    PRIMARY KEY (id)
);
