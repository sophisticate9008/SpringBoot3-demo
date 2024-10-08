CREATE TABLE `user` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `account` VARCHAR(255) UNIQUE NOT NULL,
    `name` VARCHAR(255),
    `password` VARCHAR(255) NOT NULL,
    `salt` VARCHAR(255) NOT NULL,
    `sex` TINYINT,
    `age` TINYINT,
    `the_type` TINYINT NOT NULL,
    `state` TINYINT NOT NULL,
    `avatar_path` VARCHAR(255) DEFAULT NULL,
    `phone` VARCHAR(50) DEFAULT NULL,
    `email` VARCHAR(255) DEFAULT NULL,
    `signature` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE role (
    id INT NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE permission (
    id INT NOT NULL AUTO_INCREMENT,
    permission_name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE user_role (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE role_permission (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE commission (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description Text,
    begin_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time DATETIME NOT NULL,
    money DECIMAL(10, 2) NOT NULL,
    state TINYINT NOT NULL,
    num INT NOT NULL,
    current_num INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE reply(
    id INT NOT NULL AUTO_INCREMENT,
    content Text,
    user_id INT NOT NULL,
    commission_id INT NOT NULL,
    reply_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    state TINYINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (commission_id) REFERENCES commission(id) ON DELETE CASCADE
)CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE Message (
    id INT NOT NULL AUTO_INCREMENT,
    content VARCHAR(2000),
    sender_id INT,
    receiver_id INT,
    haveRead BOOLEAN DEFAULT FALSE,
    send_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    FOREIGN KEY(sender_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY(receiver_id) REFERENCES user(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE subscribe(
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    commission_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY(commission_id) REFERENCES commission(id) ON DELETE CASCADE
)CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE bell(
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    content VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE
)CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE bill(
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    content VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    gold DECIMAL(10,2) NOT NULL,
    type TINYINT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE
)CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE balance(
    user_id INT NOT NULL,
    gold DECIMAL(10,2) NOT NULL DEFAULT 0,
    state TINYINT NOT NULL DEFAULT 1,
    PRIMARY KEY(user_id),
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE
)CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

