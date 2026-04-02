package IAHouseholdBudget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExpenseDBAccess
{
    public void viewExpenses()
    {
        String sql = "SELECT * FROM expense";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        )
        {
            while (rs.next())
            {
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
        }
        catch (SQLException e)
        {
            System.out.println("View expenses failed: " + e.getMessage());
        }
    }

    public boolean updateExpenseAmount(int expenseID, double newAmount)
    {
        String sql = "UPDATE expense SET amount = ? WHERE expenseID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setDouble(1, newAmount);
            stmt.setInt(2, expenseID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Update expense failed: " + e.getMessage());
            return false;
        }
    }

    public double getTotalExpensesForMonth(int userID, int month, int year)
    {
        String sql = "SELECT SUM(amount) AS total FROM expense WHERE userID = ? AND month = ? AND year = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, userID);
            stmt.setInt(2, month);
            stmt.setInt(3, year);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return rs.getDouble("total");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Get expenses failed: " + e.getMessage());
        }

        return 0;
    }

    public boolean insertExpense(String note, double amount, int userID, int categoryID)
    {
        String sql = "INSERT INTO expense (note, amount, expenseDate, month, year, userID, categoryID) " +
                     "VALUES (?, ?, CURDATE(), MONTH(CURDATE()), YEAR(CURDATE()), ?, ?)";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setString(1, note);
            stmt.setDouble(2, amount);
            stmt.setInt(3, userID);
            stmt.setInt(4, categoryID);

            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Insert expense failed: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String[]> getExpensesByUser(int userID)
    {
        ArrayList<String[]> list = new ArrayList<>();

        String sql = "SELECT expenseID, note, amount FROM expense WHERE userID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                String[] row =
                {
                    String.valueOf(rs.getInt("expenseID")),
                    rs.getString("note"),
                    String.valueOf(rs.getDouble("amount"))
                };

                list.add(row);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Get expenses failed: " + e.getMessage());
        }

        return list;
    }

    public boolean deleteExpense(int expenseID)
    {
        String sql = "DELETE FROM expense WHERE expenseID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, expenseID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Delete expense failed: " + e.getMessage());
            return false;
        }
    }

    public String[] getExpenseDetails(int expenseID)
    {
        String sql = "SELECT e.note, e.expenseDate, e.amount, c.categoryName " +
                     "FROM expense e " +
                     "JOIN category c ON e.categoryID = c.categoryID " +
                     "WHERE e.expenseID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, expenseID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                String[] details =
                {
                    rs.getString("note"),
                    rs.getString("expenseDate"),
                    String.valueOf(rs.getDouble("amount")),
                    rs.getString("categoryName")
                };

                return details;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Get expense details failed: " + e.getMessage());
        }

        return null;
    }

    public ArrayList<String> getExpensesByCategory(int userID, int categoryID)
    {
        ArrayList<String> expenses = new ArrayList<>();

        String sql = "SELECT note, amount, expenseDate FROM expense WHERE userID = ? AND categoryID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, userID);
            stmt.setInt(2, categoryID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                String name = rs.getString("note");
                double amount = rs.getDouble("amount");
                String date = rs.getString("expenseDate");

                expenses.add(name + " - $" + amount + " - " + date);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Get expenses by category failed: " + e.getMessage());
        }

        return expenses;
    }

    public double getTotalForCategory(int userID, int categoryID)
    {
        double total = 0;

        String sql = "SELECT SUM(amount) AS total FROM expense WHERE userID = ? AND categoryID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, userID);
            stmt.setInt(2, categoryID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                total = rs.getDouble("total");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Get category total failed: " + e.getMessage());
        }

        return total;
    }

    public static void main(String[] args)
    {
        DBManager.initialize();
        ExpenseDBAccess expenseDB = new ExpenseDBAccess();
        expenseDB.viewExpenses();
    }
}