package IAHouseholdBudget;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class CategoryGUI extends JFrame implements ActionListener
{
    private int currentUserID;
    private CategoryDBAccess categoryDB;

    private JTextField categoryField;
    private JList<String> categoryList;
    private DefaultListModel<String> listModel;

    private JButton addButton;
    private JButton viewButton;
    private JButton deleteButton;
    private JButton backButton;

    public CategoryGUI(int userID)
    {
        super("Household Budget - Categories");

        this.currentUserID = userID;
        this.categoryDB = new CategoryDBAccess();

        this.setSize(700, 550);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Background panel
        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(LoginGUI.LIGHT_BLUE_BG);
        this.setContentPane(background);

        // ================= HEADER =================
        JLabel header = new JLabel("Manage Categories", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setForeground(LoginGUI.DARK_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        background.add(header, BorderLayout.NORTH);

        // ================= CENTER PANEL =================
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        // ----- Top Input Panel -----
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setOpaque(false);

        categoryField = new JTextField(20);

        addButton = createButton("Add");
        viewButton = createButton("View");
        deleteButton = createButton("Delete");

        topPanel.add(new JLabel("Category Name:"));
        topPanel.add(categoryField);
        topPanel.add(addButton);
        topPanel.add(viewButton);
        topPanel.add(deleteButton);

        centerPanel.add(topPanel, BorderLayout.NORTH);

        // ----- Category List -----
        listModel = new DefaultListModel<>();
        categoryList = new JList<>(listModel);
        categoryList.setFont(new Font("SansSerif", Font.PLAIN, 15));
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(categoryList);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        background.add(centerPanel, BorderLayout.CENTER);

        // ================= BOTTOM PANEL =================
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);

        backButton = createButton("Back");
        bottomPanel.add(backButton);

        background.add(bottomPanel, BorderLayout.SOUTH);

        // Load categories immediately
        refreshCategories();
    }

    // ================= BUTTON CREATOR =================
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

    // ================= ACTION LISTENER =================
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("Add"))
        {
            handleAdd();
        }
        else if (command.equals("View"))
        {
            refreshCategories();
        }
        else if (command.equals("Delete"))
        {
            handleDelete();
        }
        else if (command.equals("Back"))
        {
            this.dispose();
            new DashboardGUI(currentUserID).setVisible(true);
        }
    }

    // ================= HANDLE ADD =================
    private void handleAdd()
    {
        String name = categoryField.getText().trim();

        if (name.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.");
            return;
        }

        boolean success = categoryDB.insertCategory(name, currentUserID);

        if (success)
        {
            categoryField.setText("");
            refreshCategories();
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Insert failed.");
        }
    }

    // ================= HANDLE DELETE =================
    private void handleDelete()
    {
        String selected = categoryList.getSelectedValue();

        if (selected == null)
        {
            JOptionPane.showMessageDialog(this, "Select a category first.");
            return;
        }

        int id = Integer.parseInt(selected.split(" - ")[0]);

        categoryDB.deleteCategory(id);
        refreshCategories();
    }

    // ================= REFRESH LIST =================
    private void refreshCategories()
    {
        listModel.clear();

        ArrayList<String[]> categories = categoryDB.getCategoriesByUser(currentUserID);

        for (String[] row : categories)
        {
            String display = row[0] + " - " + row[1];
            listModel.addElement(display);
        }
    }
}
