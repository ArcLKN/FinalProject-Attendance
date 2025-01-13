import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        UserDAO userDAO = new UserDAO();
        ConnectionDAO connectionDAO = new ConnectionDAO();

        //userDAO.createUser("Joanne", "joanne@gmail.com", 30);
        //userDAO.searchUser(3);
        new GUI("Attendance System", userDAO, connectionDAO);
    }
}