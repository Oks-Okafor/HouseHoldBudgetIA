package HouseholdBudgetIA;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/*
 CategoryGUI
 This class provides a graphical interface for managing
 expense categories. Users can add, update, delete, and
 view categories stored in the database.
*/
public class CategoryGUI extends JFrame {

    private JTextField categoryNameField;
    private JList<CategoryItem> categoryList;
    private DefaultListModel<CategoryItem> listModel;

    private CategoryDBAccess categoryDB;

    // TEMP userID for testing (acceptable for homework stage)
    private int userID = 1;

    public CategoryGUI() {
        setTitle("Category Management");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        categoryDB = new CategoryDBAccess();

        initComponents();
        loadCategories();

        setVisible(true);

        System.out.println("CategoryGUI launched");
    }

    private void initComponents() {

        JLabel titleLabel = new JLabel("Manage Categories");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        categoryNameField = new JTextField(20);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        listModel = new DefaultListModel<>();
        categoryList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(categoryList);

        // When a category is selected, load its name into the text field
        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                CategoryItem selected = categoryList.getSelectedValue();
                if (selected != null) {
                    categoryNameField.setText(selected.getCategoryName());
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Category Name:"));
        inputPanel.add(categoryNameField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.PAGE_END);

        // ---------- BUTTON ACTIONS ----------

        addButton.addActionListener(e -> addCategory());

        updateButton.addActionListener(e -> updateCategory());

        deleteButton.addActionListener(e -> deleteCategory());

        backButton.addActionListener(e -> {
            System.out.println("Back button clicked");
            dispose(); // closes window (acceptable for homework)
        });
    }

    // ---------- DATABASE METHODS ----------

    private void loadCategories() {
        listModel.clear();

        List<CategoryItem> categories = categoryDB.getCategoriesForUser(userID);

        for (CategoryItem c : categories) {
            listModel.addElement(c);
        }

        System.out.println("Loaded " + categories.size() + " categories");
    }

    private void addCategory() {
        String name = categoryNameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Category name cannot be empty",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = categoryDB.insertCategory(name, userID);
        System.out.println("Add category result: " + success);

        if (success) {
            categoryNameField.setText("");
            loadCategories();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to add category",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory() {
        CategoryItem selected = categoryList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                    "Select a category to update",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newName = categoryNameField.getText().trim();

        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "New name cannot be empty",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = categoryDB.updateCategory(
                selected.getCategoryID(),
                newName
        );

        System.out.println("Update category result: " + success);

        if (success) {
            loadCategories();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update category",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        CategoryItem selected = categoryList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                    "Select a category to delete",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this category?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean success = categoryDB.deleteCategory(
                selected.getCategoryID()
        );

        System.out.println("Delete category result: " + success);

        if (success) {
            categoryNameField.setText("");
            loadCategories();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to delete category",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- REQUIRED HOMEWORK TEST ----------

    public static void main(String[] args) {
        DBManager.initialize();
        new CategoryGUI();
    }
}
