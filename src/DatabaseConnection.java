import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/db_time_attendance";
    private static final String USERNAME = "root";
    static int user = 1;

    private static final String PASSWORD = (user == 0) ? "v)YpyX1;179`" : "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}