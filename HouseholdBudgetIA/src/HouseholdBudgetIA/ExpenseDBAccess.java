package HouseholdBudgetIA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 ExpenseDBAccess
 This class handles all database operations related to
 expenses recorded by a user.
 Each expense belongs to a user and a category.
 */
public class ExpenseDBAccess {

    /*
     insertExpense
         Adds a new expense record into the database.
     */
    public boolean insertExpense(double amount, String expenseDate, String note,
                                 int month, int year, int userID, int categoryID) {

        // SQL statement to insert a new expense
        String sql = "INSERT INTO expense " +
                     "(amount, expenseDate, note, month, year, userID, categoryID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
            // Connect to the IA database
            Connection conn = DBManager.getDBConnection();

            // Prepare SQL statement
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Assign values to the placeholders
            stmt.setDouble(1, amount);
            stmt.setString(2, expenseDate); // format: YYYY-MM-DD
            stmt.setString(3, note);
            stmt.setInt(4, month);
            stmt.setInt(5, year);
            stmt.setInt(6, userID);
            stmt.setInt(7, categoryID);

            // Execute INSERT command
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Insert expense failed: " + e.getMessage());
            return false;
        }
    }

    /*
     viewExpenses
         Retrieves and displays all expense records from the database.
     */
    public void viewExpenses() {

        String sql = "SELECT * FROM expense";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            // Loop through each row in the result set
            while (rs.next()) {
                System.out.println(
                    rs.getInt("expenseID") + " | " +
                    "Amount: " + rs.getDouble("amount") + " | " +
                    "Date: " + rs.getDate("expenseDate") + " | " +
                    "Note: " + rs.getString("note") + " | " +
                    "Month: " + rs.getInt("month") + " | " +
                    "Year: " + rs.getInt("year") + " | " +
                    "User ID: " + rs.getInt("userID") + " | " +
                    "Category ID: " + rs.getInt("categoryID")
                );
            }

        } catch (SQLException e) {
            System.out.println("View expenses failed: " + e.getMessage());
        }
    }

    /*
     updateExpenseAmount
         Updates the amount of an existing expense using its ID.
     */
    public boolean updateExpenseAmount(int expenseID, double newAmount) {

        String sql = "UPDATE expense SET amount = ? WHERE expenseID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Set new expense amount and expense ID
            stmt.setDouble(1, newAmount);
            stmt.setInt(2, expenseID);

            // Execute UPDATE command
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Update expense failed: " + e.getMessage());
            return false;
        }
    }

    /*
     deleteExpense
         Removes an expense record from the database using its ID.
     */
    public boolean deleteExpense(int expenseID) {

        String sql = "DELETE FROM expense WHERE expenseID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Set the expense ID to be deleted
            stmt.setInt(1, expenseID);

            // Execute DELETE command
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Delete expense failed: " + e.getMessage());
            return false;
        }
    }

    /*
      main

      Used to test all CRUD operations for the expense table.
      This method runs on its own from other classes.
     */
    public static void main(String[] args) {

        // Ensure database and tables exist before testing
        DBManager.initialize();

        ExpenseDBAccess expenseDB = new ExpenseDBAccess();

        // Insert test data
        System.out.println("Inserting expenses...");
        expenseDB.insertExpense(45.50, "2025-09-10", "Groceries at store", 9, 2025, 1, 1);
        expenseDB.insertExpense(120.00, "2025-09-12", "Electric bill", 9, 2025, 1, 1);

        // View expenses
        System.out.println("\nViewing expenses:");
        expenseDB.viewExpenses();

        // Update an expense amount
        System.out.println("\nUpdating expense...");
        expenseDB.updateExpenseAmount(1, 50.00);

        // View after update
        System.out.println("\nViewing expenses after update:");
        expenseDB.viewExpenses();

        // Delete an expense
        System.out.println("\nDeleting expense...");
        expenseDB.deleteExpense(2);

        // Final view
        System.out.println("\nViewing expenses after delete:");
        expenseDB.viewExpenses();
    }
}