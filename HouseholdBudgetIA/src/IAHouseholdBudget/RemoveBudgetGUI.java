package IAHouseholdBudget;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class RemoveBudgetGUI extends JFrame implements ActionListener
{
    private int currentUserID;
    private JTextField amountField;
    private JButton backButton;
    private JButton saveButton;
    private BudgetDBAccess budgetDB;

    public RemoveBudgetGUI(int userID)
    {
        super("Add Budget");

        this.currentUserID = userID;
        this.budgetDB = new BudgetDBAccess();

        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(LoginGUI.LIGHT_BLUE_BG);
        this.setContentPane(background);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(360, 170));
        card.setBackground(new Color(200, 235, 250));
        card.setBorder(BorderFactory.createLineBorder(LoginGUI.DARK_BLUE, 2));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel label = new JLabel("Amount to be Added");
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(LoginGUI.DARK_BLUE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(label, gbc);

        amountField = new JTextField(16);
        amountField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        amountField.setPreferredSize(new Dimension(180, 30));

        gbc.gridy = 1;
        card.add(amountField, gbc);

        backButton = createButton("BACK");
        saveButton = createButton("SAVE");

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        card.add(backButton, gbc);

        gbc.gridx = 1;
        card.add(saveButton, gbc);

        background.add(card);
    }

    private JButton createButton(String text)
    {
        JButton button = new JButton(text);
        button.setForeground(LoginGUI.DARK_BLUE);
        button.setBackground(new Color(120, 190, 235));
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.addActionListener(this);
        return button;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("SAVE"))
        {
            handleSave();
        }
        else if (command.equals("BACK"))
        {
            this.dispose();
            new AdjustChoiceGUI(currentUserID).setVisible(true);
        }
    }

    private void handleSave()
    {
        try
        {
            String input = amountField.getText().trim();

            if (input.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Enter an amount.");
                return;
            }

            double amountToAdd = Double.parseDouble(input);

            if (amountToAdd <= 0)
            {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.");
                return;
            }

            LocalDate today = LocalDate.now();
            int month = today.getMonthValue();
            int year = today.getYear();

            double currentBudget = budgetDB.getBudgetForMonth(currentUserID, month, year);
            double newBudget = currentBudget - amountToAdd;

            boolean success = budgetDB.upsertBudget(currentUserID, month, year, newBudget);

            if (success)
            {
                JOptionPane.showMessageDialog(this, "Budget aremoved successfully.");
                this.dispose();
                new DashboardGUI(currentUserID).setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Failed to update budget.");
            }
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Enter a valid number.");
        }
    }
}