import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws InterruptedException, SQLException {
        UserDAO userDAO = new UserDAO();

        userDAO.createUser("Joanne", "joanne@gmail.com", 30);
        userDAO.searchUser(1);
        new GUI("Attendance System", userDAO);
    }
}
