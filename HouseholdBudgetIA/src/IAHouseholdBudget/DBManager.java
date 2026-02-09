package IAHouseholdBudget;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * DBManager
 * Handles database creation, table creation,
 * and provides database connections for the IA.
 */
public class DBManager {

    // Database name used for the IA
    private static final String DB_NAME = "compsci_ia";

    // Base MySQL URL (no database selected)
    private static final String URL = "jdbc:mysql://localhost:3306/";

    // MySQL login credentials
    private static final String USER = "root";
    private static final String PASSWORD = "mysql1"; // change if needed

    // Runs database and table setup
    public static void initialize() {
        createDatabase();
        createTables();
    }

    // Connects to MySQL server (used to create database)
    public static Connection getServerConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Server connection error: " + e.getMessage());
            return null;
        }
    }

    // Connects directly to the IA database
    public static Connection getDBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
            return null;
        }
    }

    // Creates the database if it does not already exist
    private static void createDatabase() {
        String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;

        try (Connection conn = getServerConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("Database created or already exists.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Creates all required tables for the application
    private static void createTables() {

        String usersTable =
            "CREATE TABLE IF NOT EXISTS users (" +
            "userID INT AUTO_INCREMENT PRIMARY KEY, " +
            "username VARCHAR(100) NOT NULL UNIQUE, " +
            "password VARCHAR(100) NOT NULL, " +
            "email VARCHAR(100) NOT NULL" +
            ")";

        String categoryTable =
            "CREATE TABLE IF NOT EXISTS category (" +
            "categoryID INT AUTO_INCREMENT PRIMARY KEY, " +
            "categoryName VARCHAR(150) NOT NULL, " +
            "userID INT, " +
            "FOREIGN KEY (userID) REFERENCES users(userID)" +
            ")";

        String budgetTable =
            "CREATE TABLE IF NOT EXISTS budget (" +
            "budgetID INT AUTO_INCREMENT PRIMARY KEY, " +
            "month INT NOT NULL, " +
            "year INT NOT NULL, " +
            "amount DECIMAL(10,2) NOT NULL, " +
            "userID INT, " +
            "FOREIGN KEY (userID) REFERENCES users(userID)" +
            ")";

        String expenseTable =
            "CREATE TABLE IF NOT EXISTS expense (" +
            "expenseID INT AUTO_INCREMENT PRIMARY KEY, " +
            "amount DECIMAL(10,2) NOT NULL, " +
            "expenseDate DATE NOT NULL, " +
            "note VARCHAR(150), " +
            "month INT, " +
            "year INT, " +
            "userID INT, " +
            "categoryID INT, " +
            "FOREIGN KEY (userID) REFERENCES users(userID), " +
            "FOREIGN KEY (categoryID) REFERENCES category(categoryID)" +
            ")";

        try (Connection conn = getDBConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(usersTable);
            stmt.executeUpdate(categoryTable);
            stmt.executeUpdate(budgetTable);
            stmt.executeUpdate(expenseTable);

            System.out.println("All tables created or already exist.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Run this class first to set up the database
    public static void main(String[] args) {
        DBManager.initialize();
    }
}

