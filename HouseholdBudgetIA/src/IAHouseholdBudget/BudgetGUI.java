package IAHouseholdBudget;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BudgetGUI extends JFrame implements ActionListener
{
    private int currentUserID;
    private BudgetDBAccess budgetDB;

    private JTextField monthField;
    private JTextField yearField;
    private JTextField amountField;

    private JButton saveButton;
    private JButton viewButton;
    private JButton backButton;

    private JLabel resultLabel;

    public BudgetGUI(int userID)
    {
        super("Monthly Budget");

        this.currentUserID = userID;
        this.budgetDB = new BudgetDBAccess();

        this.setSize(600, 450);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(LoginGUI.LIGHT_BLUE_BG);
        this.setContentPane(background);

        // Header
        JLabel header = new JLabel("Monthly Budget", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(LoginGUI.DARK_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        background.add(header, BorderLayout.NORTH);

        // Center input panel
        JPanel inputPanel = new JPanel(new GridLayout(4,2,10,10));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        monthField = new JTextField();
        yearField = new JTextField();
        amountField = new JTextField();

        inputPanel.add(new JLabel("Month (1-12):"));
        inputPanel.add(monthField);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);
        inputPanel.add(new JLabel("Budget Amount:"));
        inputPanel.add(amountField);

        resultLabel = new JLabel(" ", SwingConstants.CENTER);
        resultLabel.setForeground(LoginGUI.DARK_BLUE);

        inputPanel.add(resultLabel);

        background.add(inputPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        saveButton = createButton("Save Budget");
        viewButton = createButton("View Budget");
        backButton = createButton("Back");

        buttonPanel.add(saveButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(backButton);

        background.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text)
    {
        JButton button = new JButton(text);
        button.setForeground(LoginGUI.DARK_BLUE);
        button.setBackground(new Color(220,235,250));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.addActionListener(this);
        return button;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if(command.equals("Save Budget"))
        {
            handleSave();
        }
        else if(command.equals("View Budget"))
        {
            handleView();
        }
        else if(command.equals("Back"))
        {
            this.dispose();
            new DashboardGUI(currentUserID).setVisible(true);
        }
    }

    private void handleSave()
    {
        int month = Integer.parseInt(monthField.getText());
        int year = Integer.parseInt(yearField.getText());
        double amount = Double.parseDouble(amountField.getText());

        budgetDB.upsertBudget(currentUserID, month, year, amount);

        resultLabel.setText("Budget saved.");
    }

    private void handleView()
    {
        int month = Integer.parseInt(monthField.getText());
        int year = Integer.parseInt(yearField.getText());

        double amount = budgetDB.getBudgetAmount(currentUserID, month, year);

        resultLabel.setText("Budget: $" + amount);
    }
}