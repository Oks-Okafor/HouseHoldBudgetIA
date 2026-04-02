package IAHouseholdBudget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BudgetDBAccess
{
    public boolean insertBudget(int month, int year, double amount, int userID)
    {
        String sql = "INSERT INTO budget (month, year, amount, userID) VALUES (?, ?, ?, ?)";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            stmt.setDouble(3, amount);
            stmt.setInt(4, userID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Insert budget failed: " + e.getMessage());
            return false;
        }
    }

    public void viewBudgets()
    {
        String sql = "SELECT * FROM budget";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        )
        {
            while (rs.next())
            {
                System.out.println(
                    rs.getInt("budgetID") + " | " +
                    "Month: " + rs.getInt("month") + " | " +
                    "Year: " + rs.getInt("year") + " | " +
                    "Amount: " + rs.getDouble("amount") + " | " +
                    "User ID: " + rs.getInt("userID")
                );
            }
        }
        catch (SQLException e)
        {
            System.out.println("View budgets failed: " + e.getMessage());
        }
    }

    public boolean updateBudgetAmount(int budgetID, double newAmount)
    {
        String sql = "UPDATE budget SET amount = ? WHERE budgetID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setDouble(1, newAmount);
            stmt.setInt(2, budgetID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Update budget failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBudget(int budgetID)
    {
        String sql = "DELETE FROM budget WHERE budgetID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, budgetID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Delete budget failed: " + e.getMessage());
            return false;
        }
    }

    public double getBudgetForMonth(int userID, int month, int year)
    {
        String sql = "SELECT amount FROM budget WHERE userID = ? AND month = ? AND year = ?";

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
                return rs.getDouble("amount");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Get budget failed: " + e.getMessage());
        }

        return 0;
    }

    public boolean upsertBudget(int userID, int month, int year, double amount)
    {
        String checkSQL = "SELECT budgetID FROM budget WHERE userID = ? AND month = ? AND year = ?";
        String insertSQL = "INSERT INTO budget (userID, month, year, amount) VALUES (?, ?, ?, ?)";
        String updateSQL = "UPDATE budget SET amount = ? WHERE userID = ? AND month = ? AND year = ?";

        try (Connection conn = DBManager.getDBConnection())
        {
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setInt(1, userID);
            checkStmt.setInt(2, month);
            checkStmt.setInt(3, year);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next())
            {
                PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, userID);
                updateStmt.setInt(3, month);
                updateStmt.setInt(4, year);
                updateStmt.executeUpdate();
            }
            else
            {
                PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                insertStmt.setInt(1, userID);
                insertStmt.setInt(2, month);
                insertStmt.setInt(3, year);
                insertStmt.setDouble(4, amount);
                insertStmt.executeUpdate();
            }

            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Budget upsert failed: " + e.getMessage());
            return false;
        }
    }

    public double getBudgetAmount(int userID, int month, int year)
    {
        String sql = "SELECT amount FROM budget WHERE userID = ? AND month = ? AND year = ?";

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
                return rs.getDouble("amount");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Get budget failed: " + e.getMessage());
        }

        return 0;
    }

    public ArrayList<String[]> getCategoriesByUser(int userID)
    {
        ArrayList<String[]> list = new ArrayList<>();

        String sql = "SELECT categoryID, categoryName FROM category WHERE userID = ?";

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
                    String.valueOf(rs.getInt("categoryID")),
                    rs.getString("categoryName")
                };

                list.add(row);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Get categories failed: " + e.getMessage());
        }

        return list;
    }

    public static void main(String[] args)
    {
        DBManager.initialize();
        BudgetDBAccess budgetDB = new BudgetDBAccess();
        budgetDB.viewBudgets();
    }
}