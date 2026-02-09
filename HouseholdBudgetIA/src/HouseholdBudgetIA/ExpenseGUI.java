package HouseholdBudgetIA;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/*
 ExpenseGUI
 Allows the user to add expenses linked to categories.
*/
public class ExpenseGUI extends JFrame {

    private JTextField amountField;
    private JTextField noteField;
    private JComboBox<CategoryItem> categoryBox;
    private JComboBox<String> monthBox;
    private JTextField yearField;

    private ExpenseDBAccess expenseDB;
    private CategoryDBAccess categoryDB;

    private int userID = 1; // TEMP for IA testing

    public ExpenseGUI() {
        setTitle("Add Expense");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        expenseDB = new ExpenseDBAccess();
        categoryDB = new CategoryDBAccess();

        initComponents();
        loadCategories();
        setVisible(true);

        System.out.println("ExpenseGUI launched");
    }

    private void initComponents() {

        JLabel title = new JLabel("Add Expense");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        amountField = new JTextField(10);
        noteField = new JTextField(15);

        monthBox = new JComboBox<>(new String[]{
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        });

        yearField = new JTextField("2025", 6);

        categoryBox = new JComboBox<>();

        JButton addButton = new JButton("Add Expense");
        JButton backButton = new JButton("Back");

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 8, 8));
        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryBox);
        formPanel.add(new JLabel("Month:"));
        formPanel.add(monthBox);
        formPanel.add(new JLabel("Year:"));
        formPanel.add(yearField);
        formPanel.add(new JLabel("Note:"));
        formPanel.add(noteField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        setLayout(new BorderLayout(10, 10));
        add(title, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // BUTTON ACTIONS
        addButton.addActionListener(e -> addExpense());
        backButton.addActionListener(e -> dispose());
    }

    private void loadCategories() {
        List<CategoryItem> categories = categoryDB.getCategoriesForUser(userID);
        categoryBox.removeAllItems();

        for (CategoryItem c : categories) {
            categoryBox.addItem(c);
        }

        System.out.println("Loaded categories for ExpenseGUI");
    }

    private void addExpense() {

        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            int month = monthBox.getSelectedIndex() + 1;
            int year = Integer.parseInt(yearField.getText().trim());
            String note = noteField.getText().trim();

            CategoryItem selectedCategory = (CategoryItem) categoryBox.getSelectedItem();
            if (selectedCategory == null) {
                JOptionPane.showMessageDialog(this, "Select a category");
                return;
            }

            boolean success = expenseDB.insertExpense(
                    amount,
                    selectedCategory.getCategoryID(),
                    month,
                    year,
                    note,
                    userID
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "Expense added");
                amountField.setText("");
                noteField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add expense");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount or year");
        }
    }

    public static void main(String[] args) {
        DBManager.initialize();
        new ExpenseGUI();
    }
}
