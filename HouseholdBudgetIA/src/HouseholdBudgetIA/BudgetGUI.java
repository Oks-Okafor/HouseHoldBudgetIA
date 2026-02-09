package HouseholdBudgetIA;

import javax.swing.*;
import java.awt.*;

/*
 BudgetGUI
 Allows the user to set or update a monthly budget.
*/
public class BudgetGUI extends JFrame {

    private JComboBox<String> monthBox;
    private JTextField yearField;
    private JTextField amountField;
    private JLabel currentBudgetLabel;

    private BudgetDBAccess budgetDB;

    // TEMP userID for homework stage
    private int userID = 1;

    public BudgetGUI() {
        setTitle("Set Monthly Budget");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        budgetDB = new BudgetDBAccess();

        initComponents();
        setVisible(true);

        System.out.println("BudgetGUI launched");
    }

    private void initComponents() {

        JLabel titleLabel = new JLabel("Monthly Budget");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        monthBox = new JComboBox<>(new String[]{
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        });

        yearField = new JTextField("2025", 6);
        amountField = new JTextField(10);

        currentBudgetLabel = new JLabel("Current Budget: Not set");

        JButton saveButton = new JButton("Save Budget");
        JButton viewButton = new JButton("View Current");
        JButton backButton = new JButton("Back");

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        formPanel.add(new JLabel("Month:"));
        formPanel.add(monthBox);
        formPanel.add(new JLabel("Year:"));
        formPanel.add(yearField);
        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);
        formPanel.add(new JLabel(""));
        formPanel.add(currentBudgetLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(backButton);

        setLayout(new BorderLayout(10, 10));
        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // -------- BUTTON ACTIONS --------

        saveButton.addActionListener(e -> saveBudget());
        viewButton.addActionListener(e -> viewBudget());
        backButton.addActionListener(e -> {
            System.out.println("Back from BudgetGUI");
            dispose();
        });
    }

    private void saveBudget() {
        int month = monthBox.getSelectedIndex() + 1;

        int year;
        double amount;

        try {
            year = Integer.parseInt(yearField.getText().trim());
            amount = Double.parseDouble(amountField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid year and amount",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = budgetDB.upsertBudget(month, year, amount, userID);
        System.out.println("Save budget result: " + success);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Budget saved successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            viewBudget();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save budget",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewBudget() {
        int month = monthBox.getSelectedIndex() + 1;

        int year;
        try {
            year = Integer.parseInt(yearField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid year",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double amount = budgetDB.getBudgetAmount(month, year, userID);

        if (amount != null) {
            currentBudgetLabel.setText("Current Budget: $" + amount);
            System.out.println("Loaded budget: $" + amount);
        } else {
            currentBudgetLabel.setText("Current Budget: Not set");
            System.out.println("No budget set for selected month");
        }
    }

    // ---------- REQUIRED HOMEWORK TEST ----------

    public static void main(String[] args) {
        DBManager.initialize();
        new BudgetGUI();
    }
}
