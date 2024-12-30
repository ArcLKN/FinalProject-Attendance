import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void createUser(String name, String email, int age) throws SQLException {
        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setInt(3, age);
            statement.executeUpdate();
            System.out.println("User created successfully.");
        }
    }
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement . setInt(1, id);
            statement . executeUpdate();
            System.out.println("User deleted successfully.");
        }
    }
    public void searchUser(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement . setInt(1, id);
            ResultSet response = statement.executeQuery();
            System.out.println("User searched successfully.");
            if (response.next()) {
                String username = response.getString("email");
                System.out.println("User's email is: " + username);
            }
        }
    }
    public void updateUser(int id, String key, String value) throws SQLException {
        String sql = "UPDATE users SET ? = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement . setString(1, key);
            statement . setString(2, value);
            statement . setInt(3, id);
            statement . executeUpdate();
            System.out.println("User updated successfully.");
        }
    }
}
