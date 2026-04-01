package IAHouseholdBudget;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AdjustChoiceGUI extends JFrame implements ActionListener
{
    private int currentUserID;

    private JButton addButton;
    private JButton removeButton;
    private JButton backButton;

    public AdjustChoiceGUI(int userID)
    {
        super("Adjust Budget");

        this.currentUserID = userID;

        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(LoginGUI.LIGHT_BLUE_BG);
        this.setContentPane(background);

        JLabel header = new JLabel("Adjust Budget", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(LoginGUI.DARK_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        background.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 40));
        centerPanel.setOpaque(false);

        addButton = createButton("Add");
        removeButton = createButton("Remove");
        backButton = createButton("Back");

        centerPanel.add(addButton);
        centerPanel.add(removeButton);
        centerPanel.add(backButton);

        background.add(centerPanel, BorderLayout.CENTER);
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
        button.setPreferredSize(new Dimension(130, 40));
        button.addActionListener(this);
        return button;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("Add"))
        {
            this.dispose();
            new AddBudgetGUI(currentUserID).setVisible(true);
        }
        else if (command.equals("Remove"))
        {
            this.dispose();
            new RemoveBudgetGUI(currentUserID).setVisible(true);
        }
        else if (command.equals("Back"))
        {
            this.dispose();
            new BudgetGUI(currentUserID).setVisible(true);
        }
    }
}