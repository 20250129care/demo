CREATE TABLE IF NOT EXISTS department (
    id CHAR(2) NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    is_deleted BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user (
    id CHAR(20) NOT NULL PRIMARY KEY,
    family_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    dept_id CHAR(2) NOT NULL,
    version INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY(dept_id) REFERENCES department(id)
);

CREATE TABLE IF NOT EXISTS user_summary (
    name VARCHAR(50) NOT NULL,
    dept_id CHAR(2) NOT NULL,
    dept_name VARCHAR(50) NOT NULL,
    last_updated_at DATE NOT NULL,
    user_id CHAR(20) NOT NULL PRIMARY KEY,
    user_version INT NOT NULL, 
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_name ON user_summary (name);
CREATE INDEX idx_last_updated_at ON user_summary (last_updated_at);
CREATE INDEX idx_dept_id ON user_summary (dept_id);
