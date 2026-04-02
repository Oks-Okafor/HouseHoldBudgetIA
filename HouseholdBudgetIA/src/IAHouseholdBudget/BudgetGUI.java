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
    private JButton adjustButton;
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

        JLabel header = new JLabel("Monthly Budget", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(LoginGUI.DARK_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        background.add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        saveButton = createButton("Save Budget");
        adjustButton = createButton("Adjust Budget");
        backButton = createButton("Back");

        buttonPanel.add(saveButton);
        buttonPanel.add(adjustButton);
        buttonPanel.add(backButton);

        background.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text)
    {
        JButton button = new JButton(text);
        button.setForeground(LoginGUI.DARK_BLUE);
        button.setBackground(new Color(220, 235, 250));
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

        if (command.equals("Save Budget"))
        {
            handleSave();
        }
        else if (command.equals("Adjust Budget"))
        {
            handleView();
        }
        else if (command.equals("Back"))
        {
            this.dispose();
            new DashboardGUI(currentUserID).setVisible(true);
        }
    }

    private void handleSave()
    {
        String monthText = monthField.getText().trim();
        String yearText = yearField.getText().trim();
        String amountText = amountField.getText().trim();

        if (monthText.isEmpty() || yearText.isEmpty() || amountText.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Fill in all fields.");
            return;
        }

        int month;
        int year;
        double amount;

        try
        {
            month = Integer.parseInt(monthText);
            year = Integer.parseInt(yearText);
            amount = Double.parseDouble(amountText);
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Enter valid numbers only.");
            return;
        }

        if (month < 1 || month > 12)
        {
            JOptionPane.showMessageDialog(this, "Month must be between 1 and 12.");
            return;
        }

        if (year <= 0)
        {
            JOptionPane.showMessageDialog(this, "Enter a valid year.");
            return;
        }

        if (amount <= 0)
        {
            JOptionPane.showMessageDialog(this, "Budget amount must be greater than 0.");
            return;
        }

        boolean success = budgetDB.upsertBudget(currentUserID, month, year, amount);

        if (success)
        {
            JOptionPane.showMessageDialog(this, "Budget saved successfully");
            monthField.setText("");
            yearField.setText("");
            amountField.setText("");
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Failed to save budget.");
        }
    }

    private void handleView()
    {
        this.dispose();
        new AdjustChoiceGUI(currentUserID).setVisible(true);
    }
}