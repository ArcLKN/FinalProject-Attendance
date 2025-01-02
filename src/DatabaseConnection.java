import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USERNAME = "root";
    static int user = 1;

    private static final String PASSWORD = (user == 0) ? "v)YpyX1;179`" : "Andriamahandry.91";

    //v)YpyX1;179`
    //Andriamahandry.91

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
