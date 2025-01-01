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

    boolean doSignInLabel = false;
    JLabel signInAnswerLabel;
    JLabel signInErrorLabel;
    JTextField IdField;

    //Creating date and time
    SimpleDateFormat timeFormat;
    JLabel timeLabel;

    public GUI (String title, UserDAO userDAO) throws InterruptedException {
        super(title);
        this . setSize(400, 300);
        this.setLocation(500, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);

        JPanel mainPanel = new JPanel();

        //Setting window
        mainPanel . setBounds (0 , 0 , 400 , 300) ;
        mainPanel . setLayout ( null ) ;
        mainPanel.setBackground(Color.WHITE);

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


        //Creating the window
        this . add(mainPanel);
        this . setVisible ( true ) ;
    }

    //ActionListener is an imported interface that handles events.
    //We create a class signIn implemented on ActionListener
//    private class signIn implements ActionListener {
//        @Override
//
//        //Instructions that will occur once the button is pressed
//        public void actionPerformed(ActionEvent e) {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
//            String timeStr = sdf.format(new Date());
//            System.out.println("Button clicked! at " + timeStr);
//            System.out.println("ID: " + IdField.getText());
//            int userId = Integer.parseInt(IdField.getText());
//            System.out.println("ID: " + userId);
//            try {
//                userDAO.searchUser(userId);
//            } catch (SQLException exception ) {
//                exception.printStackTrace();
//            }
//
//
//            doSignInLabel = !doSignInLabel;
//            signInAnswerLabel . setVisible(doSignInLabel);
//            signInErrorLabel . setVisible(!doSignInLabel);
//        }
//    }

    public static void main(String[] args) {
    }
}
