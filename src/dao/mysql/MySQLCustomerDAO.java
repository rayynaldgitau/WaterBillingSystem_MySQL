package dao.mysql;

import dao.CustomerDAO;
import model.Customer;
import util.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL-backed CustomerDAO. Overrides all in-memory methods with JDBC queries.
 * Uses PreparedStatements to prevent SQL injection (OWASP best practice).
 */
public class MySQLCustomerDAO extends CustomerDAO {

    @Override
    public void save(Customer c) {
        String sql = "INSERT INTO customer (customerID, name, address, phoneNumber, email, password, active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE name=?, address=?, phoneNumber=?, email=?, password=?, active=?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, c.getCustomerID());
            ps.setString(2, c.getName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getPhoneNumber());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getPassword());
            ps.setBoolean(7, c.isActive());
            // ON DUPLICATE KEY UPDATE values
            ps.setString(8, c.getName());
            ps.setString(9, c.getAddress());
            ps.setString(10, c.getPhoneNumber());
            ps.setString(11, c.getEmail());
            ps.setString(12, c.getPassword());
            ps.setBoolean(13, c.isActive());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("DB error saving customer: " + e.getMessage(), e); }
    }

    @Override
    public Customer findById(int id) {
        String sql = "SELECT * FROM customer WHERE customerID = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Customer findByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE email = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer ORDER BY customerID";
        try (Statement st = DatabaseManager.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM customer WHERE customerID = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public int count() {
        try (Statement st = DatabaseManager.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM customer")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer c = new Customer(rs.getInt("customerID"), rs.getString("name"),
                rs.getString("address"), rs.getString("phoneNumber"),
                rs.getString("email"), rs.getString("password"));
        c.setActive(rs.getBoolean("active"));
        return c;
    }
}
