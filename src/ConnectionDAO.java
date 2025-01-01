import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConnectionDAO {
    public void createConnection(int user_id, Date connection_date) throws SQLException {
        String sql = "INSERT INTO connections (user_id, connection_date) VALUES (?, ?)";
        java.sql.Timestamp utilDate = new java.sql.Timestamp(connection_date.getTime());

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user_id);
            statement.setTimestamp(2, utilDate);
            statement.executeUpdate();
            System.out.println("Connection created successfully.");
        }
    }
    public void deleteConnection(int id) throws SQLException {
        String sql = "DELETE FROM connections WHERE id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement . setInt(1, id);
            statement . executeUpdate();
            System.out.println("Connection deleted successfully.");
        }
    }
    public boolean searchConnection(int id) throws SQLException {
        String sql = "SELECT * FROM connections WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement . setInt(1, id);
            ResultSet response = statement.executeQuery();
            System.out.println("User searched successfully.");
            if (response.next()) {
                String username = response.getString("email");
                System.out.println("User's email is: " + username);
                return true;
            }
        }
        return false;
    }
    public void updateConnection(int id, String key, String value) throws SQLException {
        String sql = "UPDATE connections SET ? = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement . setString(1, key);
            statement . setString(2, value);
            statement . setInt(3, id);
            statement . executeUpdate();
            System.out.println("Connection updated successfully.");
        }
    }
}
