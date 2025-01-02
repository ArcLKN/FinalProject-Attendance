import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.sql.SQLException;
import java.util.Objects;

public class GUI extends JFrame {
    private UserDAO userDAO;
    private ConnectionDAO connectionDAO;


    // ------ VARIABLES ------
    JPanel mainPanel;
    JPanel secondaryPanel;
    JPanel navBar;

    boolean doSignInLabel = false;
    JLabel signInAnswerLabel;
    JTextField IdField;
    Color signInLabelColor = Color.GREEN;

    JTextField IdFieldAdmin;
    JTextField IdFieldPswd;
    JLabel adminAnswerLabel;
    Color adminLabelColor;

    SimpleDateFormat timeFormat;
    JLabel timeLabel;
    SimpleDateFormat timeFormat2;
    JLabel timeLabel2;


    // ------ LOGIN INTERFACE ------
    public GUI (String title, UserDAO userDAO, ConnectionDAO connectionDAO) throws InterruptedException {
        super(title);
        this.userDAO = userDAO;
        this.connectionDAO = connectionDAO;
        this.setSize(400, 300);
        this.setLocation(500, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);


        // ------ MAIN PANEL ------
        mainPanel = new JPanel();
        mainPanel . setBounds (0 , 30 , 400 , 300) ;
        mainPanel . setLayout ( null ) ;
        mainPanel.setBackground(Color.WHITE);

        //Date and time
        timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        timeLabel = new JLabel();
        mainPanel.add(timeLabel);
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> timeLabel.setText(timeFormat.format(new Date())));
        timer.start();
        int panelWidth = mainPanel.getWidth();
        timeLabel.setBounds(32, 40, 200, 30);

        //ID label
        JLabel label = new JLabel ("Enter your ID :");
        label.setBounds(0 , 80 , panelWidth , 30) ;
        mainPanel.add(label) ;
        label.setHorizontalAlignment(SwingConstants.CENTER);

        //ID input
        IdField = new JTextField (20) ;
        IdField . setBounds (100 , 128 , 200 , 20) ;
        IdField . getText () ;
        mainPanel . add ( IdField ) ;

        //Sign in button
        JButton signInButton = new JButton ("Sign in") ; //Interface UI button
        signInButton . setBounds (150 , 172 , 100 , 40) ;
        signIn signInButtonAction = new signIn(); //Creating an object from the signIn class
        signInButton.addActionListener(signInButtonAction); //Making the UI button handle events as signIn class definition
        mainPanel . add ( signInButton ) ;
        // button.setEnabled(false);

        //Sign in button onclick handling
        signInAnswerLabel = new JLabel ("You signed in successfully!");
        signInAnswerLabel . setBounds (50 , 130 , 300 , 50) ;
        signInAnswerLabel . setForeground(signInLabelColor);
        signInAnswerLabel . setVisible(doSignInLabel);
        mainPanel . add ( signInAnswerLabel ) ;


        //------ ADMIN WINDOW ------
        secondaryPanel = new JPanel();
        secondaryPanel . setBounds (0 , 30 , 400 , 300) ;
        secondaryPanel . setLayout ( null ) ;
        secondaryPanel.setBackground(Color.WHITE);
        secondaryPanel.setVisible(false);

        //Date and time
        timeFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        timeLabel2 = new JLabel();
        secondaryPanel.add(timeLabel2);
        javax.swing.Timer timer2 = new javax.swing.Timer(1000, e -> timeLabel2.setText(timeFormat2.format(new Date())));
        timer2.start();
        timeLabel2.setBounds(32, 10, 200, 30);

        //ID label username
        JLabel labelUsername = new JLabel ("Enter your ID :");
        labelUsername.setBounds(100 , 50 , 200 , 30) ;
        secondaryPanel.add(labelUsername) ;

        //ID input admin
        IdFieldAdmin = new JTextField (20) ;
        IdFieldAdmin . setBounds (100 , 80 , 200 , 20) ;
        IdFieldAdmin . getText () ;
        secondaryPanel . add ( IdFieldAdmin ) ;

        //ID label password
        JLabel labelPassword = new JLabel ("Enter your password :");
        labelPassword.setBounds(100 , 110 , 200 , 30) ;
        secondaryPanel.add(labelPassword) ;

        //ID input password
        IdFieldPswd = new JTextField (20) ;
        IdFieldPswd . setBounds (100 , 140 , 200 , 20) ;
        IdFieldPswd . getText () ;
        secondaryPanel . add ( IdFieldPswd ) ;

        //Sign in button admin
        JButton signInAdminButton = new JButton ("Sign in") ; //Interface UI button
        signInAdminButton . setBounds (150 , 172 , 100 , 40) ;
        signInAdmin signInAdminButtonAction = new signInAdmin(); //Creating an object from the signIn class
        signInAdminButton.addActionListener(signInAdminButtonAction); //Making the UI button handle events as signIn class definition
        secondaryPanel . add ( signInAdminButton ) ;
        // button.setEnabled(false);

