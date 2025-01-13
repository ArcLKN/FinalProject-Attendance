import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConnectionDAO {

    // Create a new connection (check-in) for a user
    public void createConnection(int userId, Date date) throws SQLException {
        String query = "INSERT INTO t_lock_in_record (id, check_in_time) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
            preparedStatement.executeUpdate();
        }
    }

    // Get the last connection (check-in) time for a user
    public Date searchLastUsersConnection(int userId) throws SQLException {
        String query = "SELECT check_in_time FROM t_lock_in_record WHERE id = ? ORDER BY check_in_time DESC LIMIT 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getTimestamp("check_in_time");
            }
        }
        return null;  // No previous connection found
    }

    // Insert start work time
    public Date insertStartWork(int id, Date start_work_time) throws SQLException {
        String sql = "INSERT INTO t_work_time (id, startWork) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setTimestamp(2, new Timestamp(start_work_time.getTime()));
            statement.executeUpdate();
            System.out.println("Start work time recorded for user " + id);

            // Return the fixed start time (10:00 AM) as a Date
            return new SimpleDateFormat("HH:mm:ss").parse("10:00:00"); // Fixed 10:00 AM start time
        } catch (ParseException e) {
            e.printStackTrace();
            throw new SQLException("Error parsing fixed start time.", e);
        }
    }

    public List<Object[]> getUserAttendanceRecords() throws SQLException {
        List<Object[]> records = new ArrayList<>();
        String sql = "SELECT t_emp.id, t_emp.nameEmp, t_lock_in_record.check_in_time " +
                "FROM t_emp " +
                "JOIN t_lock_in_record ON t_emp.id = t_lock_in_record.id " +
                "ORDER BY t_lock_in_record.check_in_time DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String userName = resultSet.getString("nameEmp");
                Timestamp checkInTime = resultSet.getTimestamp("check_in_time");

                // Format the check-in time to a readable format
                String formattedCheckInTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(checkInTime);

                // Add the data to the list
                records.add(new Object[]{userId, userName, formattedCheckInTime});
            }
        }
        return records;
    }

    // Get user info (adjusted for the correct columns)
    public List<Object[]> getUserInfo() throws SQLException {
        String sql = "SELECT t_emp.id, t_emp.nameEmp, t_emp.codeEmp FROM t_emp";

        List<Object[]> userInfo = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("nameEmp");
                String code = resultSet.getString("codeEmp");

                userInfo.add(new Object[]{id, name, code});
            }
        }
        return userInfo;
    }
}