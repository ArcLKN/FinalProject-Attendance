import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public boolean isAdmin(int userId) throws SQLException {
        String sql = "SELECT id FROM t_admin WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
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
            return resultSet.next();
        }
    }
    public void updateUser(int id, String key, String value) throws SQLException {
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

        return username;
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
        return "";
    }

    public void saveImage(Blob image, int userId) throws SQLException {
        String query = "INSERT INTO t_emp_img (face_image, id) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBlob(1, image);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        }
    }

    public List<FaceData> getImages() throws SQLException {
        String query = "SELECT t_emp.id AS emp_id, t_emp_img.face_image " +
                "FROM t_emp_img " +
                "INNER JOIN t_emp ON t_emp.id = t_emp_img.id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<FaceData> faceDataList = new ArrayList<>();

            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                Blob faceImage = resultSet.getBlob("face_image");

                faceDataList.add(new FaceData(empId, faceImage));
            }

            return faceDataList;
        }
    }

}
