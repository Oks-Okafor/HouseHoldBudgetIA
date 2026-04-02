package IAHouseholdBudget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDBAccess
{
    public boolean insertCategory(String categoryName, int userID)
    {
        String sql = "INSERT INTO category (categoryName, userID) VALUES (?, ?)";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setString(1, categoryName);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Insert category failed: " + e.getMessage());
            return false;
        }
    }

    public void viewCategories()
    {
        String sql = "SELECT * FROM category";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        )
        {
            while (rs.next())
            {
                System.out.println(
                    rs.getInt("categoryID") + " | " +
                    rs.getString("categoryName") + " | User ID: " +
                    rs.getInt("userID")
                );
            }
        }
        catch (SQLException e)
        {
            System.out.println("View categories failed: " + e.getMessage());
        }
    }

    public boolean updateCategory(int categoryID, String newName)
    {
        String sql = "UPDATE category SET categoryName = ? WHERE categoryID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setString(1, newName);
            stmt.setInt(2, categoryID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Update category failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCategory(int categoryID)
    {
        String sql = "DELETE FROM category WHERE categoryID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, categoryID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Delete category failed: " + e.getMessage());
            return false;
        }
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
        CategoryDBAccess categoryDB = new CategoryDBAccess();
        categoryDB.viewCategories();
    }
}