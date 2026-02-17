package IAHouseholdBudget;

import HouseholdBudgetIA.DashboardGUI;
import HouseholdBudgetIA.SignupGUI;
import com.sun.jdi.connect.spi.Connection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginGUI extends JFrame implements ActionListener
{
  private JTextField usernameField;
private JPasswordField passwordField;
private UserDBAccess userDB;
public static final Color LIGHT_BLUE_COLOR = new Color(173, 216, 230);
public static final Color BLUE = new Color(211, 211, 211);
public static final Color LIGHT_BLUE_BG = new Color(235, 245, 255);
public static final Color DARK_BLUE = new Color(20, 60, 120);
public LoginGUI(){
  super("Household Budget - Login");
this.setSize(550, 400);
this.setLocationRelativeTo(null);
this.setDefaultCloseOperation(EXIT_ON_CLOSE);
this.setLayout(new BorderLayout());
JPanel background = new JPanel(new BorderLayout());
background.setBackground(LIGHT_BLUE_BG);
this.setContentPane(background);
userDB = new UserDBAccess();
JLabel titleLabel = new JLabel("Household Budget Login", SwingConstants.CENTER);
titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
titleLabel.setForeground(DARK_BLUE);
titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
this.add(titleLabel, BorderLayout.NORTH);
JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
formPanel.setOpaque(false);
JLabel userLabel = new JLabel("Username:");
usernameField = new JTextField(15);
JLabel passLabel = new JLabel("Password:");
passwordField = new JPasswordField(15);
formPanel.add(userLabel);
formPanel.add(usernameField);
formPanel.add(passLabel);
formPanel.add(passwordField);
this.add(formPanel, BorderLayout.CENTER);
JPanel buttonPanel = new JPanel(new FlowLayout());

JButton loginButton = new JButton("Login");
loginButton.setBackground(new Color(220, 235, 250));
loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
loginButton.setFocusPainted(false);
JButton signupButton = new JButton("Signup");
signupButton.setBackground(new Color(220, 235, 250));
signupButton.setFont(new Font("SansSerif", Font.BOLD, 14));
signupButton.setFocusPainted(false);

loginButton.setOpaque(true);
loginButton.setBorderPainted(true);

signupButton.setOpaque(true);
signupButton.setBorderPainted(true);


loginButton.addActionListener(this);
signupButton.addActionListener(this);

buttonPanel.add(loginButton);
buttonPanel.add(signupButton);

this.add(buttonPanel, BorderLayout.SOUTH);
}
public void actionPerformed(ActionEvent e)
{
    String command = e.getActionCommand();

    if (command.equals("Login"))
    {
        handleLogin();
    }
    else if (command.equals("Signup"))
    {
        this.dispose();
        new SignupGUI().setVisible(true);
    }
}
private void handleLogin()
{
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());

    int userID = userDB.loginUser(username, password);

    if (userID > 0)
    {
        JOptionPane.showMessageDialog(this, "Login successful");
        this.dispose();
        new DashboardGUI(userID).setVisible(true);
    }
    else
    {
        JOptionPane.showMessageDialog(this, "Invalid username or password");
    }
}
public int loginUser(String username, String password)
{
    String sql = "SELECT userID FROM users WHERE username = ? AND password = ?";

    try (Connection conn = DBManager.getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(sql))
    {
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        if (rs.next())
        {
            return rs.getInt("userID");
        }

    } catch (SQLException e)
    {
        System.out.println("Login failed: " + e.getMessage());
    }

    return 0;
}

public static void main(String[] args)
{
    DBManager.initialize();
    new LoginGUI().setVisible(true);
}

}
