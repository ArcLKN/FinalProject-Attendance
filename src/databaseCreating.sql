drop database db_time_attendance;
CREATE DATABASE db_time_attendance;
USE db_time_attendance;

CREATE TABLE t_emp (
	id INT AUTO_INCREMENT PRIMARY KEY,
    nameEmp VARCHAR(20),
    codeEmp CHAR(36)
);

CREATE TABLE t_lock_in_record (
	id INT,
    check_in_time DATETIME,
    FOREIGN KEY(id) REFERENCES t_emp(id)
);

CREATE TABLE t_admin (
	id INT,
    username VARCHAR(20),
    passwordAdmin VARCHAR(20),
    FOREIGN KEY(id) REFERENCES t_emp(id)
);

CREATE TABLE t_work_time (
	startWork TIME
);