package IAHouseholdBudget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBAccess
{
    public void viewUsers()
    {
        String sql = "SELECT * FROM users";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        )
        {
            while (rs.next())
            {
                System.out.println(
                    rs.getInt("userID") + " | " +
                    rs.getString("username") + " | " +
                    rs.getString("email")
                );
            }
        }
        catch (SQLException e)
        {
            System.out.println("View users failed: " + e.getMessage());
        }
    }

    public boolean updateUserEmail(int userID, String newEmail)
    {
        String sql = "UPDATE users SET email = ? WHERE userID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setString(1, newEmail);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Update user failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int userID)
    {
        String sql = "DELETE FROM users WHERE userID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, userID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Delete user failed: " + e.getMessage());
            return false;
        }
    }

    public int loginUser(String username, String password)
    {
        String sql = "SELECT userID FROM users WHERE username = ? AND password = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return rs.getInt("userID");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Login failed: " + e.getMessage());
        }

        return 0;
    }

    public boolean insertUser(String username, String password, String email)
    {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Insert user failed: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameByID(int userID)
    {
        String sql = "SELECT username FROM users WHERE userID = ?";

        try (
            Connection conn = DBManager.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        )
        {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                return rs.getString("username");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Get username failed: " + e.getMessage());
        }

        return "";
    }

    public static void main(String[] args)
    {
        DBManager.initialize();
        UserDBAccess userDB = new UserDBAccess();
        userDB.viewUsers();
    }
}