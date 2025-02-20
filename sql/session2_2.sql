-- Use Database
USE todo_app;

-- Create Tables Students
CREATE TABLE IF NOT EXISTS tb_students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    class VARCHAR(10) NOT NULL
) ENGINE = InnoDB;

-- Create Tables Lessons
CREATE TABLE IF NOT EXISTS tb_lessonss (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES tb_students(id) ON DELETE CASCADE
) ENGINE = InnoDB;
