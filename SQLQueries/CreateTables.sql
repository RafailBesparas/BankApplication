-- 1. account_model table
CREATE TABLE account_model (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(19,2) NOT NULL
);

-- 2. client_profile table
CREATE TABLE client_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    date_of_birth DATE,
    national_id VARCHAR(255),
    address VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    employment_status VARCHAR(255),
    annual_income DECIMAL(15,2),
    profile_photo_path VARCHAR(512),
    account_id BIGINT UNIQUE,
    CONSTRAINT fk_client_account FOREIGN KEY (account_id) REFERENCES account_model(id)
);

-- 3. transaction table
CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(19,2) NOT NULL,
    type VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    account_id BIGINT,
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES account_model(id)
);

CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT,
    message VARCHAR(500),
    type VARCHAR(50),
    timestamp DATETIME,
    read BOOLEAN,
    priority VARCHAR(20),
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account_model(id)
);