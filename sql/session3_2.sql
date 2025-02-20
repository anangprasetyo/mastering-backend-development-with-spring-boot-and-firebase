-- Use Database
USE todo_app;

-- Update Taboe Students
ALTER TABLE tb_students ADD COLUMN password VARCHAR(25) NOT NULL AFTER email;
