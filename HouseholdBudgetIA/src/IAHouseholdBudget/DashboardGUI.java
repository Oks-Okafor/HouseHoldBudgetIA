package IAHouseholdBudget;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import javax.swing.*;

public class DashboardGUI extends JFrame implements ActionListener
{

  private int currentUserID;
  private UserDBAccess userDB;
  private BudgetDBAccess budgetDB;
  private ExpenseDBAccess expenseDB;

  public DashboardGUI(int userID)
  {
    super("Household Budget - Dashboard");

    this.currentUserID = userID;

    userDB = new UserDBAccess();
    budgetDB = new BudgetDBAccess();
    expenseDB = new ExpenseDBAccess();

    this.setSize(750, 550);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    JPanel background = new JPanel(new BorderLayout());
    background.setBackground(LoginGUI.LIGHT_BLUE_BG);
    this.setContentPane(background);

    buildHeader(background);
    buildSummaryPanel(background);
    buildNavigationPanel(background);
  }

  private void buildHeader(JPanel background)
  {
    String username = userDB.getUsernameByID(currentUserID);

    JLabel header = new JLabel("Welcome, " + username, SwingConstants.CENTER);
    header.setFont(new Font("SansSerif", Font.BOLD, 28));
    header.setForeground(LoginGUI.DARK_BLUE);
    header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

    background.add(header, BorderLayout.NORTH);
  }

  private void buildSummaryPanel(JPanel background)
  {
    JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 20));
    summaryPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
    summaryPanel.setOpaque(false);

    LocalDate today = LocalDate.now();
    int month = today.getMonthValue();
    int year = today.getYear();

    double budget = budgetDB.getBudgetForMonth(currentUserID, month, year);
    double expenses = expenseDB.getTotalExpensesForMonth(currentUserID, month, year);
    double remaining = budget - expenses;

    summaryPanel.add(createCard("Monthly Budget", budget));
    summaryPanel.add(createCard("Total Expenses", expenses));
    summaryPanel.add(createCard("Remaining Balance", remaining));

    background.add(summaryPanel, BorderLayout.CENTER);
  }

  private JPanel createCard(String title, double amount)
  {
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createLineBorder(LoginGUI.DARK_BLUE, 2));

    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    titleLabel.setForeground(LoginGUI.DARK_BLUE);

    JLabel amountLabel = new JLabel("$" + String.format("%.2f", amount), SwingConstants.CENTER);
    amountLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
    amountLabel.setForeground(LoginGUI.DARK_BLUE);

    card.add(titleLabel, BorderLayout.NORTH);
    card.add(amountLabel, BorderLayout.CENTER);

    return card;
  }

  private void buildNavigationPanel(JPanel background)
  {
    JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
    navPanel.setOpaque(false);

    navPanel.add(createNavButton("Categories"));
    navPanel.add(createNavButton("Budget"));
    navPanel.add(createNavButton("Expenses"));
    navPanel.add(createNavButton("Logout"));

    background.add(navPanel, BorderLayout.SOUTH);
  }

  private JButton createNavButton(String text)
  {
    JButton button = new JButton(text);
    button.setForeground(LoginGUI.DARK_BLUE);
    button.setBackground(new Color(220, 235, 250));
    button.setFont(new Font("SansSerif", Font.BOLD, 15));
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setBorderPainted(true);
    button.setPreferredSize(new Dimension(140, 45));
    button.addActionListener(this);
    return button;
  }

  public void actionPerformed(ActionEvent e)
  {
    String command = e.getActionCommand();

    if (command.equals("Categories"))
    {
      this.dispose();
      new CategoryGUI(currentUserID).setVisible(true);
    }
    else if (command.equals("Budget"))
    {
      this.dispose();
      new BudgetGUI(currentUserID).setVisible(true);
    }
    else if (command.equals("Expenses"))
    {
      this.dispose();
      new ExpenseGUI(currentUserID).setVisible(true);
    }
    else if (command.equals("Logout"))
    {
      this.dispose();
      new LoginGUI().setVisible(true);
    }
  }

  public static void main(String[] args)
  {
    DBManager.initialize();
    new DashboardGUI(1).setVisible(true);
  }
}
