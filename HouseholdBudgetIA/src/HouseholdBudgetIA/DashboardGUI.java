package HouseholdBudgetIA;

import javax.swing.*;
import java.awt.*;

/*
 DashboardGUI
 Central navigation screen to access all parts of the app.
 */
public class DashboardGUI extends JFrame
{

  private int userID; // store logged-in user

  public DashboardGUI(int userID)
  {
    this.userID = userID;

    setTitle("Dashboard");
    setSize(420, 420);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    initComponents();
    setVisible(true);

    System.out.println("DashboardGUI launched for userID=" + userID);
  }

  private void initComponents()
  {

    JLabel title = new JLabel("Household Budget Dashboard");
    title.setFont(new Font("Arial", Font.BOLD, 18));
    title.setHorizontalAlignment(SwingConstants.CENTER);

    JButton loginBtn = new JButton("Login");
    JButton signupBtn = new JButton("Signup");
    JButton categoriesBtn = new JButton("Categories");
    JButton budgetBtn = new JButton("Budget");
    JButton expensesBtn = new JButton("Expenses");
    JButton summaryBtn = new JButton("Summary");
    JButton logoutBtn = new JButton("Logout");

    JPanel grid = new JPanel(new GridLayout(7, 1, 10, 10));
    grid.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    grid.add(loginBtn);
    grid.add(signupBtn);
    grid.add(categoriesBtn);
    grid.add(budgetBtn);
    grid.add(expensesBtn);
    grid.add(summaryBtn);
    grid.add(logoutBtn);

    setLayout(new BorderLayout(10, 10));
    add(title, BorderLayout.NORTH);
    add(grid, BorderLayout.CENTER);

    // ---- Button actions ----
    loginBtn.addActionListener(e ->
    {
      dispose();
      new LoginGUI();
    });

    signupBtn.addActionListener(e ->
    {
      dispose();
      new SignupGUI();
    });

    categoriesBtn.addActionListener(e ->
    {
      // If your CategoryGUI currently uses a fixed userID=1, that's okay for now.
      // Later we can upgrade it to CategoryGUI(userID).
      new CategoryGUI();
    });

    budgetBtn.addActionListener(e ->
    {
      new BudgetGUI();
    });

    expensesBtn.addActionListener(e ->
    {
      new ExpenseGUI();
    });

    summaryBtn.addActionListener(e ->
    {
      // This will work once you create SummaryGUI.
      // For now it shows a message instead of crashing.
      dispose();
     new SummaryGUI();
    });

    logoutBtn.addActionListener(e ->
    {
      System.out.println("Logout clicked");
      dispose();
      new LoginGUI();
    });
  }

  // Homework-required standalone test
  public static void main(String[] args)
  {
    DBManager.initialize();
    new DashboardGUI(1); // test userID = 1
  }
}
