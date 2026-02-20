package IAHouseholdBudget;

import HouseholdBudgetIA.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 BudgetDBAccess
 This class handles all database operations related to
 monthly budgets set by a user.
 Each budget is identified by month and year.
 */
public class BudgetDBAccess {

    /*
     insertBudget
         Adds a new monthly budget for a user into the database.
     */
    public boolean insertBudget(int month, int year, double amount, int userID) {

        // SQL statement to insert a new budget record
        String sql = "INSERT INTO budget (month, year, amount, userID) VALUES (?, ?, ?, ?)";

        try (
            // Connect to the IA database
            Connection conn = DBManager.getDBConnection();

            // Prepare SQL statement
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Assign values to the placeholders
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            stmt.setDouble(3, amount);
            stmt.setInt(4, userID);

            // Execute INSERT command
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Insert budget failed: " + e.getMessage());
            return false;
        }
    }

    /*
     viewBudgets
         Retrieves and displays all budget records from the database.
     */
    public void viewBudgets() {

        String sql = "SELECT * FROM budget";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            // Loop through each row in the result set
            while (rs.next()) {
                System.out.println(
                    rs.getInt("budgetID") + " | " +
                    "Month: " + rs.getInt("month") + " | " +
                    "Year: " + rs.getInt("year") + " | " +
                    "Amount: " + rs.getDouble("amount") + " | " +
                    "User ID: " + rs.getInt("userID")
                );
            }

        } catch (SQLException e) {
            System.out.println("View budgets failed: " + e.getMessage());
        }
    }

    /*
     updateBudgetAmount
         Updates the amount of an existing budget using its ID.
     */
    public boolean updateBudgetAmount(int budgetID, double newAmount) {

        String sql = "UPDATE budget SET amount = ? WHERE budgetID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Set new budget amount and budget ID
            stmt.setDouble(1, newAmount);
            stmt.setInt(2, budgetID);

            // Execute UPDATE command
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Update budget failed: " + e.getMessage());
            return false;
        }
    }

    /*
     deleteBudget
         Removes a budget record from the database using its ID.
     */
    public boolean deleteBudget(int budgetID) {

        String sql = "DELETE FROM budget WHERE budgetID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Set the budget ID to be deleted
            stmt.setInt(1, budgetID);

            // Execute DELETE command
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Delete budget failed: " + e.getMessage());
            return false;
        }
    }
public double getBudgetForMonth(int userID, int month, int year)
{
    String sql = "SELECT amount FROM budget WHERE userID = ? AND month = ? AND year = ?";

    try (Connection conn = DBManager.getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(sql))
    {
        stmt.setInt(1, userID);
        stmt.setInt(2, month);
        stmt.setInt(3, year);

        ResultSet rs = stmt.executeQuery();

        if (rs.next())
        {
            return rs.getDouble("amount");
        }

    } catch (SQLException e)
    {
        System.out.println("Get budget failed: " + e.getMessage());
    }

    return 0;
}
public ArrayList<String[]> getCategoriesByUser(int userID)
{
    ArrayList<String[]> list = new ArrayList<>();

    String sql = "SELECT categoryID, categoryName FROM category WHERE userID = ?";

    try (Connection conn = DBManager.getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(sql))
    {
        stmt.setInt(1, userID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next())
        {
            String[] row = {
                String.valueOf(rs.getInt("categoryID")),
                rs.getString("categoryName")
            };

            list.add(row);
        }

    } catch (SQLException e)
    {
        System.out.println("Get categories failed: " + e.getMessage());
    }

    return list;
}

    /*
      main

      Used to test all CRUD operations for the budget table.
      This method runs on its own from other classes.
     */
    public static void main(String[] args) {

        // Ensure database and tables exist before testing
        DBManager.initialize();

        BudgetDBAccess budgetDB = new BudgetDBAccess();

        // Insert test data
        System.out.println("Inserting budgets...");
        budgetDB.insertBudget(9, 2025, 1000.00, 1);
        budgetDB.insertBudget(10, 2025, 1200.00, 1);

        // View budgets
        System.out.println("\nViewing budgets:");
        budgetDB.viewBudgets();

        // Update a budget amount
        System.out.println("\nUpdating budget...");
        budgetDB.updateBudgetAmount(1, 1500.00);

        // View after update
        System.out.println("\nViewing budgets after update:");
        budgetDB.viewBudgets();

        // Delete a budget
        System.out.println("\nDeleting budget...");
        budgetDB.deleteBudget(2);

        // Final view
        System.out.println("\nViewing budgets after delete:");
        budgetDB.viewBudgets();
    }
}

