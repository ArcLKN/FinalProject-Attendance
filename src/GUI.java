import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Objects;
import java.util.List;

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

        //Facial Recognition Button
        JButton registerFaceIdButton = new JButton("New Face ID");
        registerFaceIdButton.setBounds(270, 40, 110, 30);
        registerFaceIdButton.addActionListener(new registerFaceIdAction());
        mainPanel.add(registerFaceIdButton);

        //Facial Connexion Button
        JButton faceIdButton = new JButton("Face ID");
        faceIdButton.setBounds(260 , 172 , 80 , 40) ;
        faceIdButton.addActionListener(new showFaceIdAction());
        mainPanel.add(faceIdButton);

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

    public void signInFromFaceId(int faceUserId) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = sdf.format(date);
        System.out.println("Button clicked! at " + timeStr);
        System.out.println("ID: " + faceUserId);

        boolean doUserExists = false;

        try {
            doUserExists = userDAO.searchUser(faceUserId);
        } catch (SQLException exception ) {
            exception.printStackTrace();
        }

        if (!doUserExists) {
            signInAnswerLabel.setText("This user ID does not exist.");
            signInLabelColor = Color.RED;
            signInAnswerLabel . setForeground(signInLabelColor);
            signInAnswerLabel . setVisible(true);
        } else {
            employeeLoginSuccess(faceUserId, date);
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
                adminAnswerLabel.setForeground(adminLabelColor);
                adminAnswerLabel.setVisible(true);
            } else if (!Objects.equals(IdFieldPswd.getText(), isPasswordTrue)) {
                adminAnswerLabel.setText("Wrong password or ID.");
                adminLabelColor = Color.RED;
                adminAnswerLabel.setForeground(adminLabelColor);
                adminAnswerLabel.setVisible(true);
            } else {
                adminLoginSuccess(adminId, date);

                Date latestLockIn = null;
                try {
                    latestLockIn = connectionDAO.searchLastUsersConnection(adminId);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (latestLockIn == null) {
                    try {
                        connectionDAO.createConnection(adminId, date);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        connectionDAO.insertStartWork(adminId, date);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println("Admin clocked in at " + date + " and start work time recorded.");
                } else {
                    System.out.println("Admin is already clocked in at " + latestLockIn);
                }
            }
        }
    }

    private class ShowAttendance implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog attendanceDialog = new JDialog(GUI.this, "Attendance Records", true);
            attendanceDialog.setSize(600, 400);
            attendanceDialog.setLocationRelativeTo(GUI.this);
            attendanceDialog.setLayout(new BorderLayout());

            String[] columnNames = {"ID", "Name", "Check-in Date"};

            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            try {
                List<Object[]> userConnections = connectionDAO.getUserAttendanceRecords();
                for (Object[] row : userConnections) {
                    tableModel.addRow(row);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(attendanceDialog, "Error retrieving user attendance records: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTable table = new JTable(tableModel);

            JScrollPane scrollPane = new JScrollPane(table);

            attendanceDialog.add(scrollPane, BorderLayout.CENTER);

            attendanceDialog.setVisible(true);
        }
    }
    private class ManageUsers implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog infoDialog = new JDialog(GUI.this, "Users Information", true);
            infoDialog.setSize(600, 400);
            infoDialog.setLocationRelativeTo(GUI.this);
            infoDialog.setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            JButton registerButton = new JButton("Add user");
            JButton editButton = new JButton("Edit user");
            JButton deleteButton = new JButton("Delete user");

            buttonPanel.add(registerButton);
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            infoDialog.add(buttonPanel, BorderLayout.SOUTH);

            String[] columnNames = {"ID", "Name", "Code"};

            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            try {
                List<Object[]> userInfo = connectionDAO.getUserInfo();
                for (Object[] row : userInfo) {
                    tableModel.addRow(row);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(infoDialog, "Error retrieving user information: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            infoDialog.add(scrollPane, BorderLayout.CENTER);

            registerButton.addActionListener(event -> {
                String name = JOptionPane.showInputDialog(infoDialog, "Enter user name:");
                String code = JOptionPane.showInputDialog(infoDialog, "Enter user code:");

                if (name != null && code != null) {
                    try {
                        userDAO.createUser(name, code);
                        JOptionPane.showMessageDialog(infoDialog, "User added successfully.");
                        Object[] newRow = {null, name, code};
                        tableModel.addRow(newRow);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(infoDialog, "Error adding user: " + ex.getMessage(),
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // Edit Button Action
            editButton.addActionListener(event -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int userId = (int) table.getValueAt(selectedRow, 0);
                    String currentName = (String) table.getValueAt(selectedRow, 1);
                    String currentCode = (String) table.getValueAt(selectedRow, 2);

                    String newName = JOptionPane.showInputDialog(infoDialog, "Enter new name:", currentName);
                    String newCode = JOptionPane.showInputDialog(infoDialog, "Enter new code:", currentCode);

                    if (newName != null && newCode != null) {
                        try {
                            userDAO.updateUser(userId, "nameEmp", newName);
                            userDAO.updateUser(userId, "codeEmp", newCode);

                            JOptionPane.showMessageDialog(infoDialog, "User updated successfully.");

                            table.setValueAt(newName, selectedRow, 1);
                            table.setValueAt(newCode, selectedRow, 2);

                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(infoDialog, "Error updating user: " + ex.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(infoDialog, "Please select a user to edit.");
                }
            });

            // Delete Button Action
            deleteButton.addActionListener(event -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int userId = (int) table.getValueAt(selectedRow, 0);

                    int confirm = JOptionPane.showConfirmDialog(infoDialog,
                            "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            userDAO.deleteUser(userId);
                            JOptionPane.showMessageDialog(infoDialog, "User deleted successfully.");
                            tableModel.removeRow(selectedRow);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(infoDialog, "Error deleting user: " + ex.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(infoDialog, "Please select a user to delete.");
                }
            });

            infoDialog.setVisible(true);
        }
    }

    private class showMain implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainPanel.setVisible(true);
            secondaryPanel.setVisible(false);
        }
    }
    private class showSecondary implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainPanel.setVisible(false);
            secondaryPanel.setVisible(true);
        }
    }
    private class registerFaceIdAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(() -> {
                FacialRecognition facialRecognitionWindow = null;
                try {
                    facialRecognitionWindow = new FacialRecognition(GUI.this, userDAO, true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                facialRecognitionWindow.StartFacialRecognition();
                FacialRecognition finalFacialRecognitionWindow = facialRecognitionWindow;
                facialRecognitionWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        finalFacialRecognitionWindow.stopCamera();
                        finalFacialRecognitionWindow.dispose();
                    }
                });
                facialRecognitionWindow.setVisible(true);
                facialRecognitionWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            });
        }
    }

    private class showFaceIdAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(() -> {
                FacialRecognition facialRecognitionWindow = null;
                try {
                    facialRecognitionWindow = new FacialRecognition(GUI.this, userDAO, false);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                facialRecognitionWindow.StartFacialRecognition();
                FacialRecognition finalFacialRecognitionWindow = facialRecognitionWindow;
                facialRecognitionWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        finalFacialRecognitionWindow.stopCamera();
                        finalFacialRecognitionWindow.dispose();
                    }
                });
                facialRecognitionWindow.setVisible(true);
                facialRecognitionWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            });
        }
    }


    private void employeeLoginSuccess(int userId, Date date) {
        Date lastConnectionDate = null;

        JDialog dialog = new JDialog(GUI.this, "Login Successful", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(GUI.this);
        dialog.setLayout(new BorderLayout());

        String userName = userDAO.getUserName(userId);
        JLabel messageLabel = new JLabel("Welcome " + userName, JLabel.CENTER);
        dialog.add(messageLabel, BorderLayout.NORTH);

        JLabel answerLabel = new JLabel("", JLabel.CENTER);
        Color answerColor;
        dialog.add(answerLabel, BorderLayout.CENTER);
        answerLabel.setVisible(false);

        JButton checkButton = new JButton("Check-in");
        checkButton.addActionListener(e -> {
            dialog.dispose();
            this.dispose();
            System.exit(0);
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(checkButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        try {
            lastConnectionDate = connectionDAO.searchLastUsersConnection(userId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Error checking last sign-in: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (lastConnectionDate != null) {
            Date currentDate = new Date();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String lastConnectionDay = dateFormatter.format(lastConnectionDate);
            String currentDay = dateFormatter.format(currentDate);

            if (lastConnectionDay.equals(currentDay)) {
                JOptionPane.showMessageDialog(dialog,
                        "This user has already signed in today.",
                        "Check-in Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            connectionDAO.createConnection(userId, date);
        } catch (SQLException exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Error creating new sign-in: " + exception.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date startWorkTime = null;
        try {
            startWorkTime = connectionDAO.insertStartWork(userId, date);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Error inserting start work time: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm:ss");
        String userHourStr = hourFormatter.format(date);
        Date userCheckInTime;

        try {
            userCheckInTime = hourFormatter.parse(userHourStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Error parsing sign-in time: " + ex.getMessage(),
                    "Time Parsing Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isUserLate = userCheckInTime.after(startWorkTime);

        if (isUserLate) {
            answerLabel.setText("You are late.");
            answerColor = Color.ORANGE;
        } else {
            answerLabel.setText("You signed in successfully!");
            answerColor = Color.GREEN;
        }

        answerLabel.setForeground(answerColor);
        answerLabel.setVisible(true);

        dialog.setVisible(true);
    }

    private void adminLoginSuccess(int adminId, Date date) {
        Date lastConnectionDate = null;

        JDialog dialog = new JDialog(GUI.this, "Admin Login Successful", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(GUI.this);
        dialog.setLayout(new BorderLayout());

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        dialog.add(messagePanel, BorderLayout.NORTH);

        JPanel modificationPanel = new JPanel();
        modificationPanel.setLayout(new BoxLayout(modificationPanel, BoxLayout.Y_AXIS));
        dialog.add(modificationPanel, BorderLayout.CENTER);

        JLabel messageLabel = new JLabel("Welcome " + userDAO.getUserName(adminId));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagePanel.add(messageLabel);

        JLabel answerLabel = new JLabel("");
        answerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Color answerColor;
        answerLabel.setVisible(false);
        messagePanel.add(answerLabel);

        JButton viewRecords = new JButton("View Attendance Records");
        ShowAttendance attendanceTable = new ShowAttendance();
        viewRecords.addActionListener(attendanceTable);
        modificationPanel.add(viewRecords);

        JButton manageUsersButton = new JButton("Manage Users");
        ManageUsers managementTable = new ManageUsers();
        manageUsersButton.addActionListener(managementTable);
        modificationPanel.add(manageUsersButton);

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
            Date currentDate = new Date();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String lastConnectionDay = dateFormatter.format(lastConnectionDate);
            String currentDay = dateFormatter.format(currentDate);

            boolean hasAdminAlreadySignedIn = lastConnectionDay.equals(currentDay);
            if (hasAdminAlreadySignedIn) {
                JOptionPane.showMessageDialog(dialog,
                        "This admin has already signed in today.",
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
        String adminHourStr = hourFormatter.format(date);
        Date adminHour;

        try {
            adminHour = hourFormatter.parse(adminHourStr);
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

        boolean isAdminLate = adminHour.after(fixedHour);

        if (isAdminLate) {
            answerLabel.setText("You are late.");
            answerColor = Color.ORANGE;
            answerLabel.setForeground(answerColor);
            answerLabel.setVisible(true);
        } else {
            answerLabel.setText("You signed in successfully!");
            answerColor = Color.GREEN;
            answerLabel.setForeground(answerColor);
            answerLabel.setVisible(true);
        }

        dialog.setVisible(true);
    }
}
