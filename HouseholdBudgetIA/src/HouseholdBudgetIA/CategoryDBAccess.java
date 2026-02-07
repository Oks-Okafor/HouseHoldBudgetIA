package HouseholdBudgetIA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 CategoryDBAccess
 This class handles all database operations related to
  expense categories (e.g. Groceries, Utilities).
   Each category belongs to a specific user.
 */
public class CategoryDBAccess {

    /*
     insertCategory
         Adds a new category for a given user into the database.
     */
    public boolean insertCategory(String categoryName, int userID) {

        // SQL statement with placeholders to prevent SQL injection
        String sql = "INSERT INTO category (categoryName, userID) VALUES (?, ?)";

        try (
            // Get connection to the IA database
            Connection conn = DBManager.getDBConnection();

            // Prepare SQL statement
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Assign values to the placeholders
            stmt.setString(1, categoryName);
            stmt.setInt(2, userID);

            // Execute the INSERT command
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Insert category failed: " + e.getMessage());
            return false;
        }
    }

    /*
     viewCategories
       Retrieves and displays all categories stored in the database.
     */
    public void viewCategories() {

        String sql = "SELECT * FROM category";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            // Loop through each row in the result set
            while (rs.next()) {
                System.out.println(
                    rs.getInt("categoryID") + " | " +
                    rs.getString("categoryName") + " | User ID: " +
                    rs.getInt("userID")
                );
            }

        } catch (SQLException e) {
            System.out.println("View categories failed: " + e.getMessage());
        }
    }

    /*
     updateCategory
         Updates the name of an existing category using its ID.
     */
    public boolean updateCategory(int categoryID, String newName) {

        String sql = "UPDATE category SET categoryName = ? WHERE categoryID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Set new category name and category ID
            stmt.setString(1, newName);
            stmt.setInt(2, categoryID);

            // Execute UPDATE command
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Update category failed: " + e.getMessage());
            return false;
        }
    }

    /*
     deleteCategory
        Removes a category from the database using its ID.
     */
    public boolean deleteCategory(int categoryID) {

        String sql = "DELETE FROM category WHERE categoryID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // Set the category ID to be deleted
            stmt.setInt(1, categoryID);

            // Execute DELETE command
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Delete category failed: " + e.getMessage());
            return false;
        }
    }
    public List<CategoryItem> getCategoriesForUser(int userID) {

    List<CategoryItem> categories = new ArrayList<>();
    String sql = "SELECT categoryID, categoryName FROM category WHERE userID = ?";

    try (
        Connection conn = DBManager.getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setInt(1, userID);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            categories.add(
                new CategoryItem(
                    rs.getInt("categoryID"),
                    rs.getString("categoryName")
                )
            );
        }

    } catch (SQLException e) {
        System.out.println("Get categories failed: " + e.getMessage());
    }

    return categories;
}
    
    public int getCategoryIDByName(String categoryName, int userID) {

    String sql = "SELECT categoryID FROM category WHERE categoryName = ? AND userID = ?";

    try (
        Connection conn = DBManager.getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setString(1, categoryName);
        stmt.setInt(2, userID);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("categoryID");
        }

    } catch (SQLException e) {
        System.out.println("Get category ID failed: " + e.getMessage());
    }

    return -1; // not found
}


    /*
      main
     
      Used to test all CRUD operations for the category table.
      This method runs on its own from other classes.
     */
    public static void main(String[] args) {

        // Ensure database and tables exist before testing
        DBManager.initialize();

        CategoryDBAccess categoryDB = new CategoryDBAccess();

        // Insert test data
        System.out.println("Inserting categories...");
        categoryDB.insertCategory("Groceries", 1);
        categoryDB.insertCategory("Utilities", 1);

        // View categories
        System.out.println("\nViewing categories:");
        categoryDB.viewCategories();

        // Update a category
        System.out.println("\nUpdating category...");
        categoryDB.updateCategory(1, "Food");

        // View after update
        System.out.println("\nViewing categories after update:");
        categoryDB.viewCategories();

        // Delete a category
        System.out.println("\nDeleting category...");
        categoryDB.deleteCategory(2);

        // Final view
        System.out.println("\nViewing categories after delete:");
        categoryDB.viewCategories();
    }
}
