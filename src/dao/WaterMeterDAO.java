package dao;

import model.WaterMeter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterMeterDAO {
    private Map<Integer, WaterMeter> data = new HashMap<>();
    public void save(WaterMeter m) { data.put(m.getMeterID(), m); }
    public WaterMeter findById(int id) { return data.get(id); }
    public WaterMeter findByCustomerId(int cid) { for (WaterMeter m : data.values()) if (m.getCustomerID() == cid) return m; return null; }
    public List<WaterMeter> findAll() { return new ArrayList<>(data.values()); }
    public int count() { return data.size(); }
}
