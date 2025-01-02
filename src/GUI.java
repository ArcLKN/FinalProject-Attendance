import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.sql.SQLException;

public class GUI extends JFrame {
    private UserDAO userDAO;


    // ------ VARIABLES ------
    JPanel mainPanel;
    JPanel secondaryPanel;
    JPanel employeeCheck;
    JPanel navBar;

    boolean doSignInLabel = false;
    JLabel signInAnswerLabel;
    JLabel signInErrorLabel;
    JTextField IdField;

    JTextField IdFieldUser;
    JTextField IdFieldPswd;

    SimpleDateFormat timeFormat;
    JLabel timeLabel;
    SimpleDateFormat timeFormat2;
    JLabel timeLabel2;


    // ------ LOGIN INTERFACE ------
    public GUI (String title, UserDAO userDAO) throws InterruptedException {
        super(title);
        this.userDAO = userDAO;
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
        signInErrorLabel = new JLabel ("There was an error during sign in.");
        signInErrorLabel . setBounds (50 , 130 , 300 , 50) ;
        signInErrorLabel . setForeground(Color.RED);
        signInErrorLabel . setVisible(false);
        mainPanel . add ( signInErrorLabel ) ;


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

        //ID input username
        IdFieldUser = new JTextField (20) ;
        IdFieldUser . setBounds (100 , 80 , 200 , 20) ;
        IdFieldUser . getText () ;
        secondaryPanel . add ( IdFieldUser ) ;

        //ID label password
        JLabel labelPassword = new JLabel ("Enter your password :");
        labelPassword.setBounds(100 , 110 , 200 , 30) ;
        secondaryPanel.add(labelPassword) ;

        //ID input password
        IdFieldPswd = new JTextField (20) ;
        IdFieldPswd . setBounds (100 , 140 , 200 , 20) ;
        IdFieldPswd . getText () ;
        secondaryPanel . add ( IdFieldPswd ) ;

        //Sign in button
        JButton signInButton2 = new JButton ("Sign in") ; //Interface UI button
        signInButton2 . setBounds (150 , 172 , 100 , 40) ;
        signIn signInButtonAction2 = new signIn(); //Creating an object from the signIn class
        signInButton2.addActionListener(signInButtonAction2); //Making the UI button handle events as signIn class definition
        secondaryPanel . add ( signInButton2 ) ;


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

    private class signIn implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String timeStr = sdf.format(new Date());
            System.out.println("Button clicked! at " + timeStr);
            System.out.println("ID: " + IdField.getText());
            int userId = Integer.parseInt(IdField.getText());
            System.out.println("ID: " + userId);

            try {
                doSignInLabel = userDAO.searchUser(userId);
            } catch (SQLException exception ) {
                exception.printStackTrace();
            }

            if (doSignInLabel) {
                employeeLoginSuccess();
            } else {
                signInErrorLabel . setVisible(true);
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

    private void employeeLoginSuccess() {
        JDialog dialog = new JDialog(GUI.this, "Login successed", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(GUI.this); // Center relative to main panel
        dialog.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel("Welcome " + userDAO.getUserName(Integer.parseInt(IdField.getText())), JLabel.CENTER);
        dialog.add(messageLabel, BorderLayout.CENTER);

        JButton checkButton = new JButton("Check in");
        checkButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(checkButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    public static void main(String[] args) {
    }
}
