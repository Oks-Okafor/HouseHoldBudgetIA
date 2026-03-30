package IAHouseholdBudget;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

public class ExpenseGUI extends JFrame implements ActionListener
{
    private int currentUserID;
    private ExpenseDBAccess expenseDB;
    private CategoryDBAccess categoryDB;

    private JTextField nameField;
    private JTextField amountField;
    private JTextField dateField;
    private JComboBox<String> categoryBox;

    private DefaultListModel<String> listModel;
    private JList<String> expenseList;

    
    public ExpenseGUI(int userID)
    {
        super("Expenses");

        this.currentUserID = userID;
        expenseDB = new ExpenseDBAccess();
        categoryDB = new CategoryDBAccess();

        this.setSize(750, 750);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(LoginGUI.LIGHT_BLUE_BG);
        this.setContentPane(background);

        // Header
        JLabel header = new JLabel("Manage Expenses", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setForeground(LoginGUI.DARK_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        background.add(header, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setOpaque(false);

        nameField = new JTextField(12);
        amountField = new JTextField(6);
dateField = new JTextField(6);
        categoryBox = new JComboBox<>();
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
        inputPanel.add(new JLabel("Date:"));
         inputPanel.add(dateField);
        inputPanel.add(categoryBox);
        inputPanel.add(addBtn);
        inputPanel.add(viewBtn);
        inputPanel.add(deleteBtn);
        inputPanel.add(backBtn);

        background.add(inputPanel, BorderLayout.SOUTH);

        // List
        listModel = new DefaultListModel<>();
        expenseList = new JList<>(listModel);
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
        ArrayList<String[]> categories = categoryDB.getCategoriesByUser(currentUserID);

        for(String[] row : categories)
        {
            categoryBox.addItem(row[0] + " - " + row[1]);
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
        String name = nameField.getText();
        double amount = Double.parseDouble(amountField.getText());

        String selected = (String) categoryBox.getSelectedItem();
        int categoryID = Integer.parseInt(selected.split(" - ")[0]);

        expenseDB.insertExpense(name, amount, currentUserID, categoryID);

        nameField.setText("");
        amountField.setText("");
        refreshExpenses();
    }

    private void handleDelete()
    {
        String selected = expenseList.getSelectedValue();

        if(selected == null) return;

        int id = Integer.parseInt(selected.split(" - ")[0]);

        expenseDB.deleteExpense(id);
        refreshExpenses();
    }

    private void refreshExpenses()
    {
        listModel.clear();

        ArrayList<String[]> expenses = expenseDB.getExpensesByUser(currentUserID);

        for(String[] row : expenses)
        {
            listModel.addElement(row[0] + " - " + row[1] + " ($" + row[2] + ")");
        }
    }
public void viewExpenses()
{
    String selected = expenseList.getSelectedValue(); 
    // get the selected item from the list

    if (selected == null)
    {
        JOptionPane.showMessageDialog(this, "Select an expense first."); 
        // show message if nothing is selected
        return; 
        // stop method
    }

    int expenseID = Integer.parseInt(selected.split(" - ")[0]); 
    // extract expense ID from selected list item

    String[] details = expenseDB.getExpenseDetails(expenseID); 
    // get full expense details from database

    if (details != null)
    {
        ExpenseDetailsGUI detailsGUI = new ExpenseDetailsGUI(
            details[0], // expense name
            details[1], // expense date
            details[2], // expense amount
            details[3]  // category name
        );

        detailsGUI.setVisible(true); 
        // show the popup window
    }
    else
    {
        JOptionPane.showMessageDialog(this, "Could not load expense details."); 
        // show error if expense was not found
    }
}
 public static void main(String[] args)
  {
    DBManager.initialize();
    new ExpenseGUI(1).setVisible(true);
  }
}

  
