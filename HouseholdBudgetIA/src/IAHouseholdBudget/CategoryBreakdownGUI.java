package IAHouseholdBudget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CategoryBreakdownGUI extends JFrame implements ActionListener
{
    private int userID;
    private int categoryID;
    private String categoryName;

    private JList<String> expenseList;
    private DefaultListModel<String> listModel;
    private JButton backButton;

    public CategoryBreakdownGUI(int userID, int categoryID, String categoryName)
    {
        this.userID = userID;
        this.categoryID = categoryID;
        this.categoryName = categoryName;

        setTitle("Category Breakdown");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(LoginGUI.LIGHT_BLUE_BG);

        JLabel titleLabel = new JLabel("EXPENSES IN CATEGORY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(LoginGUI.DARK_BLUE);
        titleLabel.setBounds(170, 20, 350, 30);
        panel.add(titleLabel);

        JLabel categoryLabel = new JLabel("Category: " + categoryName);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        categoryLabel.setBounds(50, 70, 300, 25);
        panel.add(categoryLabel);

        listModel = new DefaultListModel<>();
        expenseList = new JList<>(listModel);
        expenseList.setFont(new Font("Arial", Font.PLAIN, 20));

        JScrollPane scrollPane = new JScrollPane(expenseList);
        scrollPane.setBounds(50, 110, 580, 220);
        panel.add(scrollPane);

        ExpenseDBAccess expenseDB = new ExpenseDBAccess();
        ArrayList<String> expenses = expenseDB.getExpensesByCategory(userID, categoryID);

        for (String expense : expenses)
        {
            listModel.addElement(expense);
        }

        double total = expenseDB.getTotalForCategory(userID, categoryID);

        JLabel totalLabel = new JLabel("Total in this category: $" + String.format("%.2f", total));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(LoginGUI.DARK_BLUE);
        totalLabel.setBounds(50, 350, 400, 30);
        panel.add(totalLabel);

        backButton = new JButton("BACK");
        backButton.setBounds(280, 400, 120, 35);
        backButton.setBackground(LoginGUI.BUTTON_BLUE);
        backButton.setForeground(LoginGUI.DARK_BLUE);
        backButton.addActionListener(this);
        panel.add(backButton);

        add(panel);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == backButton)
        {
            this.dispose();
            new CategoryGUI(userID).setVisible(true);
        }
    }
}