package IAHouseholdBudget;
  
import IAHouseholdBudget.LoginGUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ExpenseDetailsGUI extends JFrame implements ActionListener
{
    private JButton backButton; // Button to close window

    public ExpenseDetailsGUI(String name, String date, String price, String category)
    {
        super("Expense Details"); // Set window title

        this.setSize(650, 350); // Set window size
        this.setLocationRelativeTo(null); // Center window
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Close only this window
        this.setLayout(new BorderLayout()); // Use BorderLayout

        JPanel background = new JPanel(null); // Create background panel
        background.setBackground(LoginGUI.LIGHT_BLUE_BG); // Set background color
        this.setContentPane(background); // Set panel as content pane

        JLabel titleLabel = new JLabel("Expense Data"); // Create title label
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28)); // Set title font
        titleLabel.setForeground(LoginGUI.DARK_BLUE); // Set title color
        titleLabel.setBounds(200, 20, 300, 40); // Set title position
        background.add(titleLabel); // Add title to panel

        JLabel nameLabel = new JLabel("Name:"); // Create name label
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 22)); // Set font
        nameLabel.setForeground(LoginGUI.DARK_BLUE); // Set color
        nameLabel.setBounds(40, 80, 120, 30); // Set position
        background.add(nameLabel); // Add to panel

        JLabel nameValue = new JLabel(name); // Show expense name
        nameValue.setFont(new Font("SansSerif", Font.PLAIN, 22)); // Set font
        nameValue.setForeground(Color.BLACK); // Set color
        nameValue.setBounds(180, 80, 350, 30); // Set position
        background.add(nameValue); // Add to panel

        JLabel dateLabel = new JLabel("Date:"); // Create date label
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 22)); // Set font
        dateLabel.setForeground(LoginGUI.DARK_BLUE); // Set color
        dateLabel.setBounds(40, 130, 120, 30); // Set position
        background.add(dateLabel); // Add to panel

        JLabel dateValue = new JLabel(date); // Show expense date
        dateValue.setFont(new Font("SansSerif", Font.PLAIN, 22)); // Set font
        dateValue.setForeground(Color.BLACK); // Set color
        dateValue.setBounds(180, 130, 350, 30); // Set position
        background.add(dateValue); // Add to panel

        JLabel priceLabel = new JLabel("Price:"); // Create price label
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 22)); // Set font
        priceLabel.setForeground(LoginGUI.DARK_BLUE); // Set color
        priceLabel.setBounds(40, 180, 120, 30); // Set position
        background.add(priceLabel); // Add to panel

        JLabel priceValue = new JLabel("$" + price); // Show price value
        priceValue.setFont(new Font("SansSerif", Font.PLAIN, 22)); // Set font
        priceValue.setForeground(Color.BLACK); // Set color
        priceValue.setBounds(180, 180, 350, 30); // Set position
        background.add(priceValue); // Add to panel

        JLabel categoryLabel = new JLabel("Category:"); // Create category label
        categoryLabel.setFont(new Font("SansSerif", Font.PLAIN, 22)); // Set font
        categoryLabel.setForeground(LoginGUI.DARK_BLUE); // Set color
        categoryLabel.setBounds(40, 230, 120, 30); // Set position
        background.add(categoryLabel); // Add to panel

        JLabel categoryValue = new JLabel(category); // Show category
        categoryValue.setFont(new Font("SansSerif", Font.PLAIN, 22)); // Set font
        categoryValue.setForeground(Color.BLACK); // Set color
        categoryValue.setBounds(180, 230, 350, 30); // Set position
        background.add(categoryValue); // Add to panel

        backButton = new JButton("Back"); // Create back button
        backButton.setBounds(500, 260, 100, 35); // Set position
        backButton.setForeground(LoginGUI.DARK_BLUE); // Set text color
        backButton.setBackground(new Color(220, 235, 250)); // Set background color
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14)); // Set font
        backButton.setFocusPainted(false); // Remove focus border
        backButton.setOpaque(true); // Enable background
        backButton.setBorderPainted(true); // Show border
        backButton.addActionListener(this); // Add click listener
        background.add(backButton); // Add button to panel
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("Back")) // Check if Back clicked
        {
            this.dispose(); // Close window
        }
    }
}