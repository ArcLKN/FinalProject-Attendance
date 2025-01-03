import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* connections TABLE
CREATE TABLE connections(
id INT AUTO_INCREMENT PRIMARY KEY,
user_id INT NOT NULL,
connection_date DATETIME NOT NULL,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
 */

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
                String connection_date = response.getString("id");
                System.out.println("User's connection_date is: " + connection_date);
                return true;
            }
        }
        return false;
    }
    public boolean searchUsersConnections(int user_id) throws SQLException {
        String sql = "SELECT * FROM connections WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement . setInt(1, user_id);
            ResultSet response = statement.executeQuery();
            System.out.println("User searched successfully.");
            if (response.next()) {
                String connection_date = response.getString("connection_date");
                System.out.println("User's connection_date is: " + connection_date);
                return true;
            }
        }
        return false;
    }
    public Date searchLastUsersConnection(int user_id) throws SQLException {
        String sql = "SELECT * FROM connections WHERE user_id = ? ORDER BY connection_date DESC LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user_id);
            ResultSet response = statement.executeQuery();
            System.out.println("User's last connection search executed successfully.");
            if (response.next()) {
                Timestamp lastConnectionTimestamp = response.getTimestamp("connection_date");
                Date lastConnectionDate = new Date(lastConnectionTimestamp.getTime());
                return lastConnectionDate;
            }
        }
        return null;
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

    //ChatGPT
    public List<Object[]> getUserConnections() throws SQLException {
        String sql = "SELECT users.id, users.name, connections.connection_date " +
                "FROM users " +
                "INNER JOIN connections ON users.id = connections.user_id " +
                "ORDER BY connections.connection_date DESC";

        List<Object[]> userConnections = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Timestamp timestamp = resultSet.getTimestamp("connection_date");
                Date connectionDate = new Date(timestamp.getTime());

                userConnections.add(new Object[]{id, name, connectionDate});
            }
        }
        return userConnections;
    }

    public List<Object[]> getUserInfo() throws SQLException {
        String sql = "SELECT users.id, users.name, users.age, users.email FROM users ";

        List<Object[]> userInfo = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");

                userInfo.add(new Object[]{id, name, age, email});
            }
        }
        return userInfo;
    }
}
