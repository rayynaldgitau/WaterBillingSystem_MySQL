package dao;

import model.Complaint;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLComplaintDAO implements ComplaintDAO {
    private String url = "jdbc:mysql://localhost:3306/water_billing_db";
    private String user = "root";
    private String password = ""; // Default XAMPP

    @Override
    public void addComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaints (description, status) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, complaint.getDescription());
            stmt.setString(2, complaint.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                // Adjust this based on your Complaint model constructor
                list.add(new Complaint(rs.getString("description"))); 
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // This is the Resolve logic for Step 3
    public void updateStatus(String description) {
        String sql = "UPDATE complaints SET status = 'Resolved' WHERE description = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, description);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
