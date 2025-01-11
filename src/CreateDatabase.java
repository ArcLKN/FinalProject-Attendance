import java.sql.*;

public class CreateDatabase {
    public static void main(String[] args) {
        final String URL = "jdbc:mysql://localhost:3306/";
        final String USERNAME = "root";
        int user = 1;

        final String PASSWORD = (user == 0) ? "v)YpyX1;179`" : "1234";

        // Database and tables creation SQL commands
        String sqlCreateDB = "CREATE DATABASE IF NOT EXISTS db_time_attendance;";
        String sqlUseDB = "USE db_time_attendance;";

        String sqlTemp =
                "CREATE TABLE IF NOT EXISTS t_emp (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "nameEmp VARCHAR(20), " +
                        "codeEmp CHAR(36)" +
                        ");";

        String sqlLockInRecord =
                "CREATE TABLE IF NOT EXISTS t_lock_in_record (" +
                        "id INT, " +
                        "check_in_time DATETIME, " +
                        "FOREIGN KEY(id) REFERENCES t_emp(id)" +
                        ");";

        String sqlAdmin =
                "CREATE TABLE IF NOT EXISTS t_admin (" +
                        "id INT PRIMARY KEY, " +
                        "username VARCHAR(20), " +
                        "passwordAdmin VARCHAR(20), " +
                        "FOREIGN KEY(id) REFERENCES t_emp(id)" +
                        ");";

        String sqlWorkTime =
                "CREATE TABLE IF NOT EXISTS t_work_time (" +
                        "id INT, " +
                        "startWork DATETIME, " +
                        "FOREIGN KEY(id) REFERENCES t_emp(id)" +
                        ");";

        String sqlInsertEmployee =
                "INSERT INTO t_emp (nameEmp, codeEmp) VALUES ('Professor', 'password');";

        // SQL to insert a new admin into the t_admin table (this will be executed after inserting into t_emp)
        String sqlInsertAdmin =
                "INSERT INTO t_admin (id, username, passwordAdmin) VALUES ((SELECT id FROM t_emp WHERE nameEmp = 'Professor'), 'Professor', 'password');";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Execute the SQL commands to create the database and tables
            statement.executeUpdate(sqlCreateDB);
            statement.executeUpdate(sqlUseDB);
            statement.executeUpdate(sqlTemp);
            statement.executeUpdate(sqlLockInRecord);
            statement.executeUpdate(sqlAdmin);
            statement.executeUpdate(sqlWorkTime);

            // Insert the employee (professor) into the t_emp table
            statement.executeUpdate(sqlInsertEmployee);

            // Insert the admin into the t_admin table, using the id of the professor inserted above
            statement.executeUpdate(sqlInsertAdmin);

            System.out.println("Database and tables created successfully!");
            System.out.println("Employee and Admin records inserted successfully!");

        } catch (SQLException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }
}