import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
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

    private class showAttendance implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog attendanceDialog = new JDialog(GUI.this, "Attendance Records", true);
            attendanceDialog.setSize(600, 400);
            attendanceDialog.setLocationRelativeTo(GUI.this);
            attendanceDialog.setLayout(new BorderLayout());

            // Colonnes du tableau
            String[] columnNames = {"ID", "Name", "Connection Date"};

            // Récupération des données
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            try {
                List<Object[]> userConnections = connectionDAO.getUserConnections();

                for (Object[] row : userConnections) {
                    tableModel.addRow(row);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(attendanceDialog, "Error retrieving user connections: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return; // Sortir si une erreur survient
            }

            // Création de la JTable
            JTable table = new JTable(tableModel);

            // Ajouter dans un JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);

            // Ajouter le JScrollPane au JDialog
            attendanceDialog.add(scrollPane, BorderLayout.CENTER);

            // Rendre la boîte de dialogue visible
            attendanceDialog.setVisible(true);
        }
    }
    private class manageUsers implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog infoDialog = new JDialog(GUI.this, "Users informations", true);
            infoDialog.setSize(600, 400);
            infoDialog.setLocationRelativeTo(GUI.this);
            infoDialog.setLayout(new BorderLayout());

            // Créer un JPanel pour contenir les boutons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout()); // ou GridLayout, BoxLayout, etc.

            // Créer les boutons
            JButton registerButton = new JButton("Add user");
            JButton editButton = new JButton("Edit user");
            JButton deleteButton = new JButton("Delete user");

            // Ajouter les boutons au JPanel
            buttonPanel.add(registerButton);
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            // Ajouter le JPanel au JDialog dans la zone sud (SOUTH)
            infoDialog.add(buttonPanel, BorderLayout.SOUTH);

            // Colonnes du tableau
            String[] columnNames = {"ID", "Name", "Age", "Email"};

            // Récupération des données
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            try {
                List<Object[]> userInfo = connectionDAO.getUserInfo();

                for (Object[] row : userInfo) {
                    tableModel.addRow(row);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(infoDialog, "Error retrieving user informations: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                return; // Sortir si une erreur survient
            }

            // Création de la JTable
            JTable table = new JTable(tableModel);

            // Ajouter dans un JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);

            // Ajouter le JScrollPane au JDialog
            infoDialog.add(scrollPane, BorderLayout.CENTER);



            registerButton.addActionListener(event -> {
                // Ouvrir un formulaire ou une boîte de dialogue pour entrer les informations
                String name = JOptionPane.showInputDialog(infoDialog, "Enter user name:");
                String email = JOptionPane.showInputDialog(infoDialog, "Enter user email:");
                String ageStr = JOptionPane.showInputDialog(infoDialog, "Enter user age:");

                if (name != null && email != null && ageStr != null) {
                    try {
                        int age = Integer.parseInt(ageStr);
                        UserDAO userDAO = new UserDAO();
                        userDAO.createUser(name, email, age);
                        JOptionPane.showMessageDialog(infoDialog, "User added successfully.");
                        // Ajouter l'utilisateur au tableau (mise à jour en temps réel)
                        Object[] newRow = {null, name, age, email}; // L'ID sera généré automatiquement par la base de données
                        tableModel.addRow(newRow);  // Ajoute la ligne au modèle de la table
                    } catch (SQLException | NumberFormatException ex) {
                        JOptionPane.showMessageDialog(infoDialog, "Error adding user: " + ex.getMessage(),
                                "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // Action pour le bouton "Edit user"
            editButton.addActionListener(event -> {
                // Vérifiez que l'utilisateur a sélectionné une ligne
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Récupérer les données de l'utilisateur sélectionné
                    int userId = (int) table.getValueAt(selectedRow, 0); // Supposons que l'ID est dans la première colonne
                    String currentName = (String) table.getValueAt(selectedRow, 1);
                    String currentEmail = (String) table.getValueAt(selectedRow, 3);
                    int currentAge = (int) table.getValueAt(selectedRow, 2);

                    // Afficher une boîte de dialogue pour modifier les informations
                    String newName = JOptionPane.showInputDialog(infoDialog, "Enter new name:", currentName);
                    String newEmail = JOptionPane.showInputDialog(infoDialog, "Enter new email:", currentEmail);
                    String newAgeStr = JOptionPane.showInputDialog(infoDialog, "Enter new age:", currentAge);

                    if (newName != null && newEmail != null && newAgeStr != null) {
                        try {
                            int newAge = Integer.parseInt(newAgeStr);

                            // Mise à jour dans la base de données
                            UserDAO userDAO = new UserDAO();
                            userDAO.updateUser(userId, "name", newName);  // Mettre à jour le nom
                            userDAO.updateUser(userId, "email", newEmail);  // Mettre à jour l'email
                            userDAO.updateUser(userId, "age", String.valueOf(newAge));  // Mettre à jour l'âge

                            JOptionPane.showMessageDialog(infoDialog, "User updated successfully.");

                            // Mettre à jour la JTable avec les nouvelles valeurs
                            table.setValueAt(newName, selectedRow, 1);  // Met à jour la colonne du nom
                            table.setValueAt(newEmail, selectedRow, 3);  // Met à jour la colonne de l'email
                            table.setValueAt(newAge, selectedRow, 2);  // Met à jour la colonne de l'âge

                        } catch (SQLException | NumberFormatException ex) {
                            JOptionPane.showMessageDialog(infoDialog, "Error updating user: " + ex.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(infoDialog, "Please select a user to edit.");
                }
            });

            // Action pour le bouton "Delete user"
            deleteButton.addActionListener(event -> {
                // Vérifiez que l'utilisateur a sélectionné une ligne
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int userId = (int) table.getValueAt(selectedRow, 0); // Supposons que l'ID soit dans la première colonne

                    // Demander une confirmation
                    int confirm = JOptionPane.showConfirmDialog(infoDialog,
                            "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            UserDAO userDAO = new UserDAO();
                            userDAO.deleteUser(userId);
                            JOptionPane.showMessageDialog(infoDialog, "User deleted successfully.");
                            tableModel.removeRow(selectedRow);  // Retirer la ligne sélectionnée de la table
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(infoDialog, "Error deleting user: " + ex.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(infoDialog, "Please select a user to delete.");
                }
            });

            // Rendre la boîte de dialogue visible
            infoDialog.setVisible(true);
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
        showAttendance attendanceTable = new showAttendance();
        viewRecords.addActionListener(attendanceTable);
        modificationPanel.add(viewRecords);

        JButton managementUser = new JButton("Manage users");
        manageUsers managementTable = new manageUsers();
        managementUser.addActionListener(managementTable);
        modificationPanel.add(managementUser);

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
