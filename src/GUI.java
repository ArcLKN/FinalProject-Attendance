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

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    String timeStr = sdf.format(new Date());
    JLabel actualTimeLabel = new JLabel(timeStr);

    public GUI (String title, UserDAO userDAO) {
        super(title);
        this . userDAO = userDAO;
        this . setSize(400, 300);
        this.setLocation(500, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);

        navBar = new JPanel();
        navBar . setBounds(0, 0, 400, 30);

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

        mainPanel = new JPanel();

        mainPanel . setBounds (0 , 30 , 400 , 300) ;
        mainPanel . setLayout ( null ) ;
        mainPanel.setBackground(Color.WHITE);

        actualTimeLabel . setBounds(20, 30, 100, 20);
        mainPanel . add (actualTimeLabel);

        JLabel label = new JLabel ("ID :");
        label . setBounds (50 , 40 , 50 , 30) ;
        mainPanel . add ( label ) ;

        IdField = new JTextField (20) ;
        IdField . setBounds (100 , 50 , 200 , 20) ;
        IdField . getText () ;
        mainPanel . add ( IdField ) ;

        JButton signInButton = new JButton ("Sign in") ;
        signInButton . setBounds (150 , 80 , 100 , 40) ;
        signIn signInButtonAction = new signIn();
        signInButton.addActionListener(signInButtonAction);
        mainPanel . add ( signInButton ) ;
        // button.setEnabled(false);

        signInAnswerLabel = new JLabel ("You signed in successfully!");
        signInAnswerLabel . setBounds (50 , 130 , 300 , 50) ;
        signInAnswerLabel . setForeground(Color.GREEN);
        signInAnswerLabel . setVisible(doSignInLabel);
        mainPanel . add ( signInAnswerLabel ) ;

        signInErrorLabel = new JLabel ("There was an error during sign in.");
        signInErrorLabel . setBounds (50 , 130 , 300 , 50) ;
        signInErrorLabel . setForeground(Color.RED);
        signInErrorLabel . setVisible(false);
        mainPanel . add ( signInErrorLabel ) ;

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
