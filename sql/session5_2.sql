-- Use Database
USE todo_app;

-- Create Tables User Tokens
CREATE TABLE tb_student_tokens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    token TEXT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES tb_students(id) ON DELETE CASCADE
) ENGINE = InnoDB;