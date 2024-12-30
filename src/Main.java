import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        //userDAO.createUser("Raphael", "raph@gmail.com", 20);
        //userDAO.searchUser(1);
        new GUI("Attendance System", userDAO);
    }
}
