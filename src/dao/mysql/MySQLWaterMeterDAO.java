package dao.mysql;

import dao.WaterMeterDAO;
import model.WaterMeter;
import util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLWaterMeterDAO extends WaterMeterDAO {

    @Override
    public void save(WaterMeter m) {
        String sql = "INSERT INTO water_meter (meterID, customerID, installationDate, status) " +
                     "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE customerID=?, installationDate=?, status=?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, m.getMeterID());
            ps.setInt(2, m.getCustomerID());
            ps.setDate(3, Date.valueOf(m.getInstallationDate()));
            ps.setString(4, m.getStatus());
            ps.setInt(5, m.getCustomerID());
            ps.setDate(6, Date.valueOf(m.getInstallationDate()));
            ps.setString(7, m.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public WaterMeter findById(int id) {
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(
                "SELECT * FROM water_meter WHERE meterID = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public WaterMeter findByCustomerId(int cid) {
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(
                "SELECT * FROM water_meter WHERE customerID = ? LIMIT 1")) {
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public List<WaterMeter> findAll() {
        List<WaterMeter> list = new ArrayList<>();
        try (Statement st = DatabaseManager.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM water_meter")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    @Override
    public int count() {
        try (Statement st = DatabaseManager.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM water_meter")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private WaterMeter mapRow(ResultSet rs) throws SQLException {
        return new WaterMeter(rs.getInt("meterID"), rs.getInt("customerID"),
                rs.getDate("installationDate").toLocalDate(), rs.getString("status"));
    }
}
