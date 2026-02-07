package householdbudgetia;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CategoryGUI extends JFrame {

    private JTextField categoryNameField;
    private JList<String> categoryList;
    private DefaultListModel<String> listModel;

    public CategoryGUI() {
        setTitle("Category Management");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);

        System.out.println("CategoryGUI loaded");
    }

    private void initComponents() {

        JLabel titleLabel = new JLabel("Manage Categories");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        categoryNameField = new JTextField(20);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        listModel = new DefaultListModel<>();
        categoryList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(categoryList);

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

        // Button actions will be added next step
    }

    public static void main(String[] args) {
        new CategoryGUI();
    }
}
