package HouseholdBudgetIA;

import javax.swing.*;
import java.awt.*;

import HouseholdBudgetIA.DBManager;
import HouseholdBudgetIA.LoginGUI;
import HouseholdBudgetIA.UserDBAccess;
import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/*
 SignupGUI
 This class allows a new user to create an account
 by entering a username and password, which are
 stored in the database.
*/
public class SignupGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private UserDBAccess userDB;

    public SignupGUI() {
        setTitle("Sign Up");
        setSize(380, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        userDB = new UserDBAccess();

        initComponents();
        setVisible(true);

        System.out.println("SignupGUI launched");
    }

    private void initComponents() {

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);

        JButton signupButton = new JButton("Sign Up");
        JButton cancelButton = new JButton("Cancel");

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(signupButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout(10, 10));
        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // -------- BUTTON ACTIONS --------

        signupButton.addActionListener(e -> createAccount());

        cancelButton.addActionListener(e -> {
            System.out.println("Signup cancelled");
            dispose();
            new LoginGUI();
        });
    }

    private void createAccount() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = userDB.insertUser(username, password);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Account created successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            System.out.println("New user created: " + username);

            dispose();
            new LoginGUI();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create account (username may already exist)",
                    "Signup Failed",
                    JOptionPane.ERROR_MESSAGE);

            System.out.println("Signup failed for user: " + username);
        }
    }

    // ---------- REQUIRED HOMEWORK TEST ----------

    public static void main(String[] args) {
        DBManager.initialize();
        new SignupGUI();
    }
}
