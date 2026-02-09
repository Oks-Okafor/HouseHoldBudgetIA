package HouseholdBudgetIA;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/*
 SummaryGUI
 Displays a summary of expenses compared to the monthly budget.
*/
public class SummaryGUI extends JFrame {

    private JComboBox<String> monthBox;
    private JTextField yearField;

    private JLabel budgetLabel;
    private JLabel expenseLabel;
    private JLabel statusLabel;

    private BudgetDBAccess budgetDB;
    private ExpenseDBAccess expenseDB;

    private int userID = 1; // TEMP for IA testing

    public SummaryGUI() {
        setTitle("Monthly Summary");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        budgetDB = new BudgetDBAccess();
        expenseDB = new ExpenseDBAccess();

        initComponents();
        setVisible(true);

        System.out.println("SummaryGUI launched");
    }

    private void initComponents() {

        JLabel title = new JLabel("Monthly Expense Summary");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        monthBox = new JComboBox<>(new String[]{
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        });

        yearField = new JTextField("2025", 6);

        budgetLabel = new JLabel("Budget: -");
        expenseLabel = new JLabel("Total Expenses: -");
        statusLabel = new JLabel("Status: -");

        JButton viewButton = new JButton("View Summary");
        JButton backButton = new JButton("Back");

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        inputPanel.add(new JLabel("Month:"));
        inputPanel.add(monthBox);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);

        JPanel resultPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        resultPanel.add(budgetLabel);
        resultPanel.add(expenseLabel);
        resultPanel.add(statusLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);
        buttonPanel.add(backButton);

        setLayout(new BorderLayout(10, 10));
        add(title, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.PAGE_END);

        // BUTTON ACTIONS
        viewButton.addActionListener(e -> loadSummary());
        backButton.addActionListener(e -> dispose());
    }

    private void loadSummary() {

        int month = monthBox.getSelectedIndex() + 1;

        int year;
        try {
            year = Integer.parseInt(yearField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid year");
            return;
        }

        // Get budget
        Double budget = budgetDB.getBudgetAmount(month, year, userID);
        if (budget == null) {
            budget = 0.0;
        }

        // Get expenses
        List<Double> expenses = expenseDB.getExpensesForMonth(month, year, userID);

        double totalExpenses = 0.0;
        for (double amt : expenses) {
            totalExpenses += amt;
        }

        // Update labels
        budgetLabel.setText("Budget: $" + budget);
        expenseLabel.setText("Total Expenses: $" + totalExpenses);

        double difference = budget - totalExpenses;

        if (difference > 0) {
            statusLabel.setText("Status: $" + difference + " remaining");
        } else if (difference < 0) {
            statusLabel.setText("Status: Over budget by $" + Math.abs(difference));
        } else {
            statusLabel.setText("Status: Exactly on budget");
        }

        System.out.println("Summary calculated for " + month + "/" + year);
    }

    // REQUIRED HOMEWORK TEST
    public static void main(String[] args) {
        DBManager.initialize();
        new SummaryGUI();
    }
}

