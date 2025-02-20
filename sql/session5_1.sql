-- Use Database
USE todo_app;

-- Create Tables User Tokens
CREATE TABLE IF NOT EXISTS tb_user_tokens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE
) ENGINE = InnoDB;