package dao;

import java.sql.*;
import model.Bill;

public class MySQLBillDAO {
    private String url = "jdbc:mysql://localhost:3306/water_billing_db";
    private String user = "root";
    private String password = "";

    public void saveBill(Bill bill) {
        String sql = "INSERT INTO bills (customer_name, units_consumed, total_amount) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, bill.getCustomerName());
            pstmt.setDouble(2, bill.getUnits());
            pstmt.setDouble(3, bill.getTotalAmount());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}