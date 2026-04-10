package service;

import java.sql.*;

public class ReportingService {
    private String url = "jdbc:mysql://localhost:3306/water_billing_db";
    private String user = "root";
    private String password = "";

    public int getTotalComplaints() {
        return getCount("SELECT COUNT(*) FROM complaints");
    }

    public int getTotalUsers() {
        return getCount("SELECT COUNT(*) FROM users");
    }

    public double getTotalRevenue() {
        double total = 0;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SUM(total_amount) FROM bills")) {
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    private int getCount(String sql) {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getTotalComplaintsCount() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}