package dao;

import model.Administrator;
import java.sql.*;

public class MySQLAdminDAO implements AdminDAO {
    // Database connection details
    private String url = "jdbc:mysql://localhost:3306/water_billing_db";
    private String user = "root";
    private String password = ""; // Default XAMPP password is empty

    @Override
    public boolean authenticate(String username, String password) {
        // Option A: Simple Hardcoded Login (Best for quick demos)
        if (username.equals("admin") && password.equals("1234")) {
            return true;
        }

        // Option B: Database Login (Uncomment this if you want to use a 'users' table)
        /*
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a user is found
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
        return false;
    }

    @Override
    public Administrator getAdminDetails(String username) {
        // Creates the session details for the logged-in user
        return new Administrator(username, "System Administrator");
    }
}