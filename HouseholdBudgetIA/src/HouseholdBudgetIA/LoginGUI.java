package HouseholdBudgetIA;

import javax.swing.*;
import java.awt.*;

/*
 LoginGUI
 This class allows a user to log into the system
 using a username and password stored in the database.
*/
public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private UserDBAccess userDB;

    public LoginGUI() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userDB = new UserDBAccess();

        initComponents();
        setVisible(true);

        System.out.println("LoginGUI launched");
    }

    private void initComponents() {

        JLabel titleLabel = new JLabel("User Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        setLayout(new BorderLayout(10, 10));
        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // -------- BUTTON ACTIONS --------

        loginButton.addActionListener(e -> loginUser());

        exitButton.addActionListener(e -> {
            System.out.println("Login cancelled");
            dispose();
        });
    }

    private void loginUser() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userID = userDB.loginUser(username, password);

        if (userID > 0) {
            JOptionPane.showMessageDialog(this,
                    "Login successful",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            System.out.println("User logged in with ID: " + userID);

            // TEMP: open CategoryGUI after login
            dispose();
            new CategoryGUI();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);

            System.out.println("Login failed for user: " + username);
        }
    }

    // ---------- REQUIRED HOMEWORK TEST ----------

    public static void main(String[] args) {
        DBManager.initialize();
        new LoginGUI();
    }
}
