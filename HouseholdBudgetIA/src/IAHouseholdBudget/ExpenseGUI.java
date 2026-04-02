package IAHouseholdBudget;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ExpenseGUI extends JFrame implements ActionListener
{
    private int currentUserID;
    private ExpenseDBAccess expenseDB;
    private CategoryDBAccess categoryDB;

    private JTextField nameField;
    private JTextField amountField;
    private JComboBox<String> categoryBox;

    private DefaultListModel<String> listModel;
    private JList<String> expenseList;

    private ArrayList<String[]> currentExpenses;
    private ArrayList<String[]> currentCategories;

    public ExpenseGUI(int userID)
    {
        super("Expenses");

        this.currentUserID = userID;
        expenseDB = new ExpenseDBAccess();
        categoryDB = new CategoryDBAccess();
        currentExpenses = new ArrayList<String[]>();
        currentCategories = new ArrayList<String[]>();

        this.setSize(750, 750);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(LoginGUI.LIGHT_BLUE_BG);
        this.setContentPane(background);

        JLabel header = new JLabel("Manage Expenses", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setForeground(LoginGUI.DARK_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        background.add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setOpaque(false);

        nameField = new JTextField(12);
        amountField = new JTextField(6);
        categoryBox = new JComboBox<String>();
        loadCategories();

        JButton addBtn = createButton("Add");
        JButton viewBtn = createButton("View");
        JButton deleteBtn = createButton("Delete");
        JButton backBtn = createButton("Back");

        inputPanel.add(new JLabel("Item:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryBox);
        inputPanel.add(addBtn);
        inputPanel.add(viewBtn);
        inputPanel.add(deleteBtn);
        inputPanel.add(backBtn);

        background.add(inputPanel, BorderLayout.SOUTH);

        listModel = new DefaultListModel<String>();
        expenseList = new JList<String>(listModel);
        JScrollPane scrollPane = new JScrollPane(expenseList);
        background.add(scrollPane, BorderLayout.CENTER);

        refreshExpenses();
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

    private void loadCategories()
    {
        categoryBox.removeAllItems();
        currentCategories = categoryDB.getCategoriesByUser(currentUserID);

        for (int i = 0; i < currentCategories.size(); i++)
        {
            String[] row = currentCategories.get(i);
            categoryBox.addItem((i + 1) + ". " + row[1]);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if(cmd.equals("Add"))
        {
            handleAdd();
        }
        else if(cmd.equals("View"))
        {
            viewExpenses();
        }
        else if(cmd.equals("Delete"))
        {
            handleDelete();
        }
        else if(cmd.equals("Back"))
        {
            this.dispose();
            new DashboardGUI(currentUserID).setVisible(true);
        }
    }

    private void handleAdd()
    {
        try
        {
            String note = nameField.getText().trim();

            if (note.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Expense name cannot be empty.");
                return;
            }

            String amountText = amountField.getText().trim();

            if (amountText.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Amount cannot be empty.");
                return;
            }

            double amount = Double.parseDouble(amountText);

            if (amount <= 0)
            {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
                return;
            }

            int selectedIndex = categoryBox.getSelectedIndex();

            if (selectedIndex == -1)
            {
                JOptionPane.showMessageDialog(this, "Select a category first.");
                return;
            }

            int categoryID = Integer.parseInt(currentCategories.get(selectedIndex)[0]);

            boolean success = expenseDB.insertExpense(note, amount, currentUserID, categoryID);

            if (!success)
            {
                JOptionPane.showMessageDialog(this, "Expense could not be added.");
                return;
            }

            java.time.LocalDate today = java.time.LocalDate.now();
            int month = today.getMonthValue();
            int year = today.getYear();

            double budget = new BudgetDBAccess().getBudgetForMonth(currentUserID, month, year);
            double expenses = expenseDB.getTotalExpensesForMonth(currentUserID, month, year);

            if (budget > 0)
            {
                if (Math.abs(expenses - budget) < 0.01)
                {
                    JOptionPane.showMessageDialog(this, "You have reached your budget.");
                }
                else if (expenses > budget)
                {
                    JOptionPane.showMessageDialog(this, "Alert: You have exceeded your budget!");
                }
                else if (expenses >= 0.8 * budget)
                {
                    JOptionPane.showMessageDialog(this, "Warning: You are close to your budget limit!");
                }
            }

            nameField.setText("");
            amountField.setText("");
            refreshExpenses();
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Enter a valid amount.");
        }
    }

    private void handleDelete()
    {
        int selectedIndex = expenseList.getSelectedIndex();

        if(selectedIndex == -1)
        {
            JOptionPane.showMessageDialog(this, "Select an expense first.");
            return;
        }

        int expenseID = Integer.parseInt(currentExpenses.get(selectedIndex)[0]);

        boolean success = expenseDB.deleteExpense(expenseID);

        if (success)
        {
            refreshExpenses();
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Delete failed.");
        }
    }

    private void refreshExpenses()
    {
        listModel.clear();
        currentExpenses = expenseDB.getExpensesByUser(currentUserID);

        for(int i = 0; i < currentExpenses.size(); i++)
        {
            String[] row = currentExpenses.get(i);
            listModel.addElement((i + 1) + ". " + row[1] + " ($" + row[2] + ")");
        }
    }

    public void viewExpenses()
    {
        int selectedIndex = expenseList.getSelectedIndex();

        if (selectedIndex == -1)
        {
            JOptionPane.showMessageDialog(this, "Select an expense first.");
            return;
        }

        int expenseID = Integer.parseInt(currentExpenses.get(selectedIndex)[0]);

        String[] details = expenseDB.getExpenseDetails(expenseID);

        if (details != null)
        {
            ExpenseDetailsGUI detailsGUI = new ExpenseDetailsGUI(
                details[0],
                details[1],
                details[2],
                details[3]
            );

            detailsGUI.setVisible(true);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Could not load expense details.");
        }
    }

    public static void main(String[] args)
    {
        DBManager.initialize();
        new ExpenseGUI(1).setVisible(true);
    }
}