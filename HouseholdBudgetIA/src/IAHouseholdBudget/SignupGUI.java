package IAHouseholdBudget;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SignupGUI extends JFrame implements ActionListener
{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private UserDBAccess userDB;

    public SignupGUI()
    {
        super("Household Budget - Signup");

        this.setSize(550, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        userDB = new UserDBAccess();

        // Background (same as Login)
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(LoginGUI.LIGHT_BLUE_BG);
        this.setContentPane(background);

        // Header (same styling)
        JLabel titleLabel = new JLabel("Household Budget Signup", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(LoginGUI.DARK_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        background.add(titleLabel, BorderLayout.NORTH);

        // Form panel (same layout style as login)
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));
        formPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(LoginGUI.DARK_BLUE);
        usernameField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(LoginGUI.DARK_BLUE);
        passwordField = new JPasswordField(15);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(LoginGUI.DARK_BLUE);
        emailField = new JTextField(15);

        usernameField.setPreferredSize(new Dimension(220, 30));
        passwordField.setPreferredSize(new Dimension(220, 30));
        emailField.setPreferredSize(new Dimension(220, 30));

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);

        background.add(formPanel, BorderLayout.CENTER);

        // Buttons (same styling as login)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        JButton signupButton = new JButton("Signup");
        signupButton.setForeground(LoginGUI.DARK_BLUE);
        signupButton.setBackground(new Color(220, 235, 250));
        signupButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        signupButton.setFocusPainted(false);
        signupButton.setOpaque(true);
        signupButton.setBorderPainted(true);
        signupButton.setPreferredSize(new Dimension(120, 40));
        signupButton.addActionListener(this);

        JButton backButton = new JButton("Back");
        backButton.setForeground(LoginGUI.DARK_BLUE);
        backButton.setBackground(new Color(220, 235, 250));
        backButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        backButton.setBorderPainted(true);
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(this);

        buttonPanel.add(signupButton);
        buttonPanel.add(backButton);

        background.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("Signup"))
        {
            handleSignup();
        }
        else if (command.equals("Back"))
        {
            this.dispose();
            new LoginGUI().setVisible(true);
        }
    }

    private void handleSignup()
    {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();

        boolean success = userDB.insertUser(username, password, email);

        if (success)
        {
            JOptionPane.showMessageDialog(this, "Account created successfully");
            this.dispose();
            new LoginGUI().setVisible(true);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Signup failed. Username may already exist.");
        }
    }

    public static void main(String[] args)
    {
        DBManager.initialize();
        new SignupGUI().setVisible(true);
    }
}
