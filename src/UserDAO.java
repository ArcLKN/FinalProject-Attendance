import java.sql.*;

public class UserDAO {
    public boolean isAdmin(int userId) throws SQLException {
        String sql = "SELECT id FROM t_admin WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();  // Returns true if the user is an admin
        }
    }
    public void createUser(String nameEmp, String codeEmp) throws SQLException {
        String sql = "INSERT INTO t_emp (nameEmp, codeEmp) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nameEmp);
            statement.setString(2, codeEmp);
            statement.executeUpdate();
            System.out.println("User created successfully.");
        }
    }
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM t_emp WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("User deleted successfully.");
        }
    }
    public boolean searchUser(int userId) throws SQLException {
        String sql = "SELECT id FROM t_emp WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();  // Returns true if the user exists
        }
    }
    public void updateUser(int id, String key, String value) throws SQLException {
        // Check for valid column names
        if (!key.equals("nameEmp") && !key.equals("codeEmp")) {
            throw new IllegalArgumentException("Invalid column name: " + key);
        }

        String sql = "UPDATE t_emp SET " + key + " = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, value);
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("User updated successfully.");
        }
    }
    public String getUserName(int id) {
        String sqlName = "SELECT nameEmp FROM t_emp WHERE id = ?";
        String username = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statementName = connection.prepareStatement(sqlName)) {
            statementName.setInt(1, id);
            ResultSet response = statementName.executeQuery();

            if (response.next()) {
                username = response.getString("nameEmp");
                System.out.println("User's name is: " + username);
            } else {
                System.out.println("No user found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while fetching user name: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return username; // Return the username or null if not found
    }

    public String getPassword(int userId) throws SQLException {
        String sql = "SELECT passwordAdmin FROM t_admin WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("passwordAdmin");
            }
        }
        return "";  // Return an empty string if password is not found
    }
}
