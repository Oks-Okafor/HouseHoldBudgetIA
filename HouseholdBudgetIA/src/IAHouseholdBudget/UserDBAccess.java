package IAHouseholdBudget;

import HouseholdBudgetIA.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * UserDBAccess
 * Handles database operations for the users table.
 */
public class UserDBAccess {

    // Insert a new user into the database


    // View all users in the database
    public void viewUsers() {
        String sql = "SELECT * FROM users";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                    rs.getInt("userID") + " | " +
                    rs.getString("username") + " | " +
                    rs.getString("email")
                );
            }

        } catch (SQLException e) {
            System.out.println("View users failed: " + e.getMessage());
        }
    }

    // Update a user's email
    public boolean updateUserEmail(int userID, String newEmail) {
        String sql = "UPDATE users SET email = ? WHERE userID = ?";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newEmail);
            stmt.setInt(2, userID);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Update user failed: " + e.getMessage());
            return false;
        }
    }

    // Delete a user by ID
    public boolean deleteUser(int userID) {
        String sql = "DELETE FROM users WHERE userID = ?";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Delete user failed: " + e.getMessage());
            return false;
        }
    }
// Login method for validating user credentials
public int loginUser(String username, String password)
{
    String sql = "SELECT userID FROM users WHERE username = ? AND password = ?";

    try (Connection conn = DBManager.getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(sql))
    {
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        if (rs.next())
        {
            return rs.getInt("userID");
        }

    } catch (SQLException e)
    {
        System.out.println("Login failed: " + e.getMessage());
    }

    return 0; // login failed
}
public boolean insertUser(String username, String password, String email)
{
    String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

    try (Connection conn = DBManager.getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(sql))
    {
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setString(3, email);

        stmt.executeUpdate();
        return true;

    } catch (SQLException e)
    {
        System.out.println("Insert user failed: " + e.getMessage());
        return false;
    }
}
public String getUsernameByID(int userID)
{
    String sql = "SELECT username FROM users WHERE userID = ?";

    try (Connection conn = DBManager.getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(sql))
    {
        stmt.setInt(1, userID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next())
        {
            return rs.getString("username");
        }

    } catch (SQLException e)
    {
        System.out.println("Get username failed: " + e.getMessage());
    }

    return "";
}

    // Test all methods in this class
    public static void main(String[] args) {

        // Ensure database and tables exist
        DBManager.initialize();

        UserDBAccess userDB = new UserDBAccess();

        System.out.println("Inserting users...");
        userDB.insertUser("alex", "1234", "alex@email.com");
        userDB.insertUser("jordan", "abcd", "jordan@email.com");

        System.out.println("\nViewing users:");
        userDB.viewUsers();

        System.out.println("\nUpdating user email...");
        userDB.updateUserEmail(1, "alex_new@email.com");

        System.out.println("\nViewing users after update:");
        userDB.viewUsers();

        System.out.println("\nDeleting user...");
        userDB.deleteUser(2);

        System.out.println("\nViewing users after delete:");
        userDB.viewUsers();
    }
}

