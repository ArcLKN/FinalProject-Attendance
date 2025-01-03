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
    public boolean searchUser(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
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
    public void updateUser(int id, String key, String value) throws SQLException {
        // Construction de la requête SQL avec le nom de la colonne dynamique
        String sql = "UPDATE users SET " + key + " = ? WHERE id = ?";

        // Vérification que la clé est bien valide (au cas où)
        if (!key.equals("name") && !key.equals("email") && !key.equals("age")) {
            throw new IllegalArgumentException("Invalid column name: " + key);
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement . setString(1, value);
            statement . setInt(2, id);
            statement . executeUpdate();
            System.out.println("User updated successfully.");
        }
    }
    public String getUserName(int id) {
        String sqlName = "SELECT name FROM users WHERE id = ?";
        String username = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statementName = connection.prepareStatement(sqlName)) {
            // Set the id parameter in the query
            statementName.setInt(1, id);

            // Execute the query
            ResultSet response = statementName.executeQuery();

            // Check if a result is returned
            if (response.next()) {
                username = response.getString("name"); // Get the 'name' column
                System.out.println("User's name is: " + username);
            } else {
                System.out.println("No user found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while fetching user name: " + e.getMessage());
            throw new RuntimeException(e); // Handle or rethrow the exception as needed
        }

        return username; // Return the username or null if not found
    }

    public String getPassword(int id) {
        String sqlPassword = "SELECT password FROM users WHERE id = ?";
        String pswd = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statementPswd = connection.prepareStatement(sqlPassword)) {
            // Set the id parameter in the query
            statementPswd.setInt(1, id);

            // Execute the query
            ResultSet response = statementPswd.executeQuery();

            // Check if a result is returned
            if (response.next()) {
                pswd = response.getString("password"); // Get the 'name' column
            } else {
                System.out.println("Wrong password.");
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while fetching password: " + e.getMessage());
            throw new RuntimeException(e); // Handle or rethrow the exception as needed
        }

        return pswd; // Return the username or null if not found
    }
}
