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

    JPanel mainPanel;
    JPanel secondaryPanel;
    JPanel navBar;

    boolean doSignInLabel = false;
    JLabel signInAnswerLabel;
    JLabel signInErrorLabel;
    JTextField IdField;

    //Creating date and time
    SimpleDateFormat timeFormat;
    JLabel timeLabel;

    public GUI (String title, UserDAO userDAO) throws InterruptedException {
        super(title);
        this . userDAO = userDAO;
        this . setSize(400, 300);
        this.setLocation(500, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);

        mainPanel = new JPanel();

        mainPanel . setBounds (0 , 30 , 400 , 300) ;
        mainPanel . setLayout ( null ) ;
        mainPanel.setBackground(Color.WHITE);

        navBar = new JPanel();
        navBar . setBounds(0, 0, 400, 30);

        //Displaying date and time
        timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        timeLabel = new JLabel();
        mainPanel.add(timeLabel);
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> timeLabel.setText(timeFormat.format(new Date())));
        timer.start();
        //To center the date
        int panelWidth = mainPanel.getWidth();
        timeLabel.setBounds(0, 10, panelWidth, 30); // Center horizontally using full panel width
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton mainPanelButton = new JButton("Main");
        mainPanelButton.setBounds(0, 0, 50, 30);
        showMain switchToMain = new showMain();
        mainPanelButton.addActionListener(switchToMain);
        navBar.add(mainPanelButton);

        JButton secondaryPanelButton = new JButton("Secondary");
        secondaryPanelButton.setBounds(50, 0, 50, 30);
        showSecondary switchToSecondary = new showSecondary();
        secondaryPanelButton.addActionListener(switchToSecondary);
        navBar.add(secondaryPanelButton);

        //Sign in priority text
        JLabel labelPriority = new JLabel ("Sign in as...");
        labelPriority.setBounds (0, 50, panelWidth, 30) ;
        mainPanel.add(labelPriority) ;
        labelPriority.setHorizontalAlignment(SwingConstants.CENTER);

        //Sign in priority buttons
        JButton employeeButton = new JButton ("Employee"); //Interface UI button
        employeeButton.setBounds (150 , 100 , 100 , 40);
        mainPanel.add(employeeButton);

        JButton adminButton = new JButton ("Admin"); //Interface UI button
        adminButton.setBounds (150 , 150 , 100 , 40);
        mainPanel.add(adminButton);

        //ID label
//        JLabel label = new JLabel ("ID :");
//        label . setBounds (50 , 40 , 50 , 30) ;
//        mainPanel . add ( label ) ;

        //ID input
//        IdField = new JTextField (20) ;
//        IdField . setBounds (100 , 50 , 200 , 20) ;
//        IdField . getText () ;
//        mainPanel . add ( IdField ) ;

        //Sign in button
//        JButton signInButton = new JButton ("Sign in") ; //Interface UI button
//        signInButton . setBounds (150 , 80 , 100 , 40) ;
//        signIn signInButtonAction = new signIn(); //Creating an object from the signIn class
//        signInButton.addActionListener(signInButtonAction); //Making the UI button handle events as signIn class definition
//        mainPanel . add ( signInButton ) ;
        // button.setEnabled(false);


        //Sign in button onclick handling
//        signInAnswerLabel = new JLabel ("You signed in successfully!");
//        signInAnswerLabel . setBounds (50 , 130 , 300 , 50) ;
//        signInAnswerLabel . setForeground(Color.GREEN);
//        signInAnswerLabel . setVisible(doSignInLabel);
//        mainPanel . add ( signInAnswerLabel ) ;
//
//        signInErrorLabel = new JLabel ("There was an error during sign in.");
//        signInErrorLabel . setBounds (50 , 130 , 300 , 50) ;
//        signInErrorLabel . setForeground(Color.RED);
//        signInErrorLabel . setVisible(false);
//        mainPanel . add ( signInErrorLabel ) ;

        //Creating the second window

        secondaryPanel = new JPanel();
        secondaryPanel . setBounds (0 , 30 , 400 , 300) ;
        JLabel sTitle = new JLabel ("Secondary Panel!");
        sTitle . setBounds (50 , 40 , 50 , 30) ;
        secondaryPanel . add ( sTitle ) ;
        secondaryPanel.setVisible(false);

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
            signInAnswerLabel . setVisible(doSignInLabel);
            signInErrorLabel . setVisible(!doSignInLabel);
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

    public static void main(String[] args) {
    }
}
