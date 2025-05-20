-- Tạo các database cần thiết cho các service
CREATE DATABASE IF NOT EXISTS student_db;
CREATE DATABASE IF NOT EXISTS question_db;
CREATE DATABASE IF NOT EXISTS quiz_db;
CREATE DATABASE IF NOT EXISTS quiz_config_db;

-- Cấp quyền cho user
GRANT ALL PRIVILEGES ON student_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON question_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON quiz_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON quiz_config_db.* TO 'root'@'%';

FLUSH PRIVILEGES;