        //Sign in button admin onclick handling
        adminAnswerLabel = new JLabel ("");
        adminAnswerLabel . setBounds (50 , 30 , 300 , 50) ;
        adminAnswerLabel . setForeground(adminLabelColor);
        adminAnswerLabel . setVisible(doSignInLabel);
        secondaryPanel . add ( adminAnswerLabel ) ;


        //------ NAVBAR ------
        navBar = new JPanel();
        navBar . setBounds(0, 0, 400, 30);

        //Employee panel button
        JButton mainPanelButton = new JButton("Employee");
        mainPanelButton.setBounds(0, 0, 50, 30);
        showMain switchToMain = new showMain();
        mainPanelButton.addActionListener(switchToMain);
        navBar.add(mainPanelButton);

        //Admin panel button
        JButton secondaryPanelButton = new JButton("Admin");
        secondaryPanelButton.setBounds(50, 0, 50, 30);
        showSecondary switchToSecondary = new showSecondary();
        secondaryPanelButton.addActionListener(switchToSecondary);
        navBar.add(secondaryPanelButton);

        this . add(navBar);
        this . add(secondaryPanel);
        this . add(mainPanel);
        this . setVisible ( true ) ;
    }

    //------ FUNCTIONS ------
    private class signIn implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStr = sdf.format(date);
            System.out.println("Button clicked! at " + timeStr);
            System.out.println("ID: " + IdField.getText());
            int userId = Integer.parseInt(IdField.getText());
            System.out.println("ID: " + userId);

            boolean doUserExists = false;

            try {
                doUserExists = userDAO.searchUser(userId);
            } catch (SQLException exception ) {
                exception.printStackTrace();
            }

            if (!doUserExists) {
                signInAnswerLabel.setText("This user ID does not exist.");
                signInLabelColor = Color.RED;
                signInAnswerLabel . setForeground(signInLabelColor);
                signInAnswerLabel . setVisible(true);
            } else {
                employeeLoginSuccess(userId, date);
            }
        }
    }

    private class signInAdmin implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStr = sdf.format(date);
            System.out.println("Button clicked! at " + timeStr);
            System.out.println("ID: " + IdFieldAdmin.getText());
            int adminId = Integer.parseInt(IdFieldAdmin.getText());
            System.out.println("ID: " + adminId);

            boolean doUserExists = false;
            String isPasswordTrue = "";

            try {
                doUserExists = userDAO.searchUser(adminId);
                isPasswordTrue = userDAO.getPassword(Integer.parseInt(IdFieldAdmin.getText()));
            } catch (SQLException exception ) {
                exception.printStackTrace();
            }

            if (!doUserExists) {
                adminAnswerLabel.setText("This user ID does not exist.");
                adminLabelColor = Color.RED;
                adminAnswerLabel . setForeground(adminLabelColor);
                adminAnswerLabel . setVisible(true);
            } else if (!Objects.equals(IdFieldPswd.getText(), isPasswordTrue)) {
                adminAnswerLabel.setText("Wrong password or ID.");
                adminLabelColor = Color.RED;
                adminAnswerLabel . setForeground(adminLabelColor);
                adminAnswerLabel . setVisible(true);
            } else {
                adminLoginSuccess(adminId, date);
            }
        }
    }

    private class showMain implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainPanel . setVisible(true);
            secondaryPanel . setVisible(false);
        }
    }
    private class showSecondary implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainPanel . setVisible(false);
            secondaryPanel . setVisible(true);
        }
    }

    private void employeeLoginSuccess(int userId, Date date) {
        Date lastConnectionDate = null;

        //Dialog creation
        JDialog dialog = new JDialog(GUI.this, "Login successed", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(GUI.this); // Center relative to main panel
        dialog.setLayout(new BorderLayout());

        //Welcome message
        JLabel messageLabel = new JLabel("Welcome " + userDAO.getUserName(Integer.parseInt(IdField.getText())), JLabel.CENTER);
        dialog.add(messageLabel, BorderLayout.NORTH);

        JLabel AnswerCheckedLabel = new JLabel("", JLabel.CENTER);
        Color AnswerCheckedColor;
        dialog.add(AnswerCheckedLabel, BorderLayout.CENTER);
        AnswerCheckedLabel.setVisible(false);

        JButton checkButton = new JButton("Check in");
        checkButton.addActionListener(e -> {
            dialog.dispose(); // Fermer le dialogue
            this.dispose();
            System.exit(0);
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(checkButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        try {
            lastConnectionDate = connectionDAO.searchLastUsersConnection(userId);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        if (lastConnectionDate != null) {
            //User is checking in for the first time today
            Date currentDate = new Date();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String lastConnectionDay = dateFormatter.format(lastConnectionDate);
            String currentDay = dateFormatter.format(currentDate);
            System.out.println("User's last connection date is: " + lastConnectionDate);
            System.out.println("Last connection day: " + lastConnectionDay);

            //If user already checked in today
            boolean hasUserAlreadySignedIn = lastConnectionDay.equals(currentDay);
            if (hasUserAlreadySignedIn) {
                //Not sure if it works
                JOptionPane.showMessageDialog(dialog,
                        "This user has already signed-in today.",
                        "Check-in Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            connectionDAO.createConnection(userId, date);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm:ss");
        String userHourStr = hourFormatter.format(date);
        Date userHour;

        try {
            userHour = hourFormatter.parse(userHourStr);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        String fixedHourStr = "10:00:00";
        Date fixedHour;

        try {
            fixedHour = hourFormatter.parse(fixedHourStr);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        boolean isUserLate = userHour.after(fixedHour);

        if (isUserLate) {
            AnswerCheckedLabel.setText("You are late.");
            AnswerCheckedColor = Color.ORANGE;
            AnswerCheckedLabel . setForeground(AnswerCheckedColor);
            AnswerCheckedLabel . setVisible(true);
        } else {
            signInAnswerLabel.setText("You signed-in successfully!");
            signInLabelColor = Color.GREEN;
            signInAnswerLabel . setForeground(signInLabelColor);
            signInAnswerLabel . setVisible(true);
        }

        dialog.setVisible(true);
    }

    private void adminLoginSuccess(int adminId, Date date) {
        Date lastConnectionDate = null;

        //Dialog creation
        JDialog dialog = new JDialog(GUI.this, "Admin login successed", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(GUI.this); // Center relative to main panel
        dialog.setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        dialog.add(messagePanel, BorderLayout.NORTH);

        JPanel modificationPanel = new JPanel();
        modificationPanel.setLayout(new BoxLayout(modificationPanel, BoxLayout.Y_AXIS));
        dialog.add(modificationPanel, BorderLayout.CENTER);

        // Welcome message
        JLabel messageLabel = new JLabel("Welcome " + userDAO.getUserName(Integer.parseInt(IdFieldAdmin.getText())));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        messagePanel.add(messageLabel);

        // Late or success message
        JLabel AnswerCheckedLabel = new JLabel("");
        AnswerCheckedLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment
        Color AnswerCheckedColor;
        AnswerCheckedLabel.setVisible(false);
        messagePanel.add(AnswerCheckedLabel);

        JButton viewRecords = new JButton("View attendance records");
        modificationPanel.add(viewRecords);

        JButton registerUser = new JButton("Register new user");
        modificationPanel.add(registerUser);

        JButton editUser = new JButton("Edit user");
        modificationPanel.add(editUser);

        JButton deleteUser = new JButton("Delete user");
        modificationPanel.add(deleteUser);

        JButton checkButton = new JButton("Finish");
        checkButton.addActionListener(e -> {
            dialog.dispose();
            this.dispose();
            System.exit(0);
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(checkButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        try {
            lastConnectionDate = connectionDAO.searchLastUsersConnection(adminId);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        if (lastConnectionDate != null) {
            //User is checking in for the first time today
            Date currentDate = new Date();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String lastConnectionDay = dateFormatter.format(lastConnectionDate);
            String currentDay = dateFormatter.format(currentDate);
            System.out.println("User's last connection date is: " + lastConnectionDate);
            System.out.println("Last connection day: " + lastConnectionDay);

            //If user already checked in today
            boolean hasUserAlreadySignedIn = lastConnectionDay.equals(currentDay);
            if (hasUserAlreadySignedIn) {
                JOptionPane.showMessageDialog(dialog,
                        "This user has already signed-in today.",
                        "Check-in Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            connectionDAO.createConnection(adminId, date);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm:ss");
        String userHourStr = hourFormatter.format(date);
        Date userHour;

        try {
            userHour = hourFormatter.parse(userHourStr);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        String fixedHourStr = "10:00:00";
        Date fixedHour;

        try {
            fixedHour = hourFormatter.parse(fixedHourStr);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        boolean isUserLate = userHour.after(fixedHour);

        if (isUserLate) {
            AnswerCheckedLabel.setText("You are late.");
            AnswerCheckedColor = Color.ORANGE;
            AnswerCheckedLabel . setForeground(AnswerCheckedColor);
            AnswerCheckedLabel . setVisible(true);
        } else {
            AnswerCheckedLabel.setText("You signed-in successfully!");
            AnswerCheckedColor = Color.GREEN;
            AnswerCheckedLabel . setForeground(AnswerCheckedColor);
            AnswerCheckedLabel . setVisible(true);
        }

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
    }
}
