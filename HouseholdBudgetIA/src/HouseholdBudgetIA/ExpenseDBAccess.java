package HouseholdBudgetIA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 ExpenseDBAccess
 Handles database operations related to expenses.
*/
public class ExpenseDBAccess {

    // INSERT expense
    public boolean insertExpense(double amount, int categoryID, int month, int year, String note, int userID) {

        String sql = "INSERT INTO expense (amount, categoryID, month, year, note, userID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, categoryID);
            stmt.setInt(3, month);
            stmt.setInt(4, year);
            stmt.setString(5, note);
            stmt.setInt(6, userID);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET expenses for month/year
    public List<Double> getExpensesForMonth(int month, int year, int userID) {

        List<Double> expenses = new ArrayList<>();
        String sql = "SELECT amount FROM expense WHERE month = ? AND year = ? AND userID = ?";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, month);
            stmt.setInt(2, year);
            stmt.setInt(3, userID);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                expenses.add(rs.getDouble("amount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    // TEST
    public static void main(String[] args) {
        DBManager.initialize();
        ExpenseDBAccess db = new ExpenseDBAccess();

        db.insertExpense(25.50, 1, 9, 2025, "Lunch", 1);
        System.out.println(db.getExpensesForMonth(9, 2025, 1));
    }
}
